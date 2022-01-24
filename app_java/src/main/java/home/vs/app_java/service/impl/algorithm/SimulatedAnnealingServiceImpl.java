package home.vs.app_java.service.impl.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.trucking_industry.DeliveryPoint;
import home.vs.app_java.dto.trucking_industry.ExtendedGoods;
import home.vs.app_java.dto.trucking_industry.Goods;
import home.vs.app_java.dto.trucking_industry.ListsOfTruckingIndustryEntities;
import home.vs.app_java.dto.trucking_industry.PairOfSequences;
import home.vs.app_java.dto.trucking_industry.Truck;
import home.vs.app_java.dto.trucking_industry.TruckRoute;
import home.vs.app_java.service.OsrmInteractionService;
import home.vs.app_java.service.RouteFindingAlgorithm;

@Service
public class SimulatedAnnealingServiceImpl implements RouteFindingAlgorithm {
    @Autowired
    private OsrmInteractionService osrmInteractionService;
    private Random random = new Random();

    @Override
    public List<TruckRoute> getRoutes (ListsOfTruckingIndustryEntities listsOfTruckingIndustryEntities) {
        //создаём для удобства, чтобы в последующем легко доставать координаты пункта доставки
        final Map<Integer, CoordinateDTO> tmpMapOfDeliveryPointsCoordinates = new HashMap<>();
        for (DeliveryPoint deliveryPoint : listsOfTruckingIndustryEntities.getListOfDeliveryPoints()) {
            tmpMapOfDeliveryPointsCoordinates.put(deliveryPoint.getId(), deliveryPoint.getCoordinate());
        }
        //создаём последовательности товаров(расширенный координатами пункта доставки) и грузоперевозчиков для последующей перестановки
        final List<ExtendedGoods> sequenceOfGoods = new LinkedList<>();
        for (Goods goods : listsOfTruckingIndustryEntities.getListOfGoods()) {
            sequenceOfGoods.add(new ExtendedGoods(goods, tmpMapOfDeliveryPointsCoordinates.get(goods.getDeliveryPointId())));
        }
        final List<Truck> sequenceOfTruck = new LinkedList<>(listsOfTruckingIndustryEntities.getListOfTrucks());

        PairOfSequences sequences = new PairOfSequences(sequenceOfGoods, sequenceOfTruck);

        sequences = this.getSequencesAfterTheAlgorithm(sequences);
        return this.convertSequencesToRoutes(sequences);
    }

    private Map<Integer, List<CoordinateDTO>> getMapListOfcoordinates(PairOfSequences sequences) {
        Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates = new HashMap<>();
        //максимальная грузоподъёмность из выбранных грузовых машин
        Integer maxCarryingOfTruck = 0;
        for (Truck truck : sequences.getTrucks()) {
            if (maxCarryingOfTruck < truck.getCarrying()) {
                maxCarryingOfTruck = truck.getCarrying();
            }
        }
        //создание итераторов для товаров c последующим удалением если он больше грузоподъёмности грузоперевозчика
        Iterator<ExtendedGoods> goodsIterator = sequences.getGoods().iterator();
        while (goodsIterator.hasNext()) {
            ExtendedGoods nextGoods = goodsIterator.next();
            if (nextGoods.getWeight() > maxCarryingOfTruck) {
                goodsIterator.remove();
            }
        }
        Iterator<Truck> truckIterator = sequences.getTrucks().iterator();
        int totalWeight = 0; //общий вес
        Truck truck = null;
        ExtendedGoods goods = null;
        totalWeight = 0; //общий вес
        goodsIterator = sequences.getGoods().iterator();//обновляем итератор
        truck = truckIterator.next();
        while (goodsIterator.hasNext()) {
            goods = goodsIterator.next();
            totalWeight += goods.getWeight();
            if (truck.getCarrying() >= totalWeight) {
                mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
            } else {
                do {
                    if (!truckIterator.hasNext()) {
                        truckIterator = sequences.getTrucks().iterator();
                    }
                    truck = truckIterator.next();
                    totalWeight = goods.getWeight();
                } while (truck.getCarrying() < totalWeight);
                mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
            }
        }
        return mapOfListOfCoordinates;
    }

    private Map<Integer, List<CoordinateDTO>> addCoordinatesByCondition(Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates, Truck truck, ExtendedGoods goods) {
        if (mapOfListOfCoordinates.containsKey(truck.getId())) {//если он есть значит игнорируем его местоположение
            List<CoordinateDTO> innerCoordinates = mapOfListOfCoordinates.get(truck.getId());
            innerCoordinates.add(goods.getCoordinate());
            innerCoordinates.add(goods.getDeliveryPointCoordinate());
        } else {//если он есть значит добавляем его местоположение и ложим в map
            List<CoordinateDTO> newCoordinates = new LinkedList<>();
            newCoordinates.add(truck.getCoordinate());
            newCoordinates.add(goods.getCoordinate());
            newCoordinates.add(goods.getDeliveryPointCoordinate());
            mapOfListOfCoordinates.put(truck.getId(), newCoordinates);
        }
        return mapOfListOfCoordinates;
    }

    private double getSumLengthOfRoute(PairOfSequences sequences) {
        double sum = 0;
        //получаем карту массивов координат из вложенной последовательности
        final Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates = this.getMapListOfcoordinates(sequences);
        for (Map.Entry<Integer, List<CoordinateDTO>> entry : mapOfListOfCoordinates.entrySet()) {
            sum += osrmInteractionService.getLengthOfRoute(entry.getValue());
        }
        return sum;
    }

    private PairOfSequences getNewPairOfSequences(PairOfSequences sequences) {
        List<ExtendedGoods> newSequenceOfGoods = getNewSequenceOfGoods(sequences.getGoods());
        List<Truck> newSequenceOfTruck = getNewSequencesOfTrucks(sequences.getTrucks());
        return new PairOfSequences(newSequenceOfGoods, newSequenceOfTruck);
    }

    private List<ExtendedGoods> getNewSequenceOfGoods(List<ExtendedGoods> sequenceOfGoods) {
		int min = 0;
		int max = sequenceOfGoods.size() - 1;
		List<Integer> intList = this.random.ints(2, min, max)
			.boxed()
			.collect(Collectors.toList());
		if (intList.get(0) > intList.get(1)) {
			Collections.reverse(intList);
		}
		int begin = intList.get(0);
		int end = intList.get(1);
		List<ExtendedGoods> beginPath = new LinkedList<>();
		List<ExtendedGoods> middlePath = new LinkedList<>();
		List<ExtendedGoods> endPath = new LinkedList<>();
		for (int i = 0; i < sequenceOfGoods.size(); i++) {
			if (i < begin) {
				beginPath.add(sequenceOfGoods.get(i));
			}
			if (i >= begin && i <= end) {
				middlePath.add(sequenceOfGoods.get(i));
			}
			if (i > end) {
				endPath.add(sequenceOfGoods.get(i));
			}
		}

		Collections.reverse(middlePath); //производим реверс выбранного кусочка последовательности

		List<ExtendedGoods> newSequenceOfGoods = new LinkedList<>();
		newSequenceOfGoods.addAll(beginPath);
		newSequenceOfGoods.addAll(middlePath);
		newSequenceOfGoods.addAll(endPath);

        return newSequenceOfGoods;
    }

    private List<Truck> getNewSequencesOfTrucks(List<Truck> sequenceOfTruck) {
		int min = 0;
		int max = sequenceOfTruck.size() - 1;
		List<Integer> intList = this.random.ints(2, min, max)
			.boxed()
			.collect(Collectors.toList());
		List<Truck> newSequenceOfTrucks = new LinkedList<>(sequenceOfTruck);
        Collections.swap(newSequenceOfTrucks, intList.get(0), intList.get(1));//меняем местами 2 случайных элемента

        return newSequenceOfTrucks;
    }

    private List<TruckRoute> convertSequencesToRoutes(PairOfSequences sequences) {
        //получаем карту массивов координат из вложенной последовательности
        final Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates = this.getMapListOfcoordinates(sequences);
        //
        final List<TruckRoute> listOfRoutes = new ArrayList<>(mapOfListOfCoordinates.size());
        for (Map.Entry<Integer, List<CoordinateDTO>> entry : mapOfListOfCoordinates.entrySet()) {
            TruckRoute route = osrmInteractionService.getRoute(entry.getValue());
            route.setTruckId(entry.getKey());
            listOfRoutes.add(route);
        }
        
        return listOfRoutes;
    }

    private PairOfSequences getSequencesAfterTheAlgorithm(PairOfSequences sequences) {
        // final Integer kEnd = (sequences.getGoods().size()/sequences.getTrucks().size() + sequences.getTrucks().size()) * 10;
        final int kMax = 1000;
        final double T0 = 1;
        PairOfSequences newSequences;
        for (int k = 1; k <= kMax; k++) {
            double kT = T0/((double)k/(double)kMax);
            double T = boltzmann_annealing(k, kT);
            newSequences = this.getNewPairOfSequences(sequences.copy());
            double oldDistance = this.getSumLengthOfRoute(sequences);
            double newDistance = this.getSumLengthOfRoute(newSequences);
            if (this.P(oldDistance, newDistance, T) >= Math.random()) {
                sequences = newSequences;
            }
        }
        return sequences;
    }

    // метод ближайшего соседа
    private List<CoordinateDTO> nearestNeighbor(List<CoordinateDTO> coordinates) {
        return null;
    }

    // Больцманский отжиг
    private double boltzmann_annealing(double k, double T0) { // T0 = 1

        return T0 / Math.log(1 + k);
    }

    // отжиг Коши
    private double koshi_annealing(double k, double T0) { // T0 = 1
        return T0 / k;
    }

    // отжиг
    private double annealing(double k, double T0) { // T0 = 1
        return T0 / Math.exp(k);
    }
    // функция дающая число, для последующего сравнения с вероятностью
    private double P(double oldDistance, double newDistance, double T){
        return Math.exp(-1 * ((newDistance - oldDistance) / T));
    }
}




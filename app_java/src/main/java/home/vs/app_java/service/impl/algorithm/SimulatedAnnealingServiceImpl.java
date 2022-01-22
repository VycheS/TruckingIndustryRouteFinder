package home.vs.app_java.service.impl.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.trucking_industry.DeliveryPoint;
import home.vs.app_java.dto.trucking_industry.ExtendedGoods;
import home.vs.app_java.dto.trucking_industry.Goods;
import home.vs.app_java.dto.trucking_industry.ListsOfTruckingIndustryEntities;
import home.vs.app_java.dto.trucking_industry.Truck;
import home.vs.app_java.dto.trucking_industry.TruckRoute;
import home.vs.app_java.service.OsrmInteractionService;
import home.vs.app_java.service.RouteFindingAlgorithm;

@Service
public class SimulatedAnnealingServiceImpl implements RouteFindingAlgorithm {
    @Autowired
    private OsrmInteractionService osrmInteractionService;

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
        //получаем карту массивов координат из вложенной последовательности
        final Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates = this.getMapListOfcoordinates(sequenceOfGoods, sequenceOfTruck);
        //
        final List<TruckRoute> listOfRoutes = new ArrayList<>(mapOfListOfCoordinates.size());
        for (Map.Entry<Integer, List<CoordinateDTO>> entry : mapOfListOfCoordinates.entrySet()) {
            List<CoordinateDTO> route = osrmInteractionService.getRoute(entry.getValue());
            TruckRoute truckRoute = new TruckRoute(entry.getKey(), route);
            listOfRoutes.add(truckRoute);
        }
        
        return listOfRoutes;
    }

    private Map<Integer, List<CoordinateDTO>> getMapListOfcoordinates(List<ExtendedGoods> sequenceOfGoods, List<Truck> sequenceOfTruck) {
        Map<Integer, List<CoordinateDTO>> mapOfListOfCoordinates = new HashMap<>();
        //максимальная грузоподъёмность из выбранных грузовых машин
        Integer maxCarryingOfTruck = 0;
        for (Truck truck : sequenceOfTruck) {
            if (maxCarryingOfTruck < truck.getCarrying()) {
                maxCarryingOfTruck = truck.getCarrying();
            }
        }
        //создание итераторов для товаров c последующим удалением если он больше грузоподъёмности грузоперевозчика
        Iterator<ExtendedGoods> goodsIterator = sequenceOfGoods.iterator();
        while (goodsIterator.hasNext()) {
            ExtendedGoods nextGoods = goodsIterator.next();
            if (nextGoods.getWeight() > maxCarryingOfTruck) {
                goodsIterator.remove();
            }
        }
        goodsIterator = sequenceOfGoods.iterator();//обновляем итератор
        Iterator<Truck> truckIterator = sequenceOfTruck.iterator();
        int totalWeight = 0; //общий вес
        Truck truck = null;
        ExtendedGoods goods = null;
        do {
            if (truck != null && goods != null) {
                if (totalWeight == 0) {
                    totalWeight = goods.getWeight();
                } else {
                    goods = goodsIterator.next();
                    totalWeight += goods.getWeight();
                }

                if (totalWeight < truck.getCarrying() && !truckIterator.hasNext()) { //нет впереди следующего грузоперевозчика и груз влезает
                    //ложим данные по условие
                    mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
                    //обновляем итератор
                    truckIterator = sequenceOfTruck.iterator();
                } else if (totalWeight > truck.getCarrying() && !truckIterator.hasNext()) { // груз не влезает и нет впереди следующего грузоперевозчика
                    //обновляем итератор
                    truckIterator = sequenceOfTruck.iterator();
                    //обнуляем общий вес
                    totalWeight = 0;
                    //следующий грузоперевозчик
                    truck = truckIterator.next();
                } else if (totalWeight > truck.getCarrying() && truckIterator.hasNext()){// груз не влезает, впереди есть грузоперевозчик
                    //обнуляем общий вес
                    totalWeight = 0;
                    ////следующий грузоперевозчик
                    truck = truckIterator.next();
                } else if (totalWeight < truck.getCarrying() && truckIterator.hasNext()){// груз влезает, впереди есть грузоперевозчик
                    //ложим данные по условие
                    mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
                }
            } else {
                goods = goodsIterator.next();
                totalWeight += goods.getWeight();

                do {   
                    truck = truckIterator.next();
                } while (totalWeight > truck.getCarrying() && truckIterator.hasNext());

                if (totalWeight < truck.getCarrying() && !truckIterator.hasNext()) {//если одновременно нет впереди следующего грузоперевозчика и груз влезает
                    //ложим данные по условие
                    mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
                    //обновляем итератор
                    truckIterator = sequenceOfTruck.iterator();
                } else if (totalWeight < truck.getCarrying()) { // груз влезает
                    //ложим данные по условие
                    mapOfListOfCoordinates = addCoordinatesByCondition(mapOfListOfCoordinates, truck, goods);
                } else {//обновляем итератор если впереди нет грузоперевозчика
                    truckIterator = sequenceOfTruck.iterator();
                }
            }
            
        } while (goodsIterator.hasNext());
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

    // private double getAlldistance(List<Goods> sequenceOfGoods, List<Truck> sequenceOfTruck, Map<Integer, CoordinateDTO> tmpMapOfDeliveryPointsCoordinates) {
        
    // }
}

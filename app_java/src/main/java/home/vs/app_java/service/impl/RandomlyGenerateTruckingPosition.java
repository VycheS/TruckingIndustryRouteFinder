package home.vs.app_java.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.trucking_industry.DeliveryPoint;
import home.vs.app_java.dto.trucking_industry.Goods;
import home.vs.app_java.dto.trucking_industry.ListsOfTruckingIndustryEntities;
import home.vs.app_java.dto.trucking_industry.Truck;

public class RandomlyGenerateTruckingPosition {
    private final Random random = new Random();
    public ListsOfTruckingIndustryEntities generate(CoordinateDTO center, int numberOfGeoObject) {
        List<Double> x = this.random.doubles(numberOfGeoObject, -0.01, 0.01)
			.boxed()
			.collect(Collectors.toList());
        List<Double> y = this.random.doubles(numberOfGeoObject, -0.01, 0.01)
			.boxed()
			.collect(Collectors.toList());
        int shift = (int)Math.round(((double)numberOfGeoObject/100)*30);//делаем сдвиг, для для увеличения количества товаров на 30%
        List<Integer> type = this.random.ints((numberOfGeoObject - shift), 1, 4)
			.boxed()
			.collect(Collectors.toList());
        for (int i = 0; i <= shift; i++) {
            type.add(2); //goods
        }
        int countDeliveryPoint = 0;
        for (int tp : type) {
            if (tp == 3) {
                countDeliveryPoint += 1;
            }
        }
        List<Integer> goodsWeight = this.random.ints(numberOfGeoObject, 50, 300)
            .boxed()
            .collect(Collectors.toList());
        List<Integer> truckCarrying = this.random.ints(numberOfGeoObject, 70, 500)
            .boxed()
            .collect(Collectors.toList());
        List<Integer> goodsSendTo = this.random.ints(numberOfGeoObject, 0, (countDeliveryPoint - 1))
            .boxed()
            .collect(Collectors.toList());
        List<Truck> listOfTrucks = new LinkedList<>();                //1
        List<Goods> listOfGoods = new LinkedList<>();                 //2
        List<DeliveryPoint> listOfDeliveryPoints = new LinkedList<>();//3
        for (int i = 0; i < numberOfGeoObject; i++) {
            if (type.get(i) == 1) {
                listOfTrucks.add(new Truck(i, String.valueOf(i), truckCarrying.get(i), new CoordinateDTO(center.getLatitude() + y.get(i), center.getLongitude() + x.get(i))));
            } else if (type.get(i) == 3) {
                listOfDeliveryPoints.add(new DeliveryPoint(i, String.valueOf(i), new CoordinateDTO(center.getLatitude() + y.get(i), center.getLongitude() + x.get(i))));
            }
        }
        for (int i = 0; i < numberOfGeoObject; i++) {
            if (type.get(i) == 2) {
                listOfGoods.add(new Goods(i, String.valueOf(i), goodsWeight.get(i), listOfDeliveryPoints.get(goodsSendTo.get(i)).getId(), new CoordinateDTO(center.getLatitude() + y.get(i), center.getLongitude() + x.get(i))));
            }
        }
        return new ListsOfTruckingIndustryEntities(listOfTrucks, listOfGoods, listOfDeliveryPoints);
    }
}

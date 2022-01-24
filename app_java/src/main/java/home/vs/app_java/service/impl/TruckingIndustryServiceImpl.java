package home.vs.app_java.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dao.GeoObjectDAO;
import home.vs.app_java.dao.LayerDAO;
import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.dto.trucking_industry.DeliveryPoint;
import home.vs.app_java.dto.trucking_industry.Goods;
import home.vs.app_java.dto.trucking_industry.ListsOfTruckingIndustryEntities;
import home.vs.app_java.dto.trucking_industry.Truck;
import home.vs.app_java.dto.trucking_industry.TruckRoute;
import home.vs.app_java.service.RouteFindingAlgorithm;
import home.vs.app_java.service.TruckingIndustryService;

import home.vs.app_java.exception.*;

@Service
public class TruckingIndustryServiceImpl implements TruckingIndustryService {
    @Autowired
    private LayerDAO layerDAO;
    @Autowired
    private GeoObjectDAO geoObjectDAO;
    @Autowired
    private RouteFindingAlgorithm routeFindingAlgorithm;

    private RandomlyGenerateTruckingPosition randomlyGenerateTruckingPosition = new RandomlyGenerateTruckingPosition();

    @Override
    public Boolean findRoutes(int layerGroupId, int clientId) {
        final List<LayerDTO> pointLayers = this.layerDAO.getAll(layerGroupId, clientId);
        if (pointLayers != null && !pointLayers.isEmpty()) {
            final Map<LayerDTO, List<GeoObjectDTO>> mapOfListOfGeoObjects = new HashMap<>();
            for (LayerDTO layer : pointLayers) {
                List<GeoObjectDTO> geoObjects = this.geoObjectDAO.getAll(layer.getId(), layerGroupId, clientId);
                if (geoObjects != null && !geoObjects.isEmpty()) {
                    mapOfListOfGeoObjects.put(layer, geoObjects);
                }
            }
            ListsOfTruckingIndustryEntities listsOfTruckingIndustryEntities = this.separationByEntities(pointLayers, mapOfListOfGeoObjects);
            if (listsOfTruckingIndustryEntities != null) {
                final List<Truck> listOfTrucks  = listsOfTruckingIndustryEntities.getListOfTrucks();
                final List<Goods> listOfGoods = listsOfTruckingIndustryEntities.getListOfGoods();
                final List<DeliveryPoint> listOfDeliveryPoints = listsOfTruckingIndustryEntities.getListOfDeliveryPoints();
                List<TruckRoute> listOfRoutes = routeFindingAlgorithm.getRoutes(new ListsOfTruckingIndustryEntities(listOfTrucks, listOfGoods, listOfDeliveryPoints));
                if (listOfRoutes != null && !listOfRoutes.isEmpty()) {
                    for (TruckRoute route : listOfRoutes) {
                        LayerDTO layer = new LayerDTO();
                        String truckName = "";
                        for (Truck truck : listOfTrucks) {
                            if (truck.getId() == route.getTruckId()) {
                                truckName = truck.getName();
                            }
                        }
                        layer.setName("truck route " + truckName);
                        layer.setDescription("route");
                        layer.setTypeObj("line");
                        String strJsonMapOptionsGeoObject = "\"options\":{\"opacity\":0.5,\"geodesic\":true,\"strokeWidth\": 5},\"properties\":{}";
                        String strJsonTruckingIndustryOptionsGeoObject = "\"trucking_industry\":{\"type\":\"route\"}";
                        layer.setStrJson("{" + strJsonTruckingIndustryOptionsGeoObject + "}");
                        UUID layerId = layerDAO.save(layer, layerGroupId, clientId);
                        GeoObjectDTO geoObject = new GeoObjectDTO();
                        geoObject.setCoordinates(route.getCoordinates());
                        geoObject.setDescription(String.valueOf(route.getLength()));
                        geoObject.setName(truckName);
                        geoObject.setType("line");
                        geoObject.setStrJson("{" + strJsonMapOptionsGeoObject + "," + strJsonTruckingIndustryOptionsGeoObject + "}");
                        geoObjectDAO.save(geoObject, layerId, layerGroupId, clientId);
                    }
                    return true;
                }
            }            
        }
        return false;
    }

    @Override
    public Boolean randomlyGenerate(int layerGroupId, int clientId) {
        final ListsOfTruckingIndustryEntities lists = this.randomlyGenerateTruckingPosition.generate(new CoordinateDTO(52.03848435832438,113.50342766583945), 30);
        final List<Truck> listOfTrucks = lists.getListOfTrucks();
        final List<Goods> listOfGoods = lists.getListOfGoods();
        final List<DeliveryPoint> listOfDeliveryPoints = lists.getListOfDeliveryPoints();
        //truck
        final LayerDTO truckLayer = new LayerDTO();
        truckLayer.setName("грузоперевозчики");
        truckLayer.setDescription("trucks");
        truckLayer.setTypeObj("point");
        String strJsonMapOptionsTruck = "\"options\":{\"preset\":\"islands#blueAutoIcon\"},\"properties\":{}";
        truckLayer.setStrJson("{\"trucking_industry\":{\"type\":\"truck\"}}");
        final UUID truckLayerId = this.layerDAO.save(truckLayer, layerGroupId, clientId);
        for (Truck truck : listOfTrucks) {
            final String strJsonTruckingIndustryOptionsGeoObject = "\"trucking_industry\":{\"type\":\"truck\",\"carrying\":"+truck.getCarrying()+"}";
            final GeoObjectDTO geoObject = new GeoObjectDTO();
            geoObject.setCoordinates(Arrays.asList(truck.getCoordinate()));
            geoObject.setDescription("truck");
            geoObject.setName(truck.getName());
            geoObject.setType("point");
            geoObject.setStrJson("{" + strJsonMapOptionsTruck + "," + strJsonTruckingIndustryOptionsGeoObject + "}");
            this.geoObjectDAO.save(geoObject, truckLayerId, layerGroupId, clientId);
        }
        //goods
        final LayerDTO goodsLayer = new LayerDTO();
        goodsLayer.setName("товары");
        goodsLayer.setDescription("goods");
        goodsLayer.setTypeObj("point");
        final String strJsonMapOptionsGoods = "\"options\":{\"preset\":\"islands#darkOrangePocketCircleIcon\"},\"properties\":{}";
        goodsLayer.setStrJson("{\"trucking_industry\":{\"type\":\"goods\"}}");
        final UUID goodsLayerId = this.layerDAO.save(goodsLayer, layerGroupId, clientId);
        for (Goods goods : listOfGoods) {
            final String strJsonTruckingIndustryOptionsGeoObject = "\"trucking_industry\":{\"type\":\"goods\",\"weight\":"+goods.getWeight()+",\"deliveryPoint\":"+goods.getDeliveryPointId()+"}";
            final GeoObjectDTO geoObject = new GeoObjectDTO();
            geoObject.setCoordinates(Arrays.asList(goods.getCoordinate()));
            geoObject.setDescription("goods");
            geoObject.setName(goods.getName());
            geoObject.setType("point");
            geoObject.setStrJson("{" + strJsonMapOptionsGoods + "," + strJsonTruckingIndustryOptionsGeoObject + "}");
            this.geoObjectDAO.save(geoObject, goodsLayerId, layerGroupId, clientId);
        }
        //delivery point
        final LayerDTO deliveryPointLayer = new LayerDTO();
        deliveryPointLayer.setName("пункты доставки");
        deliveryPointLayer.setDescription("deliveryPoint");
        deliveryPointLayer.setTypeObj("point");
        final String strJsonMapOptionsDeliveryPoint = "\"options\":{\"preset\":\"islands#nightCircleDotIcon\"},\"properties\":{}";
        deliveryPointLayer.setStrJson("{\"trucking_industry\":{\"type\":\"deliveryPoint\"}}");
        final UUID deliveryPointLayerId = this.layerDAO.save(deliveryPointLayer, layerGroupId, clientId);
        for (DeliveryPoint deliveryPoint : listOfDeliveryPoints) {
            final String strJsonTruckingIndustryOptionsGeoObject = "\"trucking_industry\":{\"type\":\"deliveryPoint\"}";
            final GeoObjectDTO geoObject = new GeoObjectDTO();
            geoObject.setCoordinates(Arrays.asList(deliveryPoint.getCoordinate()));
            geoObject.setDescription("deliveryPoint");
            geoObject.setName(deliveryPoint.getName());
            geoObject.setType("point");
            geoObject.setStrJson("{" + strJsonMapOptionsDeliveryPoint + "," + strJsonTruckingIndustryOptionsGeoObject + "}");
            this.geoObjectDAO.save(geoObject, deliveryPointLayerId, layerGroupId, clientId);
        }

        return true;

    }
    
    private ListsOfTruckingIndustryEntities separationByEntities (List<LayerDTO> pointLayers, Map<LayerDTO, List<GeoObjectDTO>> mapOfListOfGeoObjects) {
        final List<Truck> listOfTrucks = new LinkedList<>();
        final List<Goods> listOfGoods = new LinkedList<>();
        final List<DeliveryPoint> listOfDeliveryPoints = new LinkedList<>();
        final Map<String, DeliveryPoint> mapOfDeliveryPoints = new HashMap<>();
        final Map<String, Goods> matchingOfDeliveryPointNameAndGoods = new HashMap<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String truckingIndustryLabel = "trucking_industry";
        for (LayerDTO layer : pointLayers) {
            try {
                List<GeoObjectDTO> geoObjects = mapOfListOfGeoObjects.get(layer);
                if (geoObjects != null && !geoObjects.isEmpty()) {
                    JsonNode jsonNodeContainer = objectMapper.readTree(layer.getStrJson());
                    String container = jsonNodeContainer.get(truckingIndustryLabel).toString();
                    JsonNode jsonNode = objectMapper.readTree(container);
                    final String truckingIndustryType = jsonNode.get("type").asText();
                    for (GeoObjectDTO geoObject : geoObjects) {
                        if (truckingIndustryType.equals("deliveryPoint")) {
                            DeliveryPoint deliveryPoint = new DeliveryPoint(geoObject.getId(), geoObject.getName(), geoObject.getCoordinates().get(0));
                            mapOfDeliveryPoints.put(geoObject.getName(), deliveryPoint);
                            listOfDeliveryPoints.add(deliveryPoint);
                        } else if (truckingIndustryType.equals("goods")) {
                            jsonNode = objectMapper.readTree(geoObject.getStrJson());
                            jsonNode = objectMapper.readTree(jsonNode.get(truckingIndustryLabel).toString());
                            final int weight = jsonNode.get("weight").asInt();
                            final String deliveryPointName = jsonNode.get("deliveryPoint").asText();
                            matchingOfDeliveryPointNameAndGoods.put(deliveryPointName, new Goods(geoObject.getId(), geoObject.getName(), weight, geoObject.getCoordinates().get(0)));
                        } else if (truckingIndustryType.equals("truck")) {
                            jsonNode = objectMapper.readTree(geoObject.getStrJson());
                            jsonNode = objectMapper.readTree(jsonNode.get(truckingIndustryLabel).toString());
                            final int carrying = jsonNode.get("carrying").asInt();
                            listOfTrucks.add(new Truck(geoObject.getId(), geoObject.getName(), carrying, geoObject.getCoordinates().get(0)));
                        } else throw new TruckingIndustryException(String.format("Такой грузоперевозочной единицы типа как: %s, не существует", truckingIndustryType));
                    }
                }
            } catch (TruckingIndustryException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, Goods> entry : matchingOfDeliveryPointNameAndGoods.entrySet()) {
            DeliveryPoint deliveryPoint = mapOfDeliveryPoints.get(entry.getKey());
            Goods goods = entry.getValue();
            goods.setDeliveryPointId(deliveryPoint.getId());
            listOfGoods.add(goods);
        }
        return !(listOfTrucks.isEmpty() || listOfGoods.isEmpty() || listOfDeliveryPoints.isEmpty())
            ? new ListsOfTruckingIndustryEntities(listOfTrucks, listOfGoods, listOfDeliveryPoints)
            : null;
    }
    
}

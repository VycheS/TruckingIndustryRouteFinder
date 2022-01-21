package home.vs.app_java.service;

public interface TruckingIndustryService {

    /**
     * Возвращает список слоёв найденных маршрутов.
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return возвращает в случае успеха 200.
     */
    Boolean findRoutes(int layerGroupId, int clientId);
}

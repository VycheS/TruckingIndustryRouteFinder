package home.vs.app_java.service;

import java.util.List;
import java.util.UUID;

import home.vs.app_java.dto.GeoObjectDTO;

public interface GeoObjectService {
    /**
     * Создаёт новый слой.
     * @param geoObject - геообъект для последующего создания
     * @param layerId - id слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return - true если геообъект создан, иначе false
     */
    boolean create(GeoObjectDTO geoObject, UUID layerId, int layerGroupId, int clientId);

    /**
     * Возвращает список всех имеющихся слоёв.
     * @param layerId - id слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return список объектов слоёв.
     */
    List<GeoObjectDTO> readAll(UUID layerId, int layerGroupId, int clientId);

    /**
     * Возвращает слой по его ID
     * @param id - id запрашиваемого геообъекта
     * @param layerId - id слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return объект слоя с заданным ID
     */
    GeoObjectDTO read(int id, UUID layerId, int layerGroupId, int clientId);

    /**
     * Обновляет или изменяет слой по его ID,
     * в соотвествии с переданным слоем.
     * @param geoObject - геообъект в соотвествии с которым нужно обновить данные
     * @param id - id изменяемого геообъекта
     * @param layerId - id слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return true если данные были обновлены, иначе false
     */
    boolean update(GeoObjectDTO geoObject, int id, UUID layerId, int layerGroupId, int clientId);

    /**
     * Удаляет слой по его ID
     * @param id - id геообъекта который нужно удалить
     * @param layerId - id слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return true если удалён, иначе false
     */
    boolean delete(int id, UUID layerId, int layerGroupId, int clientId);
}

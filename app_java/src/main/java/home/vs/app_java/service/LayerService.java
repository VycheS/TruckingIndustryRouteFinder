package home.vs.app_java.service;

import java.util.List;
import java.util.UUID;

import home.vs.app_java.dto.LayerDTO;

public interface LayerService {
    /**
     * Создаёт новый слой.
     * @param layer - слой для последующего создания
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return - возращает UUID созданного слоя, иначе null
     */
    UUID create(LayerDTO layer, int layerGroupId, int clientId);

    /**
     * Возвращает список всех имеющихся слоёв.
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return список объектов слоёв.
     */
    List<LayerDTO> readAll(int layerGroupId, int clientId);

    /**
     * Возвращает слой по его ID
     * @param id - ID слоя
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return объект слоя с заданным ID
     */
    LayerDTO read(UUID id, int layerGroupId, int clientId);

    /**
     * Обновляет или изменяет слой по его ID,
     * в соотвествии с переданным слоем.
     * @param layer - объект слой, в соотвествии с которым нужно обновить данные
     * @param id - ID слоя которую нужно обновить
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return true если данные были обновлены, иначе false
     */
    boolean update(LayerDTO layer, UUID id, int layerGroupId, int clientId);

    /**
     * Удаляет слой по его ID
     * @param id - ID слоя которую нужно удалить
     * @param layerGroupId - id группы слоёв
     * @param clientId - id клиента
     * @return true если удалён, иначе false
     */
    boolean delete(UUID id, int layerGroupId, int clientId);
}

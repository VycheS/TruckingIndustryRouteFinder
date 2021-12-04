package home.vs.app_java.service;

import java.util.List;

import home.vs.app_java.dto.LayerGroupDTO;

public interface LayerGroupService {
    /**
     * Создаёт новую группу.
     * @param group - группа для последующего создания
     * @param clientId - id клиента к которой принадлежит группа
     * @return - true если группа создана, иначе false
     */
    boolean create(LayerGroupDTO group, int clientId);

    /**
     * Возвращает список всех имеющихся групп принадлежащих клиенту.
     * @param clientId - id клиента к которой принадлежит группа
     * @return список объектов групп.
     */
    List<LayerGroupDTO> readAll(int clientId);

    /**
     * Возвращает группу по его ID
     * @param id - ID группы
     * @return - объект группы с заданным ID
     */
    LayerGroupDTO read(int id, int clientId);

    /**
     * Обновляет или изменяет группу по его ID,
     * в соотвествии с переданной группой.
     * @param group - объект группа, в соотвествии с которым нужно обновить данные
     * @param id - ID группы которую нужно обновить
     * @param clientId - id клиента к которой принадлежит группа
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(LayerGroupDTO group, int id, int clientId);

    /**
     * Удаляет группу по его ID
     * @param id - ID группы которую нужно удалить
     * @param clientId - id клиента к которой принадлежит группа
     * @return - true если удалён, иначе false
     */
    boolean delete(int id, int clientId);
}

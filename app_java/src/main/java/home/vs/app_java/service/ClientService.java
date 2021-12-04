package home.vs.app_java.service;

import java.util.List;

import home.vs.app_java.dto.ClientDTO;

public interface ClientService {
    /**
     * Создаёт нового клиента.
     * @param client - клиент для создания
     * @return - true если клиент создан, иначе false
     */
    boolean create(ClientDTO client);

    /**
     * Возвращает список всех имеющихся клиентов.
     * @return - список объектов клиентов.
     */
    List<ClientDTO> readAll();

    /**
     * Возвращает клиента по его ID
     * @param id - ID клиента
     * @return - объект клиента с заданным ID
     */
    ClientDTO read(int id);

    /**
     * Обновляет или изменяет клиента по его ID,
     * в соотвествии с переданным клиентом.
     * @param client - объект клиент, в соотвествии с которым нужно обновить данные
     * @param id - ID клиента которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(ClientDTO client, int id);

    /**
     * Удаляет клиента по его ID
     * @param id - ID клиента который нужно удалить
     * @return - true если клиент удалён, иначе false
     */
    boolean delete(int id);
}

package home.vs.app_java.dao;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import home.vs.app_java.dto.*;

public class FakeDB {

    public static class Client {
        // Хранилище клиентов.
        public static final Map<Integer, ClientDTO> MAP = new HashMap<>();
        // Переменная для генерации ID клиента.
        public static final AtomicInteger ID_HOLDER = new AtomicInteger();

    }
    
    public static class LayerGroup {
        // Хранилище групп слоёв.
        public static final Map<Integer, LayerGroupDTO> MAP = new HashMap<>();
        // Переменная для генерации ID группы.
        public static final AtomicInteger ID_HOLDER = new AtomicInteger();
    }

    public static class UserLayerGroup {
        // Хранилище соответствия ключей
        public static final Set<TwoFKeys> SET = new HashSet<>();
    }

    public static class Layer {
        // Хранилище слоёв.
        public static final Map<UUID, LayerDTO> MAP = new HashMap<>();
    }

    public static class GeoObject {
        // Хранилище геообъектов.
        public static final Map<Integer, GeoObjectDTO> MAP = new HashMap<>();
        // Переменная для генерации ID геообъектов.
        public static final AtomicInteger ID_HOLDER = new AtomicInteger();
    }

}

-- МОДУЛИ
-- подключаем модуль в котором криптографические функции и генерация uuid
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
-- подключаем модуль типа текст который не учитывает регистр и внутри себя делает lower(всегда маленький регистр)
CREATE EXTENSION IF NOT EXISTS "citext";

-- ПОЛЬЗОВАТЕЛЬСКИЕ ТИПЫ ДАННЫХ И ДОМЕНЫ
-- роль или права доступа(role_type)
CREATE TYPE role_type AS ENUM (
    'application',
    'super_administrator',
    'client_administrator',
    'read-write_client',
    'read-only_client',
    'driver'

);
-- тип гео обьектов(geo_obj_type)
CREATE TYPE geo_obj_type AS ENUM (
    'point',
    'line'
);
-- координата(coord)
CREATE TYPE coord AS (
    -- широта
    latitude double precision,
    -- долгота
    longitude double precision
);
-- домен поверх coord с проверкой на отсутствие NULL
CREATE DOMAIN domain_coord AS coord
CHECK (
    ((value).latitude IS NOT NULL) AND ((value).longitude IS NOT NULL)
);
-- домен поверх citext для почты
CREATE DOMAIN email_type AS citext
CHECK (
    value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'
);
-- домен поверх text для мобильного телефона
CREATE DOMAIN phone_type AS text
CHECK (
    value ~ '^[+]?[0-9]{10,13}$'
);
-- домен поверх citext для фамилии имени и отчества
CREATE DOMAIN name_type AS citext
CHECK (
    char_length(value) <= 50
);

-- СОЗДАЁМ ТАБЛИЦЫ
-- пользователь(client)
CREATE TABLE client (
    id serial PRIMARY KEY,
    surname name_type NOT NULL, -- фамилия
    name name_type NOT NULL,  -- имя
    patronymic name_type NOT NULL,  -- отчество
    password varchar(255) NOT NULL,
    email email_type UNIQUE NOT NULL,
    numberphone phone_type UNIQUE NOT NULL,
    role role_type NOT NULL,
    json_data jsonb
);
-- группы слоёв
CREATE TABLE layer_group (
    id serial PRIMARY KEY,
    name varchar(25) NOT NULL
);
-- ассоциативная таблица для создания многие ко многим между layer_group и client
CREATE TABLE user_layer_group (
    client_id integer REFERENCES client(id) ON DELETE RESTRICT,
    layer_group_id integer REFERENCES layer_group(id) ON DELETE CASCADE
);
-- слой
CREATE TABLE layer (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(), -- генерируем по умолчанию uuid
    layer_group_id integer REFERENCES layer_group(id) ON DELETE CASCADE,
    type geo_obj_type, --//TODO подумать как под другому переименовать эту переменную
    name varchar(25) NOT NULL,
    description text NOT NULL,
    json_data jsonb
);
-- гео объект
CREATE TABLE geo_object ( --//TODO возможно придётся создать отдельные таблицы для point и line
    id serial PRIMARY KEY,
    layer_id uuid REFERENCES layer(id) ON DELETE CASCADE,
    name varchar(25) NOT NULL,
    type geo_obj_type NOT NULL, --//TODO подумать как под другому переименовать эту переменную
    forward_arrow_direction boolean , --если null то это не стрелка, если true то направление вперед, если false то направление в обратную сторону
    coordinate domain_coord array NOT NULL, -- //TODO сделать проверку для point что можно было ложить только один элемент
    description text NOT NULL,
    json_data jsonb
);
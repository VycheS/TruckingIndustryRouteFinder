-- МОДУЛИ
-- подключаем модуль в котором криптографические функции и генерация uuid
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ПОЛЬЗОВАТЕЛЬСКИЕ ТИПЫ ДАННЫХ И ДОМЕНЫ
-- роль или права доступа(role_type)
CREATE TYPE role_type AS ENUM (
    'data_app',
    'client'
);
-- тип слоя(layer_type)
CREATE TYPE layer_type AS ENUM (
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
-- домен повверх coord
CREATE DOMAIN domain_coord AS coord CHECK (
    -- проверка на отсутствие NULL
    ((VALUE).latitude IS NOT NULL) AND ((VALUE).longitude IS NOT NULL)
);

-- СОЗДАЁМ ТАБЛИЦЫ
-- пользователь(client)
CREATE TABLE client ( -- //TODO подключить модуль citext для удобства, он не учитывает регистр и внутри себя делает lower
    id serial PRIMARY KEY,
    surname varchar(25) NOT NULL, -- фамилия
    name varchar(25) NOT NULL,  -- имя
    patronymic varchar(25) NOT NULL,  -- отчество
    password varchar NOT NULL,
    email varchar(50) NOT NULL, -- //TODO добавить маску(домен) для почты
    numberphone varchar(25) NOT NULL, -- //TODO добавить маску(домен) для номера телефона
    role role_type NOT NULL,
    addjson jsonb,
    -- проверка на уникальность
    UNIQUE (email, numberphone)
);
-- группы слоёв
CREATE TABLE layer_group (
    id serial PRIMARY KEY,
    name varchar(25) NOT NULL
);
-- ассоциативная таблица для создания многие ко многим между layer_group и client
CREATE TABLE user_layer_group (
    client_id integer REFERENCES client(id),
    layer_group_id integer REFERENCES layer_group(id)
);
-- слой
CREATE TABLE layer (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(), -- генерируем по умолчанию uuid
    layer_group_id integer REFERENCES layer_group(id),
    name varchar(25) NOT NULL,
    description text NOT NULL,
    addjson jsonb
);
-- гео объект
CREATE TABLE geo_object (
    id serial PRIMARY KEY,
    layer_id uuid REFERENCES layer(id),
    name varchar(25) NOT NULL,
    coordinate domain_coord array NOT NULL, -- //TODO сделать проверку для point что можно было ложить только один элемент
    description text NOT NULL,
    addjson jsonb
);
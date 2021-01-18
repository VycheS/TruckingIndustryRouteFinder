-- МОДУЛИ
-- подключаем модуль в котором криптографические функции и генерация uuid
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ПОЛЬЗОВАТЕЛЬСКИЕ ТИПЫ ДАННЫХ
-- координата(coord)
CREATE TYPE coord AS (
    -- широта
    latitude double precision,
    -- долгота
    longitude double precision
);
-- роль или права доступа(role_type)
CREATE TYPE role_type AS ENUM (
    'admin',
    'client',
    'driver'
);
-- тип слоя(layer_type)
CREATE TYPE layer_type AS ENUM (
    'point',
    'line'
);

-- СОЗДАЁМ ТАБЛИЦЫ
-- пользователь(client)
CREATE TABLE client (
    id serial PRIMARY KEY,
    surname varchar(25) NOT NULL, -- фамилия
    name varchar(25) NOT NULL,  -- имя
    patronymic varchar(25) NOT NULL,  -- отчество
    email varchar(50) NOT NULL,
    password varchar NOT NULL,
    numberphone varchar(25) NOT NULL,
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
    -- //TODO сделать проверку чтобы были заполнены оба поля
    coordinate coord array NOT NULL, -- //TODO сделать проверку для point что можно было ложить только один элемент
    description text NOT NULL,
    addjson jsonb
);
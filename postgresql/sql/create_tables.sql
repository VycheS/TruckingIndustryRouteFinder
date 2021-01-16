-- СОЗДАЁМ ТАБЛИЦЫ

-- пользователь(user)
CREATE TABLE user(
    id serial PRIMARY KEY,
    surname varchar(25) NOT NULL,
    name varchar(25) NOT NULL,
    patronymic varchar(25) NOT NULL,
    email varchar(50) NOT NULL,
    password varchar NOT NULL,
    numberphone varchar(25) NOT NULL,
    role varchar(25) NOT NULL,
    addjson jsonb
);

-- группы слоёв
CREATE TABLE layer_group(
    id serial PRIMARY KEY,
    name varchar(25) NOT NULL
);

-- ассоциативная таблица для создания многие ко многим между layer_group и user
CREATE TABLE user_layer_group(
    user_id integer REFERENCES user(id),
    layer_group_id integer REFERENCES layer_group(id)
);

-- подключаем модуль генерации uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- слой
CREATE TABLE layer(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    layer_group_id integer REFERENCES layer_group(id),
    name varchar(25) NOT NULL,
    description text NOT NULL,
    addjson jsonb
);

-- //TODO !!!!!!!!СДЕЛАТЬ чтобы наследовался только от layer либо point_layer либо line_layer но не одновременно
-- слой точка, наследованный от таблицы слой
CREATE TABLE point_layer(
    id integer REFERENCES layer(id) PRIMARY KEY
);

-- слой линия, наследованный от таблицы слой
CREATE TABLE line_layer(
    id integer REFERENCES layer(id) PRIMARY KEY
);

-- точка
CREATE TABLE point(
    id serial PRIMARY KEY,
    point_layer_id integer REFERENCES point_layer(id),
    name varchar(25) NOT NULL,
    description text NOT NULL,
    addjson jsonb
);

-- линия
CREATE TABLE line(
    id serial PRIMARY KEY,
    line_layer_id integer REFERENCES line_layer(id),
    name varchar(25) NOT NULL,
    description text NOT NULL,
    addjson jsonb
);

-- пользовательский тип координата(coord)
CREATE TYPE coord AS(
    -- //TODO сделать проверку чтобы были заполнены оба поля
    -- широта
    latitude double precision,
    -- долгота
    longitude double precision
);

-- сущность координата
CREATE TABLE coordinate(
    id serial PRIMARY KEY
    -- //TODO сделать возможность чтобы существовал только один внешний ключ либо point либо line
    point_id integer REFERENCES point(id),
    line_id intger REFERENCES line(id),
    coordinate coord[] NOT NULL -- //TODO сделать проверку для point что можно было ложить только один элемент
);
-- СОЗДАЁМ ТАБЛИЦЫ
-- пользователь(user)
CREATE TABLE client(
    id serial PRIMARY KEY,
    surname varchar(25),
    name varchar(25),
    patronymic varchar(25),
    email varchar(50),
    password varchar,
    numberphone varchar(25),
    role varchar(25),
    addjson jsonb
);

CREATE TABLE layergroup(
    id serial PRIMARY KEY,
    name varchar(25)
);

CREATE TABLE client_layergroup(
    client_id integer REFERENCES client(id),
    layergroup_id integer REFERENCES layergroup(id)
);
-- //TODO здесь доделать создание базы

-- ЗДЕСЬ ДЛЯ ПРИМЕРА В ПОСЛЕДУЮЩЕМ БУДЕТ УДАЛЕНО
-- покупатели
CREATE TABLE customer(
    id serial PRIMARY KEY,
    name varchar(255),
    phone varchar(30),
    email varchar(255)
);

-- продукты
CREATE TABLE product(
    id serial PRIMARY KEY,
    name varchar(255),
    description text,
    price integer
);

-- фото продуктов
CREATE TABLE product_photo(
    id serial PRIMARY KEY,
    url varchar(255),
    product_id integer REFERENCES product(id)
);

-- корзина
CREATE TABLE cart(
    customer_id integer REFERENCES customer(id),
    id serial PRIMARY KEY
);

-- связь корзина-продукты
CREATE TABLE cart_product(
    cart_id integer REFERENCES cart(id),
    product_id integer REFERENCES product(id)
);
#!/bin/bash

# копируем данные базы из контейнера СУБД,
# находясь в корне проекта запускаем с консоли ./scripts/copy_FROM_container.sh

docker cp postgres_container:/var/lib/postgresql/data/pgdata/. ./postgresql/pgdata
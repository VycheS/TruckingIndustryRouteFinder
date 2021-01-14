#!/bin/bash

# копируем данные базы в контейнер СУБД,
# находясь в корне проекта запускаем с консоли ./scripts/copy_INTO_container.sh

docker cp ./postgresql/pgdata/. postgres_container:/var/lib/postgresql/data/pgdata
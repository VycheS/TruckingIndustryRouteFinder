#!/bin/bash

# подключаемся к контейнеру в СУБД через psql

docker exec -it postgres_container psql -U postgres
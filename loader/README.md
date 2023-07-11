# README

## Deploy local MariaDB
```bash
docker run \
  -p 127.0.0.1:3306:3306 \
  --rm \
  --name mariadb \
  -e MARIADB_ROOT_PASSWORD="$MARIADB_ROOT_PASSWORD" \
  -d \
  mariadb:latest
```

## Access container
```bash
docker exec -it mariadb /bin/sh

mysql -u root -p 666

create schema litcode collate utf8mb4_general_ci;

use litcode;

<Create tables>
```
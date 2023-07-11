# Litcode-DEMO

** This is a demo of Litcode© of 2021. All copy rights are reserved**

- [CI/CD Pipeline](https://github.com/bamboovir/litcode/actions/workflows/litcode.yml)
- [Docker Image](https://hub.docker.com/repository/docker/bamboovir/litcode)

## Development Environment configuration

*Warning*: Any unexpected environment configuration may cause undefined errors.
Please configure strictly according to the requirements given.

### JDK

Install [sdkman](https://sdkman.io/install)

sdkman is recommended to install,
otherwise you need to ensure that the version of the installed development software package
is consistent with the following document.

Install JDK

```bash
sdk install java 11.0.11.j9-adpt
sdk default java 11.0.11.j9-adpt
```

### Host

add into `/etc/hosts` for oauth workflow test

```txt
127.0.0.1 www.litcode.com
```

### Credentials Setup

```bash
export LITCODE_MYSQL_USERNAME=""
export LITCODE_MYSQL_PASSWORD=""
export LITCODE_OAUTH2_CLIENT_ID=""
export LITCODE_OAUTH2_CLIENT_SECRET=""
```

## Commonly used quick commands:

### Building

To launch your tests:

```bash
./gradlew clean test
```

### To package application:

```bash
./gradlew clean assemble
```

### To run application:

```bash
./gradlew clean run
```

### To build Application Docker Image

```bash
bash ./scripts/build_litcode_image.bash
```

Start Docker Container

```bash
docker run \
  -p 0.0.0.0:80:80 \
  --rm -it \
  --name litcode \
  -v "$(pwd)/config.json:/usr/app/config.json" \
  -e LITCODE_OAUTH2_CLIENT_ID \
  -e LITCODE_OAUTH2_CLIENT_SECRET \
  -e LITCODE_MYSQL_USERNAME \
  -e LITCODE_MYSQL_PASSWORD \
  "docker.io/bamboovir/litcode:44aa077f"
```

## API Endpoint

### "/health"

GET

health check.

- Check the connection between the service and the database when triggered.

### "/api/question"

GET

Get Question records in pagination mode

- OAuth2 authentication required

input

- start: int QueryParam
- step: int QueryParam

### "/api/user/profile"

GET

Get the profile of the currently logged in user

- OAuth2 authentication required


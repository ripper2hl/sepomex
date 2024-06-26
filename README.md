# sepomex-api

[![codecov](https://codecov.io/gh/ripper2hl/sepomex/branch/master/graph/badge.svg?token=JPL2I5IUI0)](https://codecov.io/gh/ripper2hl/sepomex)
[![](https://img.shields.io/docker/stars/jesusperales/sepomex-api.svg)](https://hub.docker.com/r/jesusperales/sepomex-api/ 'Docker hub')
[![](https://img.shields.io/docker/pulls/jesusperales/sepomex-api.svg)](https://hub.docker.com/r/jesusperales/sepomex-api/ 'Docker hub')

API REST de SEPOMEX creada con Java 
utilizando la base de datos [sepomex-db-postgresql](https://github.com/ripper2hl/sepomex-db-postgresql).

Esta imagen corre sobre el puerto 8007 y puede utilizar
el archivo `docker-compose.yml` que se encuentra en este
repositorio para correr el proyecto.

`docker-compose up -d`

Documentación del API

* http://localhost:8080/swagger-ui/index.html

# Correr en tu ambiente local.

 * Requerimientos: 
    * java 8
    * maven
    * docker
    * docker-compose
    
 * Ejecutar los siguientes comandos.
```bash
git clone https://github.com/ripper2hl/sepomex.git

cd sepomex 

git checkout -b dev origin/dev

docker-compose pull db

docker-compose up -d db

export spring_profiles_active=local

mvn spring-boot:run
```

* [Abrir el link de la documentación de swagger](http://localhost:8080/swagger-ui/index.html#)



[![asciicast](https://asciinema.org/a/d8uBsGBw3S2kET07cDf68i8qG.svg)](https://asciinema.org/a/d8uBsGBw3S2kET07cDf68i8qG)

## GCP

### Variables de entorno

* DB_USER
* sepomex

* DB_PASS
* sepomex

* DB_NAME
* postgres

* DB_INSTANCE_CONNECTION_NAME
* clever-hangar-286504:us-central1:sepomex

* SPRING_PROFILES_ACTIVE
* gcp

## Generación imagen GCP

* Ejecutar los siguientes comandos.

```bash
git clone https://github.com/ripper2hl/sepomex.git

cd sepomex 

git checkout -b dev origin/dev

docker-compose pull db

docker-compose up -d db

export spring_profiles_active=local

mvn spring-boot:run
```

* Abrir swagger y ejecutar el endpoint de reindexado

* Ejecutar los siguientes comandos

```bash
docker build -f Dockerfile.gcp . -t gcr.io/clever-hangar-286504/sepomex-api

docker push gcr.io/clever-hangar-286504/sepomex-api
```

* Deployar en el UI de GCP.


### Curl para actualizar

curl -X 'POST'   'http://localhost:8080/v1/archivo/actualizacion'   -H 'accept: application/json;charset=UTF-8'   -H 'Content-Type: multipart/form-data;charset=UTF-8' -F 'file=@src/test/resources/sepomex.txt'

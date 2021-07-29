# Project description

Simple backend application that allows to read Github user's information. It also stores the request statistics and
performs some additional calculations.

# Running the project locally

## Dockerized environment (recommended)

In order to run the whole environment locally simply execute
`./run_application_locally.sh`. It would build a docker image of the service and run it along with PostgreSQL container.

The application will be available with a Swagger API documentation on http://localhost:8080/swagger-ui/index.html

## Running from Gradle or IntelliJ

### Application

In order to run the application locally, a *local* profile should configured. It' used automatically when running
the `bootRun` gradle task.

### Database

Running application this way requires additional dependency of PostgreSQL database.

A docker container with Postgres database can be run with:

`docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres`

The next step is to create an appropriate database:

```
# connect to the container shell
docker exec -it postgres sh

# connect to the postgres
psql postgres postgres

# create a database
CREATE DATABASE github;
```

The database schema will be updated after running the application with `./gradlew bootrun`

### IDE configuration

Additionally, annotation processing should be enabled in the IntelliJ IDEA to correctly interpret the Lombok
annotations.

It can be done by checking *Enable annotation processing* in project settings (ctrl + shitf + a -> "Enable annotation
processing").

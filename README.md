Decisions: 
1. I chose to be strict with data import -> if there is a single malformed row in the CSV the whole file is rejected
1. Clients with zero appointments/points are not returned in by the `get most loyal client` logic
1. I am using appointment `end time` in the `get most loyal client` logic
1. Service and purchase is mapped to the same entity class

TODO: NOW!! 
1. Native tests are not working

Partially autogenerated from maven template: 
# salon-quarkus project

This project uses:
1. Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.
1. GraalVM to build a native image
1. Flyway for Database schema management. Migrations are automatically applied to DB on the application startup. It might not be a good idea for the production environment.
1. Docker for both JVM and native image
1. PostgreSQL in Docker for local development and tests
1. Open API - see: /openapi and /swagger-ui
1. Tests environment: 
1.1 Testcontainers to manage PostgreSQL in Docker 
1.2 Flyway to clean the database before each test


# Quick comparison between JVM and Native:  
Startup time: 
- JVM       ~ 2.7 s 
- Native    ~ 0.069 s

Memory footprint: 
- JVM ~ 270 MB
- Native ~ 30.2 MB

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

You can start PostgreSQL in docker using: 
```
./db-in-docker.sh
```

## Running prebuilt (for mac os) native image: 
./start-native.sh

## Sample curls
list the top clients
```
curl "localhost:8080/client/findMostLoyal?from=1999-12-01&limit=10"
```

consume and parse clients.csv
```
curl --location --request POST 'http://localhost:8080/admin/import/client' \
--header 'Content-Type: multipart/form-data' \
--form 'type=client' \
--form 'file=@src/test/resources/clients.csv'
```
create client
```
curl --location --request PUT 'localhost:8080/client/EF51943B-0F6F-4FFF-85BD-E6BFE0935102' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "some@email.net",
    "firstName": "Pawel",
    "gender": "Male",
    "lastName": "Kunde",
    "phone": "1234",
    "status": "ACTIVE"
}'
```
get client by id
```
curl localhost:8080/client/EF51943B-0F6F-4FFF-85BD-E6BFE0935102
```
## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `salon-quarkus-1.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/salon-quarkus-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable
GraalVM has to be installed in order to build native image locally. 
Example installation with [sdkman](https://sdkman.io/)
```
sdk install java 19.2.1-grl
```
You can create a native executable using: `./mvnw package -Pnative`.

You can then execute your binary: `./target/salon-quarkus-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .

# TODO:
1. Proper Price/Money handling. BigDecimal is probably ok, but I am a bit outdated in this subject
1. Quarkus automatically includes the Agroal connection pool;
   Are defaults ok? (I used HikariCP in the past. No idea if Agroal is the way to go.)
1. Authentication - probably JWT 
1. ORM improvement - Panache looks interesting at first glance. Does it work well with Kotlin?
1. Support for Idempotence see: https://stripe.com/docs/api/idempotent_requests
1. Move to Java 12
1. `ArcUndeclaredThrowableException` is wrapping all checked exceptions?
1. Error message consistency is not great
1. Optimistic locking:
 
   - REST (see https://opensource.zalando.com/restful-api-guidelines/#optimistic-locking) 
   - PostgreSQL 
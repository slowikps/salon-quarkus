TODO: 
1. How quarkus manage PostgreSQL connection pool? "Quarkus automatically includes the Agroal connection pool;"
    I used HikariCP in the past. No idea if Agroal is the way to go. 
1. Flyway usage
1. Authentication - JWT
1. live-reload interesting feature
1. I chose to be strict with data import -> if there is a single malformed row in the csv the whole file is rejected
1. ORM improvement - Panache looks interesting at the first glance. Does it work well with Kotlin?
1. Move to Java 12
1. Data validation - only very basic is implemented. Problem is not properly populated on native!
1. OpenAPI 
1. Proper Price handling
1. Optimistic locking: 
1.1 REST (see https://opensource.zalando.com/restful-api-guidelines/#optimistic-locking) 
1.1 PostgreSQL 
1. ClientRepositoryTest doesn't work in Intellij
1.1 ArcUndeclaredThrowableException how to avoid this?
1.1 Assumption - clients with no appointments are not returned

Mostly autogenerated from maven template: 
# salon-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

You can start PostgreSQL in docker using: 
```
./db-in-docker.sh
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

# Quick comparision JVM vs Native: 
Startup time: 
- JVM       ~ 2.7 s 
- Native    ~ 0.069 s

Memory footprint: 
- JVM ~ 270 MB
- Native ~ 30.2 MB
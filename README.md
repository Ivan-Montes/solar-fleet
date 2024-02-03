# :sunny::rocket: solar-fleet :sunny::rocket:

An API implemented using Quarkus, Maven, Java, and MongoDB, mainly. You could list spaceships, classes, positions and the crew in charge in an amazing empty DB.....yeah, it's empty :sweat:


## Table of contents

- [Installation](#installation)
- [Usage](#usage)
- [It's not a bug, it's a feature](#features)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)


## Installation

### Creating a database

To create a empty local NOSQL database, I used MongoDb. The default name is "solarfleetdb" for production, "solarfleetdbdev" for developing and "solarfleetdbtest" for testing. You can find all of this in application.properties

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/solar-fleet-1.0.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.


## Usage

The best way is to consult SwaggerUI by browsing to `http://localhost:8080/q/swagger-ui/`


### Related Guides

- MongoDB with Panache ([guide](https://quarkus.io/guides/mongodb-panache)): Simplify your persistence code for MongoDB via the active record or the repository pattern
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI


## Features

#### :sun_with_face: Unitary Testing with JUnit 5 and Mockito 

#### :sun_with_face: Dependency injection and Clean Code respected

#### :sun_with_face: Used Sonarqube community for checking the project

#### :sun_with_face: Hibernate validator included

#### :sun_with_face: Integrity check in MongoDB before any CRUD operation

#### :sun_with_face: OpenAPI Documentation with SwaggerUI


## Maintainers

Just me, [Iván](https://github.com/Ivan-Montes) :sweat_smile:


## Contributing

Contributions are always welcome! 


## License

[![Java](https://badgen.net/static/JavaSE/17/orange)](https://www.java.com/es/)
[![Maven](https://badgen.net/badge/icon/maven?icon=maven&label&color=red)](https://https://maven.apache.org/)
[![Quarkus](https://badgen.net/static/Quarkus/3.6/black)](https://code.quarkus.io/)
[![GitHub](https://badgen.net/badge/icon/github?icon=github&label)](https://github.com)
[![Eclipse](https://badgen.net/badge/icon/eclipse?icon=eclipse&label)](https://https://eclipse.org/)
[![MongoDB](https://badgen.net/static/MongoDB/Community/green)](https://www.mongodb.com)
[![SonarQube](https://badgen.net/badge/icon/sonarqube?icon=sonarqube&label&color=purple)](https://www.sonarsource.com/products/sonarqube/downloads/)
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://choosealicense.com/licenses/gpl-3.0/)
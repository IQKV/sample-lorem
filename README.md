## 🚀 Words Processor

Lorem ipsum text processor based on the **[loripsum.net](https://loripsum.net/)** API.

Spring Boot RESTful microservices example (including Swagger UI), which allows to fetch, process dummy text,
and generate statistic reports.

### Technology stack

java 21, Maven, Spring Boot, bitnami/kafka:3.9.0, postgres:16.6.

_Including utils:_ liquibase, WireMock, Kafka & Postgres testcontainers, Kafka healthcheck feature, docker-compose._dev_.yml,
_checkstyle_ configuration, SpotBugs, PMD etc.

### Applications

**TLDR:** All-in-one docker compose for demo propose:

`docker compose -f docker-compose.yml up`

| App Name             | Description                                       | REST Endpoint (with default port settings) |
| -------------------- | ------------------------------------------------- | ------------------------------------------ |
| **words-processing** | Handle http rq, process rs text & generate report | http://localhost:8085/api/v1/text          |
| **reports-history**  | Provide pageable processing reports list          | http://localhost:8086/api/v1/history       |

### Environment variables

Applications are highly-configurable, supports many env vars, such as:

| ENV Variable                           | Description                                         | Default Value                             |
| -------------------------------------- | --------------------------------------------------- | ----------------------------------------- |
| SERVER_PORT                            | Application port                                    | 8085, 8086                                |
| KAFKA_BOOTSTRAP_SERVERS                | Kafka Broker address                                | localhost:9092                            |
| KAFKA_SECURITY_PROTOCOL                |                                                     | PLAINTEXT                                 |
| KAFKA_TOPIC_WORDS_PROCESSED            | Topic name                                          | words.processed                           |
| KAFKA_TOPIC_PARTITIONS_WORDS_PROCESSED | Topic partitions                                    | 4                                         |
| KAFKA_CONSUMER_THREADS                 | Consumer threads count,<br>respective to partitions | 4                                         |
| KAFKA_CONSUMERS_GROUP                  | Consumer group name                                 | reports-history                           |
| KAFKA_ADMIN_CREATES_TOPICS             | Enables Kafka Admin for topic creation              | true                                      |
| DATASOURCE_URL                         |                                                     | jdbc:postgresql://localhost:5432/lorem_db |
| DATASOURCE_USERNAME                    |                                                     | postgres                                  |
| DATASOURCE_PASSWORD                    |                                                     | postgres                                  |
| DATASOURCE_DRIVER                      |                                                     | org.postgresql.Driver                     |

## Code conventions

The code follows [Google Code Conventions](https://google.github.io/styleguide/javaguide.html). Code
quality is measured by:

- Sonarqube
- [PMD](https://pmd.github.io/)
- [CheckStyle](https://checkstyle.sourceforge.io/)
- [SpotBugs](https://spotbugs.github.io/)

### Tests

This project has standard JUnit tests. To run them execute this command:

```
./mvnw verify -P use-testcontainers
```

It is mandatory to keep test code coverage not below **80** percents and cover all business logic and edge cases.

### Pre-Requisites to run this example locally

- Setup git command line tool (https://help.github.com/articles/set-up-git)
- Clone source code to the local machine:

```
git clone https://github.com/IQKV/sample-mix-lorem.git

cd sample-mix-lorem
```

- Install Docker [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)
- Add new version of Docker Compose [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
- Spin-up single instance of Kafka broker, ZooKeeper and Postgresql by running command:

```
docker compose -f compose.yaml up -d
```

### Running locally

This application is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built
using [Maven](https://spring.io/guides/gs/maven/). You can build a jar files and run it from the command line:

- Create jar packages:

```
./mvnw package
```

- Run **words-processing** app:

```
java -jar words-processing/target/*.jar
```

Now you can access to the Swagger UI here: http://localhost:8085/swagger-ui.html

- Run **reports-history** app:

```
java -jar reports-history/target/*.jar
```

Swagger UI is here: http://localhost:8086/swagger-ui.html

##### After all, don't forget to clean up working directory & stop dev services:

```
./mvnw clean
```

```
docker compose -f compose.yaml down
```

## Versioning

Project uses a three-segment [CalVer](https://calver.org/) scheme, with a short year in the major version slot, short month in the minor version slot, and micro/patch version in the third
and final slot.

```
YY.MM.MICRO
```

1. **YY** - short year - 6, 16, 106
1. **MM** - short month - 1, 2 ... 11, 12
1. **MICRO** - "patch" segment

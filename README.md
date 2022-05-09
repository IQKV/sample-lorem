## Words Processor

Lorem ipsum text processor based on the  **[loripsum.net](https://loripsum.net/)** API.

Spring Boot RESTful microservices example (including Swagger UI), which allows to fetch, process dummy text,
and generate statistic reports.

### Technology stack

Java 17, Maven, Spring Boot, Confluent Kafka cp-kafka:6.2.0, postgres:13.5.

_Including utils:_ liquibase, WireMock, Kafka & Postgres testcontainers, docker-compose._dev_.yml,
custom _checkstyle_ configuration, etc.

### Applications

| App Name             | Description                                       | REST Endpont                            |
|----------------------|---------------------------------------------------|-----------------------------------------|
| __words-processing__ | Handle http rq, process rs text & generate report | http://localhost:8081/betvictor/text    |
| __reports-history__  | Provide pageable processing reports list          | http://localhost:8082/betvictor/history |

### Environment variables

Applications are highly-configurable, supports many env vars, such as: 

| ENV Variable                           | Description                            | Default Value   |
|----------------------------------------|----------------------------------------|-----------------|
| SERVER_PORT                            | Application port                       | 8081, 8082      |
| KAFKA_BOOTSTRAP_SERVERS                | Kafka Broker address                   | localhost:29092 |
| KAFKA_SECURITY_PROTOCOL                |                                        | PLAINTEXT       |
| KAFKA_TOPIC_WORDS_PROCESSED            | Topic name                             |words.processed|
| KAFKA_TOPIC_PARTITIONS_WORDS_PROCESSED | Topic partitions                       |4|
| KAFKA_CONSUMER_THREADS                 | Consumer threads count                 | 4               |
| KAFKA_CONSUMERS_GROUP                  | Consumer group name                    | reports-history |
| KAFKA_CREATE_TOPICS_ON_STARTUP         | Enables Kafka Admin for topic creation | true            |
| DATASOURCE_URL                         |                                        |jdbc:postgresql://localhost:5432/lorem_ipsum_db|
| DATASOURCE_USERNAME                    |                                        |postgres|
| DATASOURCE_PASSWORD                    |                                        |postgres|
| DATASOURCE_DRIVER                      |                                        |org.postgresql.Driver|

### Tests

This project has standard JUnit tests. To run them execute this command:

```
mvn test
```

### Pre-Requisites to run this example locally

- Setup git command line tool (https://help.github.com/articles/set-up-git)
- Clone source code to the local machine:

```
git clone https://github.com/ujar-org/lorem-ipsum-words-processor.git

cd lorem-ipsum-words-processor
```

- Install Docker [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/) - at least 1.6.0
- Add new version of docker-compose [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
- Spin-up single instance of Kafka broker, zookeeper and Postgresql by running command:

```text
docker-compose -f docker-compose.dev.yml up -d
```

### Running locally

This application is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built
using [Maven](https://spring.io/guides/gs/maven/). You can build a jar files and run it from the command line:

- Create jar packages:

```
mvn package
```

- Run **words-processing** app:

```
java -jar words-processing/target/*.jar
```

You can then access Swagger UI here: http://localhost:8081/swagger-ui.html

- Run **reports-history** app:

```
java -jar reports-history/target/*.jar
```

Swagger UI is here: http://localhost:8082/swagger-ui.html


###### Also, don't forget to stop dev services:

```text
docker-compose -f docker-compose.dev.yml down
```

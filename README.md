## loripsum.net - Words Processor

Lorem ipsum text processor based on the loripsum.net API.

Spring Boot RESTful microservices example (including Swagger UI), which allows to fetch, process dummy text,
and generate statistic reports.

### Technology stack

Java 17, Maven, Spring Boot, Confluent Kafka cp-kafka:6.2.0, postgres:13.5

### Tests

This project has standard JUnit tests. To run them execute this command:

```
mvn test
```

### Pre-Requisites to run this example locally
- Setup git command line tool (https://help.github.com/articles/set-up-git)
- Clone source code to the local machine:
 
```
git clone https://github.com/ujar-org/lorem-ipsum-words-statistic.git

cd lorem-ipsum-words-statistic
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

You can then access Swagger UI here: http://localhost:8080/swagger-ui.html

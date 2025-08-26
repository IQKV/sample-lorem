## 🚀 Words Processor

[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Build](https://img.shields.io/badge/Build-Maven-informational?logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)

Lorem ipsum text processor based on the **[loripsum.net](https://loripsum.net/)** API.

A Spring Boot microservices example showing how to fetch and process dummy text, publish results to Kafka, and expose a pageable reports history via REST. Includes Swagger UI, health checks, metrics, and a ready-to-run demo stack.

### 💡 Tech stack

- **Runtime**: Java 21, Spring Boot
- **Messaging/DB**: Kafka (Confluent 7.8.3), PostgreSQL 16.6, Liquibase
- **Build/Tooling**: Maven, Checkstyle, PMD, SpotBugs, Testcontainers, WireMock
- **Ops**: Docker Compose (dev and demo), Prometheus, Grafana, SonarQube

### 🧩 Modules

| Module             | Description                                                                   | Default port | Main endpoint                                               |
| ------------------ | ----------------------------------------------------------------------------- | ------------ | ----------------------------------------------------------- |
| `words-processing` | Calls loripsum.net, analyzes text, publishes a report to Kafka and returns it | 8085         | `GET /api/v1/text?p={1..10}&l={short,medium,long,verylong}` |
| `reports-history`  | Consumes reports from Kafka and stores in Postgres; exposes pageable history  | 8086         | `GET /api/v1/history?page=0&size=20&sort=id,desc`           |

Swagger UI: `http://localhost:8085/swagger-ui.html` and `http://localhost:8086/swagger-ui.html`.

---

## ⚡ Quick start (demo stack)

Run the pre-built images with all dependencies (Kafka, Postgres) in one go:

```shell script
docker compose -f docker-compose.yml up
```

Once started:

- Words Processing API: `http://localhost:8085/api/v1/text`
- Reports History API: `http://localhost:8086/api/v1/history`

Example calls:

```shell script
curl "http://localhost:8085/api/v1/text?p=2&l=short"

curl "http://localhost:8086/api/v1/history?page=0&size=10&sort=id,desc"
```

Stop the demo stack:

```shell script
docker compose -f docker-compose.yml down
```

---

## 🛠 Local development

### Prerequisites

- Git, JDK 21, Docker, Docker Compose

Clone the repo:

```shell script
git clone https://github.com/IQKV/sample-lorem.git
cd sample-lorem
```

### Bring up dev infrastructure (Kafka, ZK, Postgres, optional observability)

```shell script
docker compose -f compose.yaml up -d
```

This exposes Postgres on `localhost:5432` and Kafka on `localhost:9092`. Optional services: Prometheus, Grafana, SonarQube (see `compose.yaml`).

### Build

```shell script
./mvnw -q -DskipTests package
```

### Run services locally

- Words Processing:

```shell script
java -jar words-processing/target/*.jar
```

Open Swagger UI: `http://localhost:8085/swagger-ui.html`

- Reports History:

```shell script
java -jar reports-history/target/*.jar
```

Open Swagger UI: `http://localhost:8086/swagger-ui.html`

### Validate APIs

```shell script
curl "http://localhost:8085/api/v1/text?p=3&l=medium"
curl "http://localhost:8086/api/v1/history?page=0&size=5&sort=id,desc"
```

---

## 📦 Maven multi‑module structure

This repository is a Maven aggregator project (`packaging=pom`) with three modules:

```text
sample-lorem
├─ shared            (artifactId: sample-lorem-shared)
├─ words-processing  (artifactId: sample-lorem-words-processing)
└─ reports-history   (artifactId: sample-lorem-reports-history)
```

Root coordinates: `com.iqkv:sample-lorem:25.0.0-SNAPSHOT`

Dependency graph:

```text
sample-lorem (aggregator)
├─► shared
│
├─► words-processing ──┐
│        ▲             │
│        │             │ depends on
│        └─────────────┘
│
├─► reports-history ───┐
         ▲             │
         │             │ depends on
         └─────────────┘
```

### Common commands (from repo root)

- Build everything:

```shell script
./mvnw -q clean install
```

- Build and run only `words-processing`, building its deps (`-am`):

```shell script
./mvnw -q -pl words-processing -am clean package
java -jar words-processing/target/*.jar
```

- Build and run only `reports-history`:

```shell script
./mvnw -q -pl reports-history -am clean package
java -jar reports-history/target/*.jar
```

- Run tests with Testcontainers profile for all modules:

```shell script
./mvnw verify -P use-testcontainers
```

- Limit actions to a module (e.g., unit tests only for `shared`):

```shell script
./mvnw -q -pl shared test
```

### Run with Spring Boot plugin

- Start `words-processing` (build dependencies and run):

```shell script
./mvnw -q -pl words-processing -am spring-boot:run
```

- Start `reports-history`:

```shell script
./mvnw -q -pl reports-history -am spring-boot:run
```

- Optionally enable the `dev` profile while running:

```shell script
./mvnw -q -pl words-processing -am spring-boot:run -Dspring-boot.run.profiles=dev
./mvnw -q -pl reports-history -am spring-boot:run -Dspring-boot.run.profiles=dev
```

### Useful profiles

- `dev`: adds Spring Boot DevTools to the service modules
- `use-testcontainers`: enables embedded Kafka/Postgres testing
- `use-qulice`: enables additional static analysis checks

## ⚙️ Configuration

Applications support environment variables (see `words-processing/src/main/resources/application.yml` and `reports-history/src/main/resources/application.yml`). Common ones:

| Variable                                 | Description                   | Default                                     |
| ---------------------------------------- | ----------------------------- | ------------------------------------------- |
| `SERVER_PORT`                            | HTTP port                     | `8085` (words), `8086` (history)            |
| `KAFKA_BOOTSTRAP_SERVERS`                | Kafka broker list             | `localhost:9092`                            |
| `KAFKA_SECURITY_PROTOCOL`                | Security protocol             | `PLAINTEXT`                                 |
| `KAFKA_TOPIC_WORDS_PROCESSED`            | Topic for processed reports   | `words.processed`                           |
| `KAFKA_TOPIC_PARTITIONS_WORDS_PROCESSED` | Topic partitions              | `4`                                         |
| `KAFKA_CONSUMER_THREADS`                 | Consumer threads (history)    | `4`                                         |
| `KAFKA_CONSUMERS_GROUP`                  | Consumer group (history)      | `reports-history`                           |
| `KAFKA_ADMIN_CREATES_TOPICS`             | Auto-create topics on startup | `true`                                      |
| `DATASOURCE_URL`                         | JDBC URL (history)            | `jdbc:postgresql://localhost:5432/lorem_db` |
| `DATASOURCE_USERNAME`                    | DB username (history)         | `postgres`                                  |
| `DATASOURCE_PASSWORD`                    | DB password (history)         | `postgres`                                  |

---

## 📈 Observability

- Prometheus scrapes `:8080/actuator/prometheus` for each service
- Grafana dashboards are provisioned under `src/main/docker/grafana/provisioning/`

Start them via `compose.yaml` (already included when you run `docker compose -f compose.yaml up -d`).

---

## 🧪 Testing

JUnit 5, Hamcrest, Mockito, Testcontainers, WireMock.

```shell script
./mvnw verify -P use-testcontainers
```

The minimum required code coverage is **80%**.

---

## 📚 Code quality

The code follows the [Google Java Style](https://google.github.io/styleguide/javaguide.html) and is checked by Checkstyle, PMD, and SpotBugs. Optional SonarQube is available in `compose.yaml`.

---

## 🧹 Cleanup

```shell script
./mvnw clean
docker compose -f compose.yaml down
```

---

## 📄 License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).

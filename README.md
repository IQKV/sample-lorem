## 🚀 Words Processor

[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-informational?logo=apachemaven)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Code Coverage](https://img.shields.io/badge/Coverage-80%25-green)]()

Lorem ipsum text processor based on the **[loripsum.net](https://loripsum.net/)** API.

A Spring Boot microservices example showing how to fetch and process dummy text, publish results to Kafka, and expose a pageable reports history via REST. Includes Swagger UI, health checks, metrics, and a ready-to-run demo stack.

### 💡 Tech Stack

#### Core Technologies

- **Runtime**: Java 21 (with Java 21 features), Spring Boot 4.0, Spring Framework 7.x
- **Messaging**: Apache Kafka (Confluent 7.8.3) with Spring Kafka
- **Database**: PostgreSQL 16.6 with Liquibase migrations
- **Caching**: EhCache with Spring Cache abstraction

#### Development & Quality

- **Build Tool**: Maven 3.x with multi-module structure
- **Code Quality**: Checkstyle, PMD, SpotBugs, Qulice
- **Testing**: JUnit 5, Testcontainers, WireMock, Spring Boot Test
- **Code Coverage**: JaCoCo (minimum 80% coverage)

#### Observability & Operations

- **Monitoring**: Micrometer with Prometheus metrics
- **Logging**: Logback with structured JSON logging (Logstash encoder)
- **Tracing**: Micrometer Tracing with Brave
- **Dashboards**: Grafana with pre-configured dashboards
- **Code Analysis**: SonarQube integration
- **Containerization**: Docker & Docker Compose

### 🧩 Modules

| Module             | Description                                                                   | Default port | Main endpoint                                               |
| ------------------ | ----------------------------------------------------------------------------- | ------------ | ----------------------------------------------------------- |
| `words-processing` | Calls loripsum.net, analyzes text, publishes a report to Kafka and returns it | 8085         | `GET /api/v1/text?p={1..10}&l={short,medium,long,verylong}` |
| `reports-history`  | Consumes reports from Kafka and stores in Postgres; exposes pageable history  | 8086         | `GET /api/v1/history?page=0&size=20&sort=id,desc`           |

### 📚 API Documentation

#### Interactive API Documentation

- **Words Processing API**: [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)
- **Reports History API**: [http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html)

#### OpenAPI Specifications

- **Words Processing**: [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)
- **Reports History**: [http://localhost:8086/v3/api-docs](http://localhost:8086/v3/api-docs)

#### Additional Documentation

- **Development Guide**: [development.md](development.md) - Detailed development setup and guidelines
- **API Reference**: [api-reference.md](api-reference.md) - Complete API reference documentation

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

- **Git**: Version control
- **JDK 21+**: OpenJDK or Oracle JDK (Java 21 features supported)
- **Docker & Docker Compose**: For infrastructure services
- **Maven 3.8+**: Build tool (wrapper included)
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse with Spring Boot support

### Modern Java Features Used

- **Records**: Immutable DTOs and value objects
- **Pattern Matching**: Enhanced switch expressions
- **Text Blocks**: Improved string handling
- **Sealed Classes**: Restricted inheritance hierarchies
- **Virtual Threads**: Project Loom integration (when available)

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

## 📦 Maven Multi-Module Architecture

This repository follows a Maven aggregator pattern (`packaging=pom`) with a clean separation of concerns:

```text
sample-lorem (com.iqkv:sample-lorem:0.25.0-SNAPSHOT)
├─ shared/                    # Common utilities, DTOs, Kafka configuration
│  └─ sample-lorem-shared     # Shared across all modules
├─ words-processing/          # Text processing microservice
│  └─ sample-lorem-words-processing  # Port 8085
└─ reports-history/           # Report storage microservice
   └─ sample-lorem-reports-history   # Port 8086
```

### Module Dependencies & Responsibilities

```text
┌─────────────────┐    ┌──────────────────┐
│ words-processing│    │ reports-history  │
│                 │    │                  │
│ • REST API      │    │ • REST API       │
│ • Text analysis │    │ • Kafka consumer │
│ • Kafka producer│    │ • PostgreSQL     │
│ • External API  │    │ • JPA/Hibernate  │
└─────────┬───────┘    └─────────┬────────┘
          │                      │
          └──────┬─────────────┬──┘
                 │             │
         ┌───────▼─────────────▼───┐
         │       shared            │
         │                         │
         │ • Common DTOs           │
         │ • Kafka configuration   │
         │ • Validation utilities  │
         │ • OpenAPI documentation │
         │ • Actuator endpoints    │
         └─────────────────────────┘
```

### Key Architectural Decisions

- **Shared Module**: Contains common utilities, DTOs, and configurations to avoid duplication
- **Service Independence**: Each service can be built, tested, and deployed independently
- **Event-Driven Communication**: Services communicate via Kafka for loose coupling
- **Database per Service**: Each service owns its data (reports-history uses PostgreSQL)
- **Observability**: All modules include Micrometer metrics and structured logging

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

## � Seceurity

### Security Features

- **Input Validation**: Bean Validation (JSR-303) with custom validators
- **CORS Configuration**: Configurable cross-origin resource sharing
- **Actuator Security**: Production-ready endpoint security
- **Dependency Scanning**: OWASP dependency check integration

### Security Best Practices

```bash
# Check for security vulnerabilities
./mvnw org.owasp:dependency-check-maven:check

# Security headers validation
curl -I http://localhost:8085/api/v1/text
```

### Environment-Specific Security

- **Development**: Relaxed CORS, debug endpoints enabled
- **Production**: Strict CORS, minimal actuator endpoints, HTTPS only

## 📈 Observability & Monitoring

### Metrics Collection

- **Application Metrics**: Custom business metrics via Micrometer
- **JVM Metrics**: Memory, GC, thread pools automatically exposed
- **Database Metrics**: Connection pool, query performance via Hibernate
- **Kafka Metrics**: Producer/consumer lag, throughput metrics

### Monitoring Stack

```bash
# Start monitoring infrastructure
docker compose -f compose.yaml up -d prometheus grafana

# Access dashboards
open http://localhost:3000  # Grafana (admin/changeme)
open http://localhost:9090  # Prometheus
```

### Pre-configured Dashboards

- **Application Overview**: Request rates, response times, error rates
- **JVM Monitoring**: Memory usage, GC performance, thread metrics
- **Database Performance**: Connection pools, query execution times
- **Kafka Monitoring**: Topic throughput, consumer lag, partition metrics

### Log Aggregation

- **Structured Logging**: JSON format with correlation IDs
- **Log Levels**: Environment-specific configuration
- **Centralized Logging**: Loki integration available

---

## 🧪 Testing Strategy

### Testing Pyramid Implementation

```shell script
# Run all tests with Testcontainers
./mvnw verify -P use-testcontainers

# Run only unit tests
./mvnw test

# Run integration tests
./mvnw integration-test

# Generate coverage report
./mvnw jacoco:report
```

### Testing Technologies

- **Unit Tests**: JUnit 5, Mockito, Hamcrest
- **Integration Tests**: Spring Boot Test, Testcontainers (PostgreSQL, Kafka)
- **API Testing**: MockMvc, WireMock for external service mocking
- **Architecture Tests**: ArchUnit for enforcing architectural constraints

### Quality Gates

- **Minimum Code Coverage**: 80% (enforced by JaCoCo)
- **Branch Coverage**: 85% minimum
- **Mutation Testing**: Available via PIT testing
- **Static Analysis**: Checkstyle, PMD, SpotBugs

## 🚀 Performance & Scalability

### Performance Characteristics

- **Connection Pooling**: HikariCP for optimal database connections
- **Caching Strategy**: EhCache for frequently accessed data
- **Kafka Partitioning**: 4 partitions for parallel processing

### Monitoring & Metrics

```bash
# Application metrics
curl http://localhost:8085/actuator/metrics
curl http://localhost:8086/actuator/metrics

# Health checks
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# Prometheus metrics
curl http://localhost:8085/actuator/prometheus
```

### Scalability Considerations

- **Horizontal Scaling**: Stateless services support multiple instances
- **Database Scaling**: Read replicas supported via Spring profiles
- **Kafka Consumer Groups**: Multiple consumer instances for load distribution
- **Caching**: Distributed caching ready (Redis integration available)

---

## 📚 Code quality

The code follows the [Google Java Style](https://google.github.io/styleguide/javaguide.html) and is checked by Checkstyle, PMD, and SpotBugs. Optional SonarQube is available in `compose.yaml`.

## 📏 Cursor Rules

The project includes Cursor Editor rules for consistent development practices:

- **Java Language Best Practices**: Rules for code style, performance, security, and testing to ensure clean, maintainable, and efficient Java code.
- **Spring Boot Best Practices**: Guidelines for proper implementation of Spring Boot patterns, dependency injection, configuration, and RESTful API design.

These rules are available in the `.cursor/rules` directory and are automatically applied when using the Cursor Editor.

---

## 🔧 Troubleshooting

### Common Issues

#### Build Issues

```bash
# Clear Maven cache
./mvnw dependency:purge-local-repository

# Skip tests if needed
./mvnw clean install -DskipTests

# Check for dependency conflicts
./mvnw dependency:tree
```

#### Runtime Issues

```bash
# Check application health
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# View application logs
docker logs lorem-demo-words-processing
docker logs lorem-demo-reports-history

# Check Kafka connectivity
docker exec -it lorem-demo-kafka kafka-topics --bootstrap-server localhost:9092 --list
```

#### Database Issues

```bash
# Connect to PostgreSQL
docker exec -it lorem-demo-postgres psql -U postgres -d lorem_db

# Check database migrations
./mvnw liquibase:status -pl reports-history

# Reset database (development only)
docker compose -f compose.yaml down -v
docker compose -f compose.yaml up -d postgres
```

### Performance Tuning

```bash
# JVM tuning for containers
export JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Enable virtual threads (Java 21+)
export JAVA_OPTS="$JAVA_OPTS --enable-preview"

# Database connection tuning
export DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=20
export DATASOURCE_HIKARI_MINIMUM_IDLE=5
```

## 🧹 Cleanup & Maintenance

### Development Cleanup

```shell script
# Clean build artifacts
./mvnw clean

# Stop and remove containers
docker compose -f compose.yaml down

# Remove volumes (data loss!)
docker compose -f compose.yaml down -v

# Clean Docker system
docker system prune -f
```

### Production Maintenance

```bash
# Health check endpoints
curl http://localhost:8085/actuator/health/liveness
curl http://localhost:8086/actuator/health/readiness

# Graceful shutdown
curl -X POST http://localhost:8085/actuator/shutdown
curl -X POST http://localhost:8086/actuator/shutdown

# Log rotation and cleanup
find logs/ -name "*.log" -mtime +7 -delete
```

---

## 📄 License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE).

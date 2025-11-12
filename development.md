# Development Guide

## Getting Started

This guide helps developers set up their local environment and understand the development workflow for the Words Processing microservices project.

## Prerequisites

### Required Software

- **Java 21+**: OpenJDK or Oracle JDK

  ```bash
  java -version  # Should show 21 or higher
  ```

- **Maven 3.8+**: Build tool (wrapper included)

  ```bash
  ./mvnw --version
  ```

- **Docker & Docker Compose**: For infrastructure services

  ```bash
  docker --version
  docker compose version
  ```

- **Git**: Version control
  ```bash
  git --version
  ```

### Recommended Tools

- **IDE**: IntelliJ IDEA (recommended), VS Code, or Eclipse
- **Database Client**: DBeaver, pgAdmin, or similar
- **API Client**: Postman, Insomnia, or curl
- **Kafka Client**: Kafka Tool, Conduktor, or command-line tools

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/IQKV/sample-lorem.git
cd sample-lorem
```

### 2. Start Infrastructure Services

```bash
# Start PostgreSQL, Kafka, and monitoring stack
docker compose -f compose.yaml up -d

# Verify services are running
docker compose -f compose.yaml ps
```

### 3. Build the Project

```bash
# Clean build with tests
./mvnw clean install

# Quick build without tests
./mvnw clean install -DskipTests

# Build specific module
./mvnw clean install -pl shared
```

### 4. Run the Applications

#### Option 1: Using Maven Spring Boot Plugin

```bash
# Terminal 1: Start words-processing service
./mvnw spring-boot:run -pl words-processing

# Terminal 2: Start reports-history service
./mvnw spring-boot:run -pl reports-history
```

#### Option 2: Using JAR Files

```bash
# Build JARs
./mvnw clean package -DskipTests

# Run services
java -jar words-processing/target/sample-lorem-words-processing-*.jar
java -jar reports-history/target/sample-lorem-reports-history-*.jar
```

### 5. Verify Setup

```bash
# Check health endpoints
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# Test the API
curl "http://localhost:8085/api/v1/text?p=1&l=short"
curl "http://localhost:8086/api/v1/history"
```

## Development Workflow

### Code Style and Standards

The project follows Google Java Style Guide with additional conventions:

#### Code Formatting

```bash
# Format code with Prettier (if configured)
npm run prettier:write

# Check code style
./mvnw checkstyle:check

# Run all quality checks
./mvnw verify -P use-qulice
```

#### Naming Conventions

- **Classes**: PascalCase (`UserService`, `ProductController`)
- **Methods/Variables**: camelCase (`findUserById`, `isValidEmail`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_RETRY_ATTEMPTS`)
- **Packages**: lowercase (`com.iqkv.sample.lorem.processing`)

### Modern Java Features

The project leverages modern Java features:

#### Records for DTOs

```java
// Instead of traditional POJOs
public record CreateUserRequest(@NotBlank String username, @Email String email, @Valid AddressDto address) {}

// Response DTOs
public record UserResponse(String id, String username, String email, Instant createdAt) {
  public UserResponse(User user) {
    this(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
  }
}
```

#### Pattern Matching

```java
// Enhanced switch expressions
public String processResult(Result result) {
  return switch (result) {
    case Success(var data) -> "Processed: " + data;
    case Error(var message) -> "Failed: " + message;
    case Pending() -> "Still processing...";
  };
}
```

#### Text Blocks

```java
// Multi-line strings
String query = """
  SELECT u.id, u.username, u.email
  FROM users u
  WHERE u.active = true
    AND u.created_at > ?
  ORDER BY u.username
  """;
```

### Testing Strategy

#### Test Structure

```
src/test/java/
├── unit/                    # Unit tests
│   ├── service/
│   ├── controller/
│   └── util/
├── integration/             # Integration tests
│   ├── api/
│   ├── repository/
│   └── kafka/
└── architecture/            # Architecture tests
    └── ArchitectureTest.java
```

#### Writing Tests

```java
// Unit test example
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("Should create user with valid data")
  void shouldCreateUserWithValidData() {
    // Given
    var request = new CreateUserRequest("john", "john@example.com");
    var user = new User("john", "john@example.com");
    when(userRepository.save(any(User.class))).thenReturn(user);

    // When
    var result = userService.createUser(request);

    // Then
    assertThat(result.username()).isEqualTo("john");
    verify(userRepository).save(any(User.class));
  }
}

// Integration test example
@SpringBootTest
@Testcontainers
class UserControllerIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void shouldCreateAndRetrieveUser() {
    // Test implementation
  }
}
```

#### Running Tests

```bash
# All tests
./mvnw test

# Integration tests with Testcontainers
./mvnw verify -P use-testcontainers

# Specific test class
./mvnw test -Dtest=UserServiceTest

# Generate coverage report
./mvnw jacoco:report
open target/site/jacoco/index.html
```

### Database Development

#### Schema Management

The project uses Liquibase for database migrations:

```bash
# Check migration status
./mvnw liquibase:status -pl reports-history

# Apply migrations
./mvnw liquibase:update -pl reports-history

# Generate changelog from existing database
./mvnw liquibase:generateChangeLog -pl reports-history
```

#### Creating Migrations

1. Create new changeset in `reports-history/src/main/resources/db/changelog/`
2. Follow naming convention: `YYYY-MM-DD-sequence-description.xml`
3. Test migration locally
4. Include rollback instructions

Example changeset:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                   http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.0.xsd">

    <changeSet id="2023-12-01-001-add-user-table" author="developer">
        <createTable tableName="users">
            <column name="id" type="BIGSERIAL">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="username" type="VARCHAR(100)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="email" type="VARCHAR(255)">
                <constraints nullable="false" unique="true"/>
            </column>
            <column name="created_at" type="TIMESTAMP WITH TIME ZONE">
                <constraints nullable="false"/>
            </column>
        </createTable>

        <rollback>
            <dropTable tableName="users"/>
        </rollback>
    </changeSet>
</databaseChangeLog>
```

### Kafka Development

#### Topic Management

```bash
# List topics
docker exec -it dev-lorem-kafka kafka-topics --bootstrap-server localhost:9092 --list

# Create topic
docker exec -it dev-lorem-kafka kafka-topics --bootstrap-server localhost:9092 \
  --create --topic test-topic --partitions 4 --replication-factor 1

# Describe topic
docker exec -it dev-lorem-kafka kafka-topics --bootstrap-server localhost:9092 \
  --describe --topic words.processed
```

#### Testing Kafka Integration

```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "test-topic" })
class KafkaIntegrationTest {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Test
  void shouldSendAndReceiveMessage() {
    // Test implementation
  }
}
```

### API Development

#### Adding New Endpoints

1. **Define DTO/Record**:

   ```java
   public record CreateProductRequest(@NotBlank String name, @NotNull @Positive BigDecimal price) {}
   ```

2. **Create Controller**:

   ```java
   @RestController
   @RequestMapping("/api/v1/products")
   @Validated
   public class ProductController {

     @PostMapping
     public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
       // Implementation
     }
   }
   ```

3. **Add OpenAPI Documentation**:

   ```java
   @Operation(
       summary = "Create new product",
       description = "Creates a new product with the provided details"
   )
   @ApiResponses({
       @ApiResponse(responseCode = "201", description = "Product created"),
       @ApiResponse(responseCode = "400", description = "Invalid input")
   })
   ```

4. **Write Tests**:
   ```java
   @WebMvcTest(ProductController.class)
   class ProductControllerTest {
     // Test implementation
   }
   ```

### Monitoring and Observability

#### Adding Custom Metrics

```java
@Component
public class BusinessMetrics {

  private final Counter orderCounter;
  private final Timer processingTimer;

  public BusinessMetrics(MeterRegistry meterRegistry) {
    this.orderCounter = Counter.builder("orders.created").description("Number of orders created").register(meterRegistry);

    this.processingTimer = Timer.builder("order.processing.duration").description("Order processing time").register(meterRegistry);
  }

  public void recordOrderCreated() {
    orderCounter.increment();
  }

  public <T> T recordProcessingTime(Supplier<T> operation) {
    return processingTimer.recordCallable(operation::get);
  }
}
```

#### Structured Logging

```java
@Slf4j
@RestController
public class ProductController {

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    log.info("Fetching product: productId={}", id);

    try {
      var product = productService.findById(id);
      log.info("Product retrieved successfully: productId={}, name={}", product.getId(), product.getName());
      return ResponseEntity.ok(product);
    } catch (final Exception ex) {
      log.error("Error fetching product: productId={}, error={}", id, ex.getMessage(), ex);
      throw ex;
    }
  }
}
```

## IDE Configuration

### IntelliJ IDEA

#### Recommended Plugins

- **Lombok**: For annotation processing
- **Spring Boot**: Enhanced Spring support
- **Docker**: Container management
- **Database Navigator**: Database tools
- **SonarLint**: Code quality analysis

#### Code Style Settings

1. Import Google Java Style: `File → Settings → Editor → Code Style → Java`
2. Import scheme from: `https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml`
3. Configure Lombok: `File → Settings → Build → Compiler → Annotation Processors`

#### Run Configurations

Create run configurations for:

- Words Processing Application
- Reports History Application
- All Tests
- Integration Tests

### VS Code

#### Recommended Extensions

```json
{
  "recommendations": ["vscjava.vscode-java-pack", "pivotal.vscode-spring-boot", "gabrielbb.vscode-lombok", "ms-vscode.vscode-docker", "sonarsource.sonarlint-vscode"]
}
```

#### Settings

```json
{
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.compile.nullAnalysis.mode": "automatic",
  "spring-boot.ls.problem.application-properties.unknown-property": "warning"
}
```

## Debugging

### Application Debugging

```bash
# Enable debug mode
export JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
./mvnw spring-boot:run -pl words-processing

# Or use IDE debug configuration
```

### Database Debugging

```bash
# Connect to PostgreSQL
docker exec -it dev-lorem-postgres psql -U postgres -d lorem_db

# Enable query logging
ALTER SYSTEM SET log_statement = 'all';
SELECT pg_reload_conf();

# View logs
docker logs dev-lorem-postgres
```

### Kafka Debugging

```bash
# Monitor topic messages
docker exec -it dev-lorem-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic words.processed \
  --from-beginning

# Check consumer group status
docker exec -it dev-lorem-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --describe --group reports-history
```

## Performance Profiling

### JVM Profiling

```bash
# Enable JFR (Java Flight Recorder)
export JAVA_OPTS="-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=profile.jfr"

# Analyze with JProfiler, VisualVM, or async-profiler
```

### Load Testing

```bash
# Install Apache Bench
sudo apt-get install apache2-utils

# Simple load test
ab -n 1000 -c 10 "http://localhost:8085/api/v1/text?p=1&l=short"

# Or use wrk
wrk -t12 -c400 -d30s "http://localhost:8085/api/v1/text"
```

## Troubleshooting

### Common Issues

#### Port Conflicts

```bash
# Check what's using port 8085
lsof -i :8085
netstat -tulpn | grep 8085

# Kill process
kill -9 <PID>
```

#### Memory Issues

```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2g -XX:MaxMetaspaceSize=512m"

# JVM memory tuning
export JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC"
```

#### Docker Issues

```bash
# Clean up Docker
docker system prune -f
docker volume prune -f

# Reset Docker Desktop (if needed)
```

### Getting Help

- **Documentation**: Check this guide and README.md
- **Issues**: Search existing GitHub issues
- **Logs**: Check application and container logs
- **Community**: Ask questions in team channels

## Contributing

See [CONTRIBUTING.md](.github/CONTRIBUTING.md) for detailed contribution guidelines.

### Quick Contribution Checklist

- [ ] Code follows style guidelines
- [ ] Tests added for new functionality
- [ ] Documentation updated
- [ ] Commit messages follow convention
- [ ] PR template completed
- [ ] All checks pass

## Resources

### Documentation

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

### Tools

- [Testcontainers](https://www.testcontainers.org/)
- [Liquibase](https://docs.liquibase.com/)
- [Micrometer](https://micrometer.io/docs)

### Best Practices

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Spring Boot Best Practices](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.best-practices)
- [Microservices Patterns](https://microservices.io/patterns/)

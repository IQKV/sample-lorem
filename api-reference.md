# API Reference Guide

## Overview

This document provides comprehensive API documentation for the Words Processing microservices system.

## Base URLs

- **Words Processing Service**: `http://localhost:8085`
- **Reports History Service**: `http://localhost:8086`

## Authentication

Currently, the APIs are open for development purposes. In production environments, consider implementing:

- JWT-based authentication
- API key validation
- OAuth2 integration

## Words Processing API

### Generate and Process Text

**Endpoint**: `GET /api/v1/text`

Fetches lorem ipsum text from loripsum.net, analyzes it, and publishes results to Kafka.

#### Parameters

| Parameter | Type    | Required | Description          | Default | Valid Values                  |
| --------- | ------- | -------- | -------------------- | ------- | ----------------------------- |
| `p`       | integer | No       | Number of paragraphs | 1       | 1-10                          |
| `l`       | string  | No       | Text length          | medium  | short, medium, long, verylong |

#### Response

```json
{
  "id": "uuid-string",
  "originalText": "Lorem ipsum dolor sit amet...",
  "wordCount": 150,
  "characterCount": 892,
  "paragraphCount": 2,
  "averageWordsPerParagraph": 75.0,
  "mostFrequentWords": [
    { "word": "lorem", "frequency": 5 },
    { "word": "ipsum", "frequency": 3 }
  ],
  "processedAt": "2023-12-01T10:30:00Z",
  "processingTimeMs": 45
}
```

#### Example Requests

```bash
# Basic request
curl "http://localhost:8085/api/v1/text"

# Custom parameters
curl "http://localhost:8085/api/v1/text?p=3&l=long"

# With response formatting
curl -H "Accept: application/json" \
     "http://localhost:8085/api/v1/text?p=2&l=short" | jq
```

#### Error Responses

```json
{
  "timestamp": "2023-12-01T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid parameter value",
  "path": "/api/v1/text",
  "details": {
    "p": "must be between 1 and 10",
    "l": "must be one of: short, medium, long, verylong"
  }
}
```

## Reports History API

### Get Processing History

**Endpoint**: `GET /api/v1/history`

Retrieves paginated history of text processing reports.

#### Parameters

| Parameter | Type    | Required | Description           | Default |
| --------- | ------- | -------- | --------------------- | ------- |
| `page`    | integer | No       | Page number (0-based) | 0       |
| `size`    | integer | No       | Page size             | 20      |
| `sort`    | string  | No       | Sort criteria         | id,desc |

#### Response

```json
{
  "content": [
    {
      "id": 1,
      "reportId": "uuid-string",
      "wordCount": 150,
      "characterCount": 892,
      "paragraphCount": 2,
      "averageWordsPerParagraph": 75.0,
      "processedAt": "2023-12-01T10:30:00Z",
      "createdAt": "2023-12-01T10:30:01Z"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 150,
  "totalPages": 8,
  "first": true,
  "last": false,
  "numberOfElements": 20
}
```

#### Example Requests

```bash
# Get first page
curl "http://localhost:8086/api/v1/history"

# Get specific page with custom size
curl "http://localhost:8086/api/v1/history?page=2&size=10"

# Sort by processing time
curl "http://localhost:8086/api/v1/history?sort=processedAt,asc"

# Multiple sort criteria
curl "http://localhost:8086/api/v1/history?sort=wordCount,desc&sort=processedAt,asc"
```

### Get Report by ID

**Endpoint**: `GET /api/v1/history/{id}`

Retrieves a specific processing report by its database ID.

#### Parameters

| Parameter | Type | Required | Description        |
| --------- | ---- | -------- | ------------------ |
| `id`      | long | Yes      | Report database ID |

#### Response

```json
{
  "id": 1,
  "reportId": "uuid-string",
  "originalText": "Lorem ipsum dolor sit amet...",
  "wordCount": 150,
  "characterCount": 892,
  "paragraphCount": 2,
  "averageWordsPerParagraph": 75.0,
  "mostFrequentWords": [
    { "word": "lorem", "frequency": 5 },
    { "word": "ipsum", "frequency": 3 }
  ],
  "processedAt": "2023-12-01T10:30:00Z",
  "createdAt": "2023-12-01T10:30:01Z"
}
```

## Health and Monitoring Endpoints

### Health Checks

Both services expose Spring Boot Actuator endpoints:

```bash
# Overall health
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# Detailed health (includes database, Kafka)
curl http://localhost:8085/actuator/health/details
curl http://localhost:8086/actuator/health/details

# Liveness probe
curl http://localhost:8085/actuator/health/liveness
curl http://localhost:8086/actuator/health/liveness

# Readiness probe
curl http://localhost:8085/actuator/health/readiness
curl http://localhost:8086/actuator/health/readiness
```

### Metrics

```bash
# Prometheus metrics
curl http://localhost:8085/actuator/prometheus
curl http://localhost:8086/actuator/prometheus

# Application info
curl http://localhost:8085/actuator/info
curl http://localhost:8086/actuator/info

# JVM metrics
curl http://localhost:8085/actuator/metrics/jvm.memory.used
curl http://localhost:8086/actuator/metrics/jvm.memory.used
```

## Error Handling

### Standard Error Response Format

All APIs return errors in a consistent format:

```json
{
  "timestamp": "2023-12-01T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/text",
  "details": {
    "field": "error description"
  }
}
```

### HTTP Status Codes

| Status Code | Description           | When It Occurs                              |
| ----------- | --------------------- | ------------------------------------------- |
| 200         | OK                    | Successful request                          |
| 400         | Bad Request           | Invalid parameters or request body          |
| 404         | Not Found             | Resource not found                          |
| 500         | Internal Server Error | Unexpected server error                     |
| 503         | Service Unavailable   | External service (loripsum.net) unavailable |

## Rate Limiting

Currently, no rate limiting is implemented. For production use, consider:

- API Gateway with rate limiting
- Redis-based rate limiting
- Per-client quotas

## Versioning

APIs use URL path versioning (`/api/v1/`). Future versions will be available at `/api/v2/`, etc.

## CORS Configuration

CORS is configured to allow requests from:

- `http://localhost:3000` (React development)
- `http://localhost:8080` (Vue.js development)
- Custom origins via `CORS_ALLOWED_ORIGINS` environment variable

## Content Types

### Supported Request Content Types

- `application/json`
- `application/x-www-form-urlencoded` (for query parameters)

### Supported Response Content Types

- `application/json` (default)
- `application/hal+json` (for paginated responses)

## Examples and Use Cases

### Workflow Example

1. **Generate Text Report**:

   ```bash
   curl "http://localhost:8085/api/v1/text?p=3&l=long"
   ```

2. **Wait for Processing** (reports are processed asynchronously via Kafka)

3. **Retrieve History**:
   ```bash
   curl "http://localhost:8086/api/v1/history?page=0&size=5&sort=processedAt,desc"
   ```

### Integration Testing

```bash
#!/bin/bash
# Integration test script

# Generate a report
RESPONSE=$(curl -s "http://localhost:8085/api/v1/text?p=2&l=medium")
REPORT_ID=$(echo $RESPONSE | jq -r '.id')

echo "Generated report: $REPORT_ID"

# Wait for Kafka processing
sleep 2

# Check if report appears in history
HISTORY=$(curl -s "http://localhost:8086/api/v1/history?size=1&sort=processedAt,desc")
LATEST_REPORT=$(echo $HISTORY | jq -r '.content[0].reportId')

if [ "$REPORT_ID" = "$LATEST_REPORT" ]; then
    echo "✅ Integration test passed"
else
    echo "❌ Integration test failed"
fi
```

## SDK and Client Libraries

Currently, no official SDKs are provided. You can generate client libraries using:

- **OpenAPI Generator**: Generate clients from OpenAPI specs
- **Swagger Codegen**: Alternative code generation tool
- **Postman Collections**: Import OpenAPI specs into Postman

### Generate Java Client

```bash
# Install OpenAPI Generator
npm install @openapitools/openapi-generator-cli -g

# Generate Java client
openapi-generator-cli generate \
  -i http://localhost:8085/v3/api-docs \
  -g java \
  -o ./generated-client \
  --additional-properties=groupId=com.iqkv,artifactId=lorem-client
```

## Support and Contact

For API support and questions:

- **Issues**: [GitHub Issues](https://github.com/IQKV/sample-lorem/issues)
- **Documentation**: This repository's documentation
- **Email**: Contact repository maintainers

## Changelog

### v0.25.0-SNAPSHOT

- Initial API implementation
- OpenAPI 3.0 documentation
- Kafka-based async processing
- PostgreSQL persistence
- Comprehensive monitoring endpoints

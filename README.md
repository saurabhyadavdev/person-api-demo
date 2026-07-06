# Person API

A RESTful CRUD API for managing person records, built with Spring Boot 3.4 and Java 21. Demonstrates a clean 3-layer architecture (Controller → Service → Repository), proper DTO separation, comprehensive error handling, and full test coverage.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.4.2, Spring Data JPA, Spring Security 6 |
| Language | Java 21 |
| Database | H2 (in-memory) |
| Build | Maven (wrapper included) |
| Container | Docker (multi-stage build) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| CI | GitHub Actions |
| Testing | JUnit 5, Mockito, AssertJ, Spring Boot Test |

## Architecture

```
Controller → Service → Repository → JPA Entity
     │            │            │
     │            │      PersonJpaRepository
     │      PersonService
PersonController
```

- **Controller** — HTTP routing, request validation, status codes
- **Service** — Business logic, DTO mapping, exception handling
- **Repository** — Data access (thin wrapper over Spring Data JPA)
- **DTOs** — `PersonRequest` (input) and `PersonResponse` (output), never expose entities

## Features

- CRUD operations on person records (GET, POST, PUT, DELETE)
- Input validation (`@Valid`, `@NotBlank`, `@Min`)
- HTTP Basic Authentication
- Global exception handler with consistent error JSON
- OpenAPI/Swagger documentation
- Docker containerization
- CI pipeline via GitHub Actions

## Quick Start

### Prerequisites

- JDK 21
- Docker (optional — for containerized run)

### Build & Test

```bash
./mvnw clean test
```

### Run

```bash
./mvnw spring-boot:run
```

### Verify

```bash
curl -u admin:admin123 http://localhost:8080/api/v1/persons
```

## API Endpoints

All endpoints require HTTP Basic Authentication (default: `admin` / `admin123`).

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| `GET` | `/api/v1/persons` | List all persons | 200 |
| `GET` | `/api/v1/persons/{id}` | Get person by ID | 200 / 404 |
| `POST` | `/api/v1/persons` | Create person | 201 |
| `PUT` | `/api/v1/persons/{id}` | Update person | 200 / 404 |
| `DELETE` | `/api/v1/persons/{id}` | Delete person | 204 / 404 |

### Example Request

```json
POST /api/v1/persons
{
  "firstName": "Jane",
  "lastName": "Doe",
  "age": 28,
  "favouriteColor": "Green"
}
```

## Docker

```bash
# Build
docker build -t person-api .

# Run
docker run -p 8080:8080 person-api
```

## API Documentation

With the app running, visit:
[http://localhost:8080/swagger-ui-custom.html](http://localhost:8080/swagger-ui-custom.html)

## Seed Data

The application loads sample person records on startup via `data.sql`:

| Name | Age | Favourite Color |
|------|-----|-----------------|
| Saurabh Yadav | 35 | Blue |
| Sarika Yadav | 31 | Pink |
| Gauraw Yadav | 33 | Green |

## Testing

30 tests across 6 test classes covering:

- **Unit tests** — `PersonServiceTest` with mocked repository (8 tests)
- **Web layer tests** — `PersonControllerTest` via `@WebMvcTest` (9 tests)
- **Data layer tests** — `PersonRepositoryTest` via `@DataJpaTest` (5 tests)
- **Integration tests** — `SecurityConfigTest` with full context (2 tests)
- **Smoke test** — `PersonDemoApplicationTests` context load (1 test)
- **DTO tests** — `DtoTest` (5 tests)

## Project Structure

```
src/main/java/org/example/person/
├── config/SecurityConfig.java
├── controllers/PersonController.java
├── dto/
│   ├── PersonRequest.java
│   └── PersonResponse.java
├── exceptions/
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   └── PersonNotFoundException.java
├── models/Person.java
├── repositories/
│   ├── PersonJpaRepository.java
│   └── PersonRepository.java
├── services/PersonService.java
└── PersonDemoApplication.java
```

 Member Management Service

The Member Management Service is a standalone Spring Boot microservice responsible for handling member registration, profile updates, and managing the membership lifecycle in a Library Management System. It is designed for modularity, scalability, and integration within a microservices ecosystem via Eureka Discovery and Spring Cloud Gateway.

Functional Scope

- Register new members
- Retrieve member details by ID or email
- Update member profiles
- Manage membership status (ACTIVE / INACTIVE)
- (Optional) Search/filter by parameters


Technology Stack

| Layer              | Technology             |
|--------------------|------------------------|
| Language           | Java 17+               |
| Framework          | Spring Boot 3.x        |
| REST API Docs      | Springdoc OpenAPI      |
| ORM & Persistence  | Spring Data JPA        |
| Database           | MySQL                  |
| Service Discovery  | Eureka                 |
| API Gateway        | Spring Cloud Gateway   |


 REST API Endpoints

| Method | Endpoint                  | Description               |
|--------|---------------------------|---------------------------|
| POST   | `/api/members`            | Register a new member     |
| PUT    | `/api/members/{id}`       | Update existing member    |
| GET    | `/api/members/{id}`       | Retrieve member by ID     |
| GET    | `/api/members?email=`     | Retrieve member by email  |


Data Model

`Member` Entity

| Field            | Type     | Description                    |
|------------------|----------|--------------------------------|
| memberId         | BIGINT   | Primary key, auto-generated    |
| name             | VARCHAR  | Member's full name             |
| email            | VARCHAR  | Unique                         |
| phone            | VARCHAR  |                                |
| address          | VARCHAR  |                                |
| membershipStatus | ENUM     | ACTIVE / INACTIVE              |
| registrationDate | DATE     | Member's onboarding date       |


Service Layer

Responsible for:
- Validating and processing incoming requests
- Performing update logic and exception handling
- Delegating persistence to the repository layer


Controller Layer

Responsible for:
- Exposing RESTful endpoints
- Transforming input/output via DTOs
- Returning appropriate HTTP responses


Service Discovery

This microservice registers itself with Eureka Server using the following config in `application.properties`:

properties
spring.application.name=member-service
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka

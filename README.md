# Library Management System (LMS)

## 📚 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture Diagram](#architecture-diagram)
- [Module Overview](#module-overview)
  - [Book Service](#book-service)
  - [Member Service](#member-service)
  - [Transaction Service](#transaction-service)
  - [Fine Service](#fine-service)
  - [Notification Service](#notification-service)
  - [API Gateway](#api-gateway)
  - [Discovery Server (Eureka)](#discovery-server-eureka)
- [Setup Instructions](#setup-instructions)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Overview
The Library Management System (LMS) is a RESTful API-based backend application designed to manage book collections, member registrations, borrowing and returning of books, overdue tracking, and notifications. It is built using Spring Boot and supports relational databases like MySQL and PostgreSQL.

---

## Features
- **Book Management**: Add, update, delete, and search books.
- **Member Management**: Register and manage library members.
- **Borrowing and Return**: Track book borrowing and return processes.
- **Overdue and Fines**: Monitor overdue books and calculate fines.
- **Notifications**: Send alerts for due dates and fines.

---

## Tech Stack

- Java 
- Spring Boot 
- Spring Data JPA
- MySQL
- Springdoc OpenAPI (Swagger)
- Eureka Discovery Client

---
## Architecture Diagram
```mermaid
flowchart TD
 
  subgraph Client [Client Applications]
    A[Web App]
    B[Mobile App]
  end
 
  subgraph Gateway [API Gateway]
    C[API Gateway]
  end
 
  subgraph Infra [Infrastructure Services]
    D[Load Balancer]
    E[Discovery Service]
    F[Config Service]
  end
 
  subgraph Services [Microservices]
    G[User Service] --> GDB[(User DB)]
    H[Book Service] --> HDB[(Book DB)]
    I[Loan Service] --> IDB[(Loan DB)]
    J[Notification Service] --> JDB[(Notification DB)]
    J --> Queue[(Message Queue)]
    K[Catalog Service] --> KDB[(Catalog DB)]
    L[Recommendation Service] --> LDB[(Recommendation DB)]
  end
 
  subgraph External [External Services]
    M[Remote Web Service]
  end
 
  %% Connections
  A --> C
  B --> C
  C --> D
  D --> E
  D --> F
  C --> G
  C --> H
  C --> I
  C --> J
  C --> K
  C --> L
  L --> M
 
  %% Styling
  classDef client fill:#e3f2fd,stroke:#2196f3,color:#0d47a1
  classDef gateway fill:#fff3e0,stroke:#ff9800,color:#e65100
  classDef infra fill:#ede7f6,stroke:#673ab7,color:#311b92
  classDef service fill:#e8f5e9,stroke:#4caf50,color:#1b5e20
  classDef external fill:#fce4ec,stroke:#f06292,color:#880e4f
  classDef db fill:#f3e5f5,stroke:#ab47bc,color:#4a148c
  classDef queue fill:#fffde7,stroke:#fbc02d,color:#f57f17
 
  class A,B client
  class C gateway
  class D,E,F infra
  class G,H,I,J,K,L service
  class M external
  class GDB,HDB,IDB,JDB,KDB,LDB db
  class Queue queue
```
---
##  Module Overview

The Library Management System is built using a microservices architecture. Each module handles a specific business capability and communicates via REST APIs. Below is a quick summary:

### Book Service
Handles the library's book catalog. Responsible for adding, updating, searching, and listing books, along with managing availability status (e.g., available, issued, reserved).
- [Book Service](./book-service/README.md)

### Member Service
Manages all library members: registration, updates, lookups, and status changes. Exposes endpoints to search and retrieve member details and supports member lifecycle management.
- [Member Service](./member-service/README.md)

### Transaction Service
Manages the issuance and return of books by members. Ensures book availability, tracks due dates, and maintains borrowing history. Coordinates with both Member and Book services.
- [Transaction Service](./transaction-service/README.md)
###  Fine Service
Calculates and manages overdue fines for borrowed books. It tracks return deadlines, applies configurable penalty rules, and exposes endpoints for querying outstanding dues. Integrates with the Borrowing Service to detect overdue returns and can trigger notifications via the Notification Service.
- [Fine Service](./fine-service/README.md)

###  Notification Service
Sends alerts and reminders to users. Used to notify members about upcoming due dates, overdue returns, registration confirmations, and system messages (via email, SMS, etc.).
- [ Notification Service](./notification-service/README.md)
  
### API Gateway
Provides a unified entry point to route incoming client requests to appropriate microservices. Also handles load balancing, logging, and cross-cutting concerns.
Access the API documentation at `http://localhost:8080/swagger-ui.html`.

###  Discovery Server (Eureka)
Acts as a service registry where all microservices register themselves. Enables dynamic service discovery and communication within the ecosystem.

Eureka Discovery : http://localhost:8761/

---
## Setup Instructions
1. Clone the repository:
   ```bash
   git clone repository
   cd LMS
   ```
2. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

## Testing
Run the tests using Maven:
```bash
mvn test
```
---

## Contributing
Contributions are welcome! Please see the `CONTRIBUTING.md` file for guidelines.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

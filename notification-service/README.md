#  Notification Service

## Contributors
- Sakshi Shinde
- Parth Deshmukh
- Nehal Rane
- Sakshi Gujar
  
## 📚 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Folder Structure](#folder-structure)
- [REST API Endpoints](#rest-api-endpoints)
- [Data Model](#data-model)
- [Module Architecture Diagram](#module-architecture-diagram)
- [Component Diagram](#component-diagram)
- [Sequence Diagram](#sequence-diagram)
- [UI Section](#ui-section)
- [Run Locally](#run-locally)
 
## Overview
-  The **Notification Service** delivers alerts and system messages to library members. It handles due date reminders, overdue notices, and fine notifications by email, SMS, or in-app delivery. This service is triggered by other modules like Borrowing and Fine and logs all sent messages for traceability.

---
##  Features

- Accept notification requests from other services
- Log and store notifications for each member
- Enable member-specific notification retrieval
- Future-proofed for email/SMS integrations
- Trigger alerts based on due dates, fines, and account status

---

## Folder Structure
<pre>
src/
└── main/
    ├── java/
    │   └── com.library.book/
    │       ├── controller/       # REST controllers
    │       ├── dto/              # Data Transfer Objects
    │       ├── entity/           # JPA Entities
    │       ├── repository/       # Spring Data Repositories
    │       └── service/          # Business logic layer
    └── resources/
        └── application.properties  # App configuration
</pre>
---
##  REST API Endpoints

| Method | Endpoint                                      | Description                                           |
|--------|-----------------------------------------------|-------------------------------------------------------|
| GET    | `/api/fines`                                  | Retrieve all fines                                    |
| GET    | `/api/fines/collected`                        | Get total collected fines                             |
| GET    | `/api/fines/pending`                          | Get total pending fines                               |
| GET    | `/api/fines/{id}`                             | Retrieve a fine by ID                                 |
| GET    | `/api/fines/member/{memberId}`                | Retrieve fines by member ID                           |
| GET    | `/api/fines/member/{memberId}/total`          | Get total pending fines for a specific member         |
| POST   | `/api/fines/{transactionId}/{fineType}`       | Create a fine for a transaction                       |
| PUT    | `/api/fines/{id}/pay`                         | Pay a fine                                            |
| PUT    | `/api/fines/{id}/reverse`                     | Reverse a fine payment                                |
| PUT    | `/api/fines/{id}/cancel`                      | Cancel a fine                                         |
| PUT    | `/api/fines/update-fines`                     | Process and update overdue fines                      |
| DELETE | `/api/fines/{id}`                             | Delete a fine by ID                                   |

Swagger URL : http://localhost:8080/swagger-ui/index.html#/

## Data Model

### `Notification` Entity

| Field           | Type      | Description                          |
|------------------|-----------|--------------------------------------|
| notificationId   | BIGINT    | Primary key                          |
| memberId         | BIGINT    | FK to `Member`                       |
| message          | TEXT      | Notification content                 |
| dateSent         | DATETIME  | Timestamp of delivery                |

---
##  Module Architecture Diagram 

```mermaid
flowchart LR
  A[/api/notifications/] --> B[NotificationController]
  B --> C[NotificationService]
  C --> D[NotificationRepository]
  D --> E[(notification_db<br>MySQL)]
  C --> F[Eureka Discovery Server]

  %% Color Scheme Styling
  classDef endpoint fill:#cce5ff,stroke:#339af0,color:#003566
  classDef controller fill:#ffe8cc,stroke:#ff922b,color:#7f4f24
  classDef service fill:#d3f9d8,stroke:#51cf66,color:#1b4332
  classDef repository fill:#e0f7fa,stroke:#00bcd4,color:#006064
  classDef database fill:#e6e6fa,stroke:#b39ddb,color:#4a148c
  classDef registry fill:#f1f3f5,stroke:#868e96,color:#343a40

  class A endpoint
  class B controller
  class C service
  class D repository
  class E database
  class F registry
```
---
## Component diagram
 
```mermaid
flowchart LR
  subgraph React_Frontend [React Frontend]
    direction TB
    A[Notification UI Components]
    B[Notification API Client]
  end
  subgraph Backend [Spring Boot]
    direction TB
    C[NotificationController]
    D[NotificationService]
    E[NotificationRepository]
  end
  subgraph Database [Relational Database]
    direction TB
    F[(Notifications Table)]
  end
  G[Notification DTO]
  H[Notification Entity]
  %% Connections
  B -->|HTTP/REST| C
  C -->|Calls| D
  D -->|Calls| E
  E -->|ORM / JDBC| F
  C ---|uses| G
  E ---|maps to| H
  %% Styling
  classDef ui fill:#dae8fc,stroke:#6c8ebf,color:#1a237e
  classDef backend fill:#d5e8d4,stroke:#82b366,color:#1b4332
  classDef database fill:#f3e5f5,stroke:#ab47bc,color:#4a148c
  classDef model fill:#fff2cc,stroke:#d6b656,color:#7f4f24
  class A,B ui
  class C,D,E backend
  class F database
  class G,H model
```
## Sequence Diagram
```mermaid
 
sequenceDiagram
  actor UI as React Frontend
  participant C as NotificationController
  participant S as NotificationService
  participant R as NotificationRepository
  participant DB as Database
  UI->>C: HTTP POST /api/notifications (NotificationDto)
  C->>S: registerMember(NotificationDto)
  S->>R: save(notificationEntity)
  R->>DB: INSERT INTO Notifications
  R-->>S: Return Saved Notification
  S-->>C: Return NotificationDto
  C-->>UI: 201 Created (NotificationDto)
  %% Styling (as comments for Mermaid, no visual impact)
  %% UI:       #dae8fc (soft blue)
  %% Controller/Service/Repo: #d5e8d4 (light green)
  %% DB:       #f3e5f5 (lavender)
```
---
## UI Section
- [Dashboard](dashboard/png)
- [Notification](notification.png)

##  Run Locally
```bash
#for Backend :
# Navigate to the folder
cd book-service
# Build and run
mvn clean install
mvn spring-boot:run

#for frontend :
# Install dependencies
npm install
# Start the development server
npm run dev

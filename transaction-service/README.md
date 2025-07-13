# Transaction Service(Borrow / Return)

## Contributor
- Parth Deshmukh

## 📚 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Folder Structure](#folder-structure)
- [REST API Endpoints](#rest-api-endpoints)
- [Data Model](#data-model)
- [Module Architecture Diagram](#module-architecture-diagram)
- [Component Diagram](#component-diagram)
- [Sequence Diagram](#sequence-diagram)
- [Run Locally](#run-locally)

## Overview
-  The Borrowing Service is responsible for handling book loan and return operations in the Library Management System. It maintains borrowing history for each member, checks borrowing eligibility, and updates book availability. This service integrates with Book and Member services and registers with Eureka for service discovery.

## Features
- Borrow a book by a registered member
- Return a borrowed book
- Validate borrow limits or book availability
- View borrowing history by member
- Track active vs. returned borrow records


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
## REST API Endpoints

| Method | Endpoint                                      | Description                                           |
|--------|-----------------------------------------------|-------------------------------------------------------|
| GET    | `/api/transactions`                           | Retrieve all borrowing transactions                   |
| GET    | `/api/transactions/{id}`                      | Retrieve a transaction by ID                          |
| GET    | `/api/transactions/member/{memberId}`         | Retrieve transactions by member ID                    |
| GET    | `/api/transactions/book/{bookId}`             | Retrieve transactions by book ID                      |
| POST   | `/api/transactions/borrow`                    | Borrow a book (create a new transaction)              |
| PUT    | `/api/transactions/{id}/return`               | Return a borrowed book                                |
| POST   | `/api/transactions/update-overdue`            | Update overdue transactions                           |

---
## Data Model

`BorrowingTransaction` Entity

| Field         | Type     | Description                           |
|---------------|----------|---------------------------------------|
| transactionId | BIGINT   | Primary key                           |
| memberId      | BIGINT   | Foreign key to Member                 |
| bookId        | BIGINT   | Foreign key to Book                   |
| borrowDate    | DATE     | Date when the book was borrowed       |
| dueDate       | DATE     | Due date for returning the book       |
| returnDate    | DATE     | Nullable; date of return              |
| status        | VARCHAR  | BORROWED / RETURNED                   |

---
##  Module Architecture Diagram 

```mermaid
flowchart LR
  A[/api/transaction/] --> B[transactionController]
  B --> C[transactionService]
  C --> D[transactionRepository]
  D --> E[(transaction_db<br>MySQL)]
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
    A[Transaction UI Components]
    B[Transaction API Client]
  end
  subgraph Backend [Spring Boot]
    direction TB
    C[TransactionController]
    D[TransactionService]
    E[TransactionRepository]
  end
  subgraph Database [Relational Database]
    direction TB
    F[(Transactions Table)]
  end
  G[Transaction DTO]
  H[Transaction Entity]
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
  participant C as TransactionController
  participant S as TransactionService
  participant R as TransactionRepository
  participant DB as Database
  UI->>C: HTTP POST /api/transactions (TransactionDto)
  C->>S: registerMember(transactionDto)
  S->>R: save(transactionEntity)
  R->>DB: INSERT INTO Transactions
  R-->>S: Return Saved Transaction
  S-->>C: Return TransactionDto
  C-->>UI: 201 Created (TransactionDto)
  %% Styling (as comments for Mermaid, no visual impact)
  %% UI:       #dae8fc (soft blue)
  %% Controller/Service/Repo: #d5e8d4 (light green)
  %% DB:       #f3e5f5 (lavender)
```
---
##  Layered Architecture per Microservice
Each service follows a standard 3-layered architecture pattern to ensure clean separation of concerns and long-term scalability:

**Controller Layer**  
  `BorrowingController`: Exposes endpoints for issuing and returning books  
  → `/api/borrow`, `/api/return`

- **Service Layer**  
  `BorrowingService`, `BorrowingServiceImpl`: Manages due dates, availability checks, and triggers fine calculation

- **Repository Layer**  
  `BorrowingRepository`: Tracks all borrow/return records per member and book
---
## Inter-Service Communication

This service may call:
- **Member Service**: to confirm member is ACTIVE
- **Book Service**: to confirm and update available copies
---
## UI Section
- [Dashboard](dashboard.png)
- [Transaction](transaction.png)

---

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


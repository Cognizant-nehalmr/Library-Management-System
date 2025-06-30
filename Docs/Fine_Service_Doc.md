#  Fine Service

The **Fine Service** handles overdue tracking and fine calculations in the Library Management System. It monitors late returns from the Borrowing Service, calculates penalties based on overdue days, and enables members to view and pay fines. It operates independently and is integrated through Eureka for discoverability.

---

##  Functional Scope

- Track and calculate fines for overdue books
- Associate fines with borrowing transactions or members
- Mark fines as paid or pending
- Provide fine summaries by member
- Support fine payment status updates

---

##  Technology Stack

| Layer              | Technology             |
|-------------------|------------------------|
| Language           | Java 17+               |
| Framework          | Spring Boot 3.x        |
| REST API Docs      | Springdoc OpenAPI      |
| ORM & Persistence  | Spring Data JPA        |
| Database           | MySQL                  |
| Service Discovery  | Eureka                 |
| Gateway Routing    | Spring Cloud Gateway   |

---

##  REST API Endpoints

| Method | Endpoint                    | Description                      |
|--------|-----------------------------|----------------------------------|
| GET    | `/api/fines/{memberId}`     | Get fines for a specific member |
| POST   | `/api/fines`                | Create fine record (overdue)    |
| POST   | `/api/fines/pay/{fineId}`   | Mark a fine as paid             |

> Swagger UI available at: `/swagger-ui/index.html`

---

##  Data Model

### `Fine` Entity

| Field            | Type      | Description                           |
|------------------|-----------|---------------------------------------|
| fineId           | BIGINT    | Primary key                           |
| memberId         | BIGINT    | Foreign key to `Member`               |
| amount           | DECIMAL   | Fine amount calculated                |
| status           | VARCHAR   | PENDING / PAID                        |
| transactionDate  | DATE      | When fine was assessed or paid        |

---

##  Service Layer

Responsible for:
- Receiving triggers from Borrowing Service (when a book is overdue)
- Calculating fine based on configured rate (e.g., ₹10/day)
- Marking fines as paid
- Grouping fines by member

---

##  Controller Layer

Responsible for:
- Providing fine summaries via API
- Accepting fine payment confirmations
- Exposing endpoints to external services or frontend

---

##  Integration

- **Borrowing Service**: triggers fine creation when books are overdue
- **Notification Service** *(optional)*: sends reminders to users with pending fines

---

##  Sample DB Schema

```sql
CREATE TABLE fines (
  fine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  member_id BIGINT NOT NULL,
  amount DECIMAL(8,2) NOT NULL,
  status VARCHAR(20) CHECK (status IN ('PENDING','PAID')),
  transaction_date DATE
);

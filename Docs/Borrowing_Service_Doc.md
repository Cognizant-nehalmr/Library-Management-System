 Borrowing Service

The Borrowing Service is responsible for handling book loan and return operations in the Library Management System. It maintains borrowing history for each member, checks borrowing eligibility, and updates book availability. This service integrates with Book and Member services and registers with Eureka for service discovery.


 Functional Scope

- Borrow a book by a registered member
- Return a borrowed book
- Validate borrow limits or book availability
- View borrowing history by member
- Track active vs. returned borrow records


 Technology Stack

| Layer              | Technology             |
|--------------------|------------------------|
| Language           | Java 17+               |
| Framework          | Spring Boot 3.x        |
| REST API Docs      | Springdoc OpenAPI      |
| ORM & Persistence  | Spring Data JPA        |
| Database           | MySQL                  |
| Service Discovery  | Eureka                 |
| Inter-service Calls| RestTemplate / Feign   |


 REST API Endpoints

| Method | Endpoint                           | Description                    |
|--------|------------------------------------|--------------------------------|
| POST   | `/api/borrowings`                  | Borrow a book                  |
| PUT    | `/api/borrowings/return/{id}`      | Return a book by transactionId |
| GET    | `/api/borrowings/member/{memberId}`| Get member borrow history      |


Data Model

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


 Service Layer

Responsible for:
- Validating whether member and book exist (via Member & Book services)
- Checking book availability
- Generating due dates
- Marking transactions as returned


 Controller Layer

Responsible for:
- Accepting borrow and return requests
- Providing member-specific borrow history
- Ensuring input validation and status codes


 Inter-Service Communication

This service may call:
- **Member Service**: to confirm member is ACTIVE
- **Book Service**: to confirm and update available copies


 Service Discovery
Service is registered with Eureka as:

```properties
spring.application.name=borrowing-service

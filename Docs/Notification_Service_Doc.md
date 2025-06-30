#  Notification Service

The **Notification Service** delivers alerts and system messages to library members. It handles due date reminders, overdue notices, and fine notifications by email, SMS, or in-app delivery. This service is triggered by other modules like Borrowing and Fine and logs all sent messages for traceability.

---

##  Functional Scope

- Accept notification requests from other services
- Log and store notifications for each member
- Enable member-specific notification retrieval
- Future-proofed for email/SMS integrations
- Trigger alerts based on due dates, fines, and account status

---

##  Technology Stack

| Layer              | Technology             |
|--------------------|------------------------|
| Language           | Java 17+               |
| Framework          | Spring Boot 3.x        |
| REST API Docs      | Springdoc OpenAPI      |
| ORM & Persistence  | Spring Data JPA        |
| Database           | MySQL                  |
| Service Discovery  | Eureka                 |
| Messaging *(opt)*  | Kafka / RabbitMQ       |

---

##  REST API Endpoints

| Method | Endpoint                          | Description                        |
|--------|-----------------------------------|------------------------------------|
| POST   | `/api/notifications/send`         | Send and log a new notification    |
| GET    | `/api/notifications/{memberId}`   | Fetch notifications for a member   |



## Data Model

### `Notification` Entity

| Field           | Type      | Description                          |
|------------------|-----------|--------------------------------------|
| notificationId   | BIGINT    | Primary key                          |
| memberId         | BIGINT    | FK to `Member`                       |
| message          | TEXT      | Notification content                 |
| dateSent         | DATETIME  | Timestamp of delivery                |

---

##  Service Layer

Responsibilities:
- Accept incoming notification requests
- Generate formatted messages (e.g. “Book due tomorrow”)
- Store notifications for viewing by members
- (Optional) Interface with external message services (email/SMS)

---

##  Controller Layer

Responsibilities:
- Receive requests from internal services
- Return notification history by member
- Log alerts with date and message text

---

##  Sample Payload

```json
{
  "memberId": 101,
  "message": "Reminder: 'Atomic Habits' is due in 2 days."
}

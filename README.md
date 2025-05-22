# Library Management System (LMS)

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

## Technologies Used
- **Backend**: Spring Boot (Java)
- **Database**: MySQL/PostgreSQL (H2 for development)
- **ORM**: Hibernate/JPA
- **Testing**: JUnit, Mockito
- **API Documentation**: Swagger/OpenAPI
- **Logging**: SLF4J with Logback

---

## Folder Structure

```
LMS-Monolithic/
├── README.md                   # Project documentation
├── pom.xml                     # Maven configuration file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── lms/
│   │   │               ├── LmsApplication.java       # Main application entry point
│   │   │               ├── config/                   # Configuration files
│   │   │               ├── controller/               # REST API controllers
│   │   │               ├── entity/                   # JPA entities
│   │   │               ├── repository/               # Spring Data JPA repositories
│   │   │               └── service/                  # Service layer
│   │   └── resources/
│   │       ├── application.properties                # Application configuration
│   │       ├── data.sql                              # Sample data (optional)
│   │       └── schema.sql                            # Database schema (optional)
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── lms/
│                       ├── controller/               # Controller tests
│                       └── service/                  # Service tests
└── target/                                            # Compiled output and packaged application
    ├── LMS-1.0-SNAPSHOT.jar                          # Packaged JAR file
    ├── classes/                                       # Compiled classes
    ├── surefire-reports/                              # Test reports
```

## REST API Endpoints

### Book Management
- `GET /api/books`: Fetch all books.
- `GET /api/books/{id}`: Fetch a book by ID.
- `POST /api/books`: Add a new book.
- `PUT /api/books/{id}`: Update book details.
- `DELETE /api/books/{id}`: Delete a book.
- `GET /api/books/search?title={title}&author={author}`: Search books by title and/or author.

### Member Management
- `GET /api/members`: Fetch all members.
- `GET /api/members/{id}`: Fetch a member by ID.
- `POST /api/members`: Add a new member.
- `PUT /api/members/{id}`: Update member details.
- `DELETE /api/members/{id}`: Delete a member.

### Borrowing Transactions
- `GET /api/transactions`: Fetch all transactions.
- `GET /api/transactions/{id}`: Fetch a transaction by ID.
- `POST /api/transactions`: Borrow a book.
- `PUT /api/transactions/return/{id}`: Return a borrowed book.

### Fines
- `GET /api/fines`: Fetch all fines.
- `GET /api/fines/{id}`: Fetch a fine by ID.
- `POST /api/fines`: Create a new fine.
- `POST /api/fines/pay/{id}`: Pay a fine.
- `GET /api/fines/member/{memberId}`: Fetch fines for a specific member.

### Notifications
- `GET /api/notifications`: Fetch all notifications.
- `GET /api/notifications/{id}`: Fetch a notification by ID.
- `POST /api/notifications`: Send a notification.

---

## Entity Properties

### Book
- `bookId` (Long): Unique identifier.
- `title` (String): Title of the book.
- `author` (String): Author of the book.
- `genre` (String): Genre of the book.
- `isbn` (String): ISBN number.
- `yearPublished` (int): Year of publication.
- `availableCopies` (int): Number of available copies.

### Member
- `memberId` (Long): Unique identifier.
- `name` (String): Name of the member.
- `email` (String): Email address.
- `phone` (String): Phone number.
- `address` (String): Address of the member.
- `membershipStatus` (Enum): Membership status (`ACTIVE`, `INACTIVE`).

### BorrowingTransaction
- `transactionId` (Long): Unique identifier.
- `book` (Book): Associated book.
- `member` (Member): Associated member.
- `borrowDate` (LocalDate): Date of borrowing.
- `returnDate` (LocalDate): Date of return.
- `status` (Enum): Transaction status (`BORROWED`, `RETURNED`).

### Fine
- `fineId` (Long): Unique identifier.
- `member` (Member): Associated member.
- `amount` (double): Fine amount.
- `status` (Enum): Fine status (`PAID`, `PENDING`).
- `transactionDate` (LocalDate): Date of fine creation.

### Notification
- `notificationId` (Long): Unique identifier.
- `member` (Member): Associated member.
- `message` (String): Notification message.
- `dateSent` (LocalDate): Date the notification was sent.

---

## Database Schema

### `books` Table
| Column            | Type        | Constraints          |
|--------------------|-------------|----------------------|
| `book_id`         | BIGINT      | Primary Key          |
| `title`           | VARCHAR(255)| Not Null             |
| `author`          | VARCHAR(255)| Not Null             |
| `genre`           | VARCHAR(255)|                      |
| `isbn`            | VARCHAR(255)| Unique               |
| `year_published`  | INT         |                      |
| `available_copies`| INT         |                      |

### `members` Table
| Column              | Type        | Constraints          |
|----------------------|-------------|----------------------|
| `member_id`         | BIGINT      | Primary Key          |
| `name`              | VARCHAR(255)| Not Null             |
| `email`             | VARCHAR(255)| Unique               |
| `phone`             | VARCHAR(255)|                      |
| `address`           | VARCHAR(255)|                      |
| `membership_status` | VARCHAR(255)| Enum (`ACTIVE`, `INACTIVE`) |

### `borrowing_transactions` Table
| Column            | Type        | Constraints          |
|--------------------|-------------|----------------------|
| `transaction_id`  | BIGINT      | Primary Key          |
| `book_id`         | BIGINT      | Foreign Key (`books`)|
| `member_id`       | BIGINT      | Foreign Key (`members`)|
| `borrow_date`     | DATE        |                      |
| `return_date`     | DATE        |                      |
| `status`          | VARCHAR(255)| Enum (`BORROWED`, `RETURNED`) |

### `fines` Table
| Column            | Type        | Constraints          |
|--------------------|-------------|----------------------|
| `fine_id`         | BIGINT      | Primary Key          |
| `member_id`       | BIGINT      | Foreign Key (`members`)|
| `amount`          | DOUBLE      |                      |
| `status`          | VARCHAR(255)| Enum (`PAID`, `PENDING`) |
| `transaction_date`| DATE        |                      |

### `notifications` Table
| Column            | Type        | Constraints          |
|--------------------|-------------|----------------------|
| `notification_id` | BIGINT      | Primary Key          |
| `member_id`       | BIGINT      | Foreign Key (`members`)|
| `message`         | VARCHAR(255)|                      |
| `date_sent`       | DATE        |                      |

---

## Prerequisites
- Java 17 or higher
- Maven 3.8+
- MySQL/PostgreSQL database

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd LMS
   ```
2. Configure the database in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lmsdb
   spring.datasource.username=<your-username>
   spring.datasource.password=<your-password>
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the API documentation at `http://localhost:8080/swagger-ui.html`.

## Testing
Run the tests using Maven:
```bash
mvn test
```

## Advanced Features
- **API Endpoints**: Comprehensive RESTful APIs for managing books, members, transactions, fines, and notifications.
- **Security**: Configured with Spring Security for authentication and authorization.
- **Logging**: Centralized logging using SLF4J and Logback, with separate logs for development and production environments.
- **Database Schema**: Automatically managed by Hibernate, with support for schema updates.

## Deployment
1. **Build the Application**:
   ```bash
   mvn clean package
   ```
   This generates a JAR file in the `target/` directory.

2. **Run the Application**:
   ```bash
   java -jar target/LMS-1.0-SNAPSHOT.jar
   ```

3. **Environment Variables**:
   Set the following environment variables for production:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export DATABASE_URL=jdbc:mysql://<production-db-url>:3306/lmsdb
   export DATABASE_USERNAME=<your-username>
   export DATABASE_PASSWORD=<your-password>
   ```

4. **Monitor Logs**:
   Logs are stored in the `logs/` directory. Use the following command to view logs in real-time:
   ```bash
   tail -f logs/lms-production.log
   ```

## Contributing
Contributions are welcome! Please see the `CONTRIBUTING.md` file for guidelines.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

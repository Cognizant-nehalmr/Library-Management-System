# Library Management System (LMS)

## Overview
The Library Management System (LMS) is a RESTful API-based backend application designed to manage book collections, member registrations, borrowing and returning of books, and overdue tracking. It is built using Spring Boot and supports relational databases like MySQL and PostgreSQL.

## Features
- **Book Management**: Add, update, delete, and search books.
- **Member Management**: Register and manage library members.
- **Borrowing and Return**: Track book borrowing and return processes.
- **Overdue and Fines**: Monitor overdue books and calculate fines.
- **Notifications**: Send alerts for due dates and fines.

## Technologies Used
- **Backend**: Spring Boot (Java)
- **Database**: MySQL/PostgreSQL (H2 for development)
- **Testing**: JUnit, Mockito
- **API Documentation**: Swagger/OpenAPI

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
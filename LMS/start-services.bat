@echo off

:: Start Eureka Server
start cmd /k "cd eureka-server && mvn spring-boot:run"

:: Start Book Service
start cmd /k "cd book-service && mvn spring-boot:run"

:: Start Member Service
start cmd /k "cd member-service && mvn spring-boot:run"

:: Start Borrowing Transaction Service
start cmd /k "cd borrowing-transaction-service && mvn spring-boot:run"

:: Start Fine Service
start cmd /k "cd fine-service && mvn spring-boot:run"

:: Start Notification Service
start cmd /k "cd notification-service && mvn spring-boot:run"

:: Start API Gateway
start cmd /k "cd api-gateway && mvn spring-boot:run"

@echo All services are starting...
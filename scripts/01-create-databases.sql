-- Create databases for each microservice
CREATE DATABASE IF NOT EXISTS book_service_db;
CREATE DATABASE IF NOT EXISTS member_service_db;
CREATE DATABASE IF NOT EXISTS transaction_service_db;
CREATE DATABASE IF NOT EXISTS fine_service_db;
CREATE DATABASE IF NOT EXISTS notification_service_db;

-- Create tables for Book Service
USE book_service_db;

CREATE TABLE books (
    book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    year_published INT,
    available_copies INT DEFAULT 0,
    total_copies INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create tables for Member Service
USE member_service_db;

CREATE TABLE members (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    membership_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create tables for Transaction Service
USE transaction_service_db;

CREATE TABLE borrowing_transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create tables for Fine Service
USE fine_service_db;

CREATE TABLE fines (
    fine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'PAID') DEFAULT 'PENDING',
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_date TIMESTAMP NULL
);

-- Create tables for Notification Service
USE notification_service_db;

CREATE TABLE notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    type ENUM('DUE_REMINDER', 'OVERDUE_ALERT', 'FINE_NOTICE', 'WELCOME', 'BOOK_AVAILABLE', 'MEMBERSHIP_EXPIRY') NOT NULL,
    status ENUM('PENDING', 'SENT', 'FAILED', 'RETRY') DEFAULT 'PENDING',
    date_sent TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    recipient_email VARCHAR(255),
    subject VARCHAR(255),
    retry_count INT DEFAULT 0,
    error_message TEXT
);

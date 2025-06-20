-- Seed data for testing with Indian and global datasets
USE book_service_db;

INSERT INTO books (title, author, genre, isbn, year_published, available_copies, total_copies) VALUES
('The White Tiger', 'Aravind Adiga', 'Fiction', '978-1-4165-6259-7', 2008, 5, 5),
('Midnight''s Children', 'Salman Rushdie', 'Historical Fiction', '978-0-224-01864-2', 1981, 3, 4),
('The Alchemist', 'Paulo Coelho', 'Adventure', '978-0-06-112241-5', 1988, 6, 6),
('Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 'Non-Fiction', '978-0-06-231609-7', 2011, 4, 5),
('The Kite Runner', 'Khaled Hosseini', 'Drama', '978-1-59448-000-3', 2003, 2, 3);

USE member_service_db;

INSERT INTO members (name, email, phone, address, membership_status) VALUES
('Ravi Kumar', 'ravi.kumar@email.com', '+91-9876543210', 'Mumbai, Maharashtra, India', 'ACTIVE'),
('Priya Sharma', 'priya.sharma@email.com', '+91-9123456789', 'Delhi, India', 'ACTIVE'),
('Liam Smith', 'liam.smith@email.com', '+1-555-0201', 'New York, USA', 'ACTIVE'),
('Emma Johnson', 'emma.johnson@email.com', '+44-20-7946-0958', 'London, UK', 'INACTIVE'),
('Wei Zhang', 'wei.zhang@email.com', '+86-10-88888888', 'Beijing, China', 'ACTIVE');

USE notification_service_db;

INSERT INTO notifications (member_id, message, type, status, recipient_email, subject) VALUES
(1, 'Welcome to Library Management System! Your account has been created successfully.', 'WELCOME', 'SENT', 'ravi.kumar@email.com', 'Welcome to Library Management System'),
(2, 'Your borrowed book "The White Tiger" is due in 2 days. Please return it on time.', 'DUE_REMINDER', 'SENT', 'priya.sharma@email.com', 'Book Due Reminder'),
(3, 'Your book "The Alchemist" is overdue by 3 days. Please return it immediately.', 'OVERDUE_ALERT', 'PENDING', 'liam.smith@email.com', 'Overdue Book Alert');
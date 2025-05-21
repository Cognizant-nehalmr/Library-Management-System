-- Insert sample data into the Book table
INSERT INTO books (title, author, genre, isbn, year_published, available_copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', '9780743273565', 1925, 5),
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', '9780061120084', 1960, 3),
('1984', 'George Orwell', 'Dystopian', '9780451524935', 1949, 4);

-- Insert sample data into the Member table
INSERT INTO members (name, email, phone, address, membership_status) VALUES
('John Doe', 'john.doe@example.com', '1234567890', '123 Main St, Springfield', 'ACTIVE'),
('Jane Smith', 'jane.smith@example.com', '0987654321', '456 Elm St, Springfield', 'ACTIVE'),
('Alice Johnson', 'alice.johnson@example.com', '555-123-4567', '789 Pine Road, Springfield', 'INACTIVE');

-- Insert sample data into the BorrowingTransaction table
INSERT INTO borrowing_transactions (book_id, member_id, borrow_date, return_date, status) VALUES
(1, 1, '2025-05-01', NULL, 'BORROWED'),
(2, 2, '2025-04-15', '2025-05-10', 'RETURNED');

-- Insert sample data into the Fine table
INSERT INTO fines (member_id, amount, transaction_date, status) VALUES
(1, 10.00, '2025-05-15', 'PENDING'),
(2, 5.00, '2025-05-10', 'PAID');

-- Insert sample data into the Notification table
INSERT INTO notifications (member_id, message, date_sent) VALUES
(1, 'Your book is overdue. Please return it.', '2025-05-18'),
(2, 'Your fine has been paid. Thank you.', '2025-05-10');
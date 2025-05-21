-- Create the Book table
CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    year_published INT,
    available_copies INT NOT NULL
);

-- Create the Member table
CREATE TABLE IF NOT EXISTS members (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    membership_status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'
);

-- Create the BorrowingTransaction table
CREATE TABLE IF NOT EXISTS borrowing_transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    status ENUM('BORROWED', 'RETURNED') DEFAULT 'BORROWED',
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

-- Create the Fine table
CREATE TABLE IF NOT EXISTS fines (
    fine_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PAID', 'PENDING') DEFAULT 'PENDING',
    transaction_date DATE NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

-- Create the Notification table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    date_sent DATE NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);
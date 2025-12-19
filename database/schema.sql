-- Splitwise Database Schema
-- Database Name: splitwise_db

USE splitwise_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Groups table
CREATE TABLE IF NOT EXISTS `groups` (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Group Members (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS group_members (
    group_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_group_id (group_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id VARCHAR(50) PRIMARY KEY,
    description VARCHAR(500),
    amount DECIMAL(10, 2) NOT NULL,
    paid_by_user_id VARCHAR(50) NOT NULL,
    group_id VARCHAR(50) NOT NULL,
    split_type ENUM('EQUAL', 'EXACT', 'PERCENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paid_by_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE,
    INDEX idx_paid_by (paid_by_user_id),
    INDEX idx_group_id (group_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Splits table (for expense splits)
CREATE TABLE IF NOT EXISTS splits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expense_id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    value DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_expense_id (expense_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY unique_expense_user (expense_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Balances table (for tracking who owes whom)
CREATE TABLE IF NOT EXISTS balances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    debtor_user_id VARCHAR(50) NOT NULL,
    creditor_user_id VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (debtor_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (creditor_user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_debtor (debtor_user_id),
    INDEX idx_creditor (creditor_user_id),
    UNIQUE KEY unique_debtor_creditor (debtor_user_id, creditor_user_id),
    CHECK (debtor_user_id != creditor_user_id),
    CHECK (amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Settlements table (for tracking settlement transactions)
CREATE TABLE IF NOT EXISTS settlements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    from_user_id VARCHAR(50) NOT NULL,
    to_user_id VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    settled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_from_user (from_user_id),
    INDEX idx_to_user (to_user_id),
    INDEX idx_settled_at (settled_at),
    CHECK (from_user_id != to_user_id),
    CHECK (amount > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


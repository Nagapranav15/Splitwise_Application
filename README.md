# Expense-Sharing-Application

A console-based Java application that simulates an expense sharing system similar to Splitwise. This application allows users to create groups, add expenses with different split types, track balances, and settle dues.

## Features

- **User Management**: Create and manage users with unique IDs
- **Group Management**: Create groups and add multiple users
- **Expense Management**: Support for three split types:
  - **EQUAL**: Split amount equally among participants
  - **EXACT**: Specify exact amounts per user
  - **PERCENT**: Split based on percentages
- **Balance Tracking**: Track who owes whom with automatic balance simplification
- **Settlement**: Full or partial settlement of dues between users
- **Edge Case Handling**: Validates invalid splits and prevents over-settlement

## Technology Stack

- **Language**: Java 8+
- **Database**: MySQL (optional - schema provided)
- **Architecture**: Object-Oriented Design with SOLID principles

## Project Structure

```
src/
├── splitwise/
│   ├── Main.java                    # Main application entry point
│   ├── model/                       # Domain entities
│   │   ├── User.java
│   │   ├── Group.java
│   │   ├── Expense.java
│   │   ├── Split.java               # Abstract base class
│   │   ├── EqualSplit.java
│   │   ├── ExactSplit.java
│   │   ├── PercentSplit.java
│   │   └── SplitType.java           # Enum for split types
│   └── service/                     # Business logic services
│       ├── UserService.java
│       ├── GroupService.java
│       ├── ExpenseService.java
│       └── BalanceService.java     # Handles balance simplification
database/
└── schema.sql                       # MySQL database schema
```

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- MySQL (optional, for database integration)

### Compilation

```bash
javac -source 8 -target 8 -d out -sourcepath src src/splitwise/Main.java
```

### Running the Application

```bash
java -cp out splitwise.Main
```

## Database Setup (Optional)

The project includes a MySQL schema file for future database integration:

**Database Name**: `splitwise_db`

**Connection Details**:
- Database: `splitwise_db`

To set up the database:

```bash
mysql -u root -p < database/schema.sql
```

This uses the MySQL user `root`. You will be prompted for your password.

## Usage Example

The application demonstrates:

1. Creating users (Alice, Bob, Charlie, Diana)
2. Creating a group and adding members
3. Adding expenses with different split types:
   - Hotel booking: $400 (EQUAL split)
   - Restaurant: $150 (EXACT split)
   - Taxi: $200 (PERCENT split)
4. Viewing balances (simplified and per-user)
5. Settling dues between users
6. Error handling for invalid inputs

## Design Principles

- **SOLID Principles**: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- **Object-Oriented Design**: Clear separation of concerns between models and services
- **Precision**: Uses `BigDecimal` for financial calculations to avoid rounding errors
- **Balance Simplification**: Minimizes the number of transactions needed to settle all dues

## Key Components

### Models
- **User**: Represents a user with unique ID and name
- **Group**: Represents a group containing multiple users
- **Expense**: Represents an expense with amount, paid-by user, participants, and split type
- **Split**: Abstract base class for different split types

### Services
- **UserService**: Manages user creation and retrieval
- **GroupService**: Manages group creation and membership
- **ExpenseService**: Handles expense creation and validation
- **BalanceService**: Tracks balances and simplifies transactions

## License

This project is open source and available for educational purposes.


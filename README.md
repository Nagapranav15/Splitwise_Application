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
- **Database**: MySQL 8.0+ (with JDBC driver)
- **Build Tool**: Maven (for dependency management)
- **Architecture**: Layered Architecture with Repository Pattern
- **Design Patterns**: Singleton, Factory, Repository

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
│   ├── service/                     # Business logic services
│   │   ├── UserService.java
│   │   ├── GroupService.java
│   │   ├── ExpenseService.java
│   │   └── BalanceService.java      # Handles balance simplification
│   ├── repository/                  # Data access layer
│   │   ├── UserRepository.java      # Repository interface
│   │   ├── GroupRepository.java     # Repository interface
│   │   └── impl/                    # Repository implementations
│   │       ├── UserRepositoryImpl.java
│   │       └── GroupRepositoryImpl.java
│   └── db/                          # Database utilities
│       ├── DbConfig.java            # Database configuration
│       ├── DbConnection.java        # Connection management
│       └── DbInitializer.java       # Database initialization
├── resources/
│   └── db.properties.example        # Example database config
database/
└── schema.sql                       # MySQL database schema
```

## Getting Started

### Prerequisites
- Java JDK 8 or higher
- MySQL Server 8.0+
- Maven (recommended) or manual JDBC driver setup

### Build and Run with Maven
```bash
# Clone the repository
git clone https://github.com/Nagapranav15/Splitwise_Application.git
cd Splitwise_Application

# Build the project
mvn clean package

# Run the application
java -cp "target/classes:target/dependency/*" splitwise.Main
```

### Manual Compilation and Execution
```bash
# Compile
javac -cp "lib/mysql-connector-j-8.x.x.jar" -d out -sourcepath src src/splitwise/Main.java

# Run
java -cp "out:lib/mysql-connector-j-8.x.x.jar" splitwise.Main
```

## Database Setup

### Prerequisites
- MySQL Server 8.0+ installed and running
- MySQL user with appropriate privileges

### 1. Create Database
```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS splitwise_db;"
```

### 2. Import Schema
```bash
mysql -u root -p splitwise_db < database/schema.sql
```

### 3. Configure Database Connection
1. Copy the example configuration file:
   ```bash
   cp db.properties.example db.properties
   ```
2. Edit `db.properties` with your MySQL credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/splitwise_db?useSSL=false&serverTimezone=UTC
   db.username=your_username
   db.password=your_password
   ```

### 4. Add MySQL JDBC Driver
Download the MySQL Connector/J and add it to your classpath:
```bash
# For Maven, add to pom.xml:
# <dependency>
#     <groupId>mysql</groupId>
#     <artifactId>mysql-connector-java</artifactId>
#     <version>8.0.27</version>
# </dependency>

# For manual setup, download from:
# https://dev.mysql.com/downloads/connector/j/
```

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
- **Layered Architecture**:
  - Presentation Layer: `Main` class for console I/O
  - Service Layer: Business logic in `service` package
  - Repository Layer: Data access in `repository` package
  - Domain Layer: Core business entities in `model` package
- **Repository Pattern**: Abstract data access layer for database operations
- **Dependency Injection**: Manual dependency injection for better testability

## Security Considerations

- Database credentials are stored in `db.properties` (not version controlled)
- Example configuration is provided in `db.properties.example`
- Always use environment variables or secure vaults for production credentials
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


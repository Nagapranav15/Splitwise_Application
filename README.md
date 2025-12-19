# 💰 Splitwise Clone - Expense Sharing Made Easy

Tired of awkward money conversations with friends? This Splitwise clone takes the hassle out of splitting bills and tracking shared expenses. Whether you're splitting rent, dinner bills, or travel costs, this application makes it simple to track who owes what.

## ✨ Key Features

- **Multiple Split Options**
  - 🟢 **Equal Split** - Split the bill evenly
  - 🔢 **Exact Amounts** - Specify who pays what
  - 📊 **Percentage Split** - Divide by custom percentages
- **Group Expenses** - Perfect for trips, roommates, or team lunches
- **Real-time Balances** - Instantly see who's up and who's down
- **Settle Up** - Mark expenses as paid with a single click
- **Persistent Storage** - Your data is safe with MySQL

## 🛠 Tech Stack

- **Backend**: Java 8+
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Architecture**: Layered (MVC + Repository Pattern)

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- MySQL Server 8.0+
- Maven (recommended)

### One-Command Setup (Maven)
```bash
git clone https://github.com/Nagapranav15/Splitwise_Application.git
cd Splitwise_Application
mvn clean package
java -cp "target/classes:target/dependency/*" splitwise.Main
```

## 🏗 Project Structure

```
src/
├── splitwise/
│   ├── model/       # 🧑‍💼 Core business models
│   ├── service/     # 🛠 Business logic
│   ├── repository/  # 💾 Data access layer
│   └── db/          # 🔌 Database utilities
├── database/        # 🗃 SQL schema
└── resources/       # ⚙️  Configuration
```

## ⚙️ Configuration

1. **Set up your database**:
   ```bash
   mysql -u root -p < database/schema.sql
   ```

2. **Configure your credentials**:
   ```bash
   cp db.properties.example db.properties
   # Edit db.properties with your MySQL credentials
   ```

3. **Add MySQL JDBC Driver**:
   - For Maven users: Already included in `pom.xml`
   - Manual setup: Download from [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

## 🎮 Demo

```
=== Splitwise Clone ===

1. Creating users...
   ✅ Alice, Bob, Charlie, and Diana joined

2. Adding expenses...
   🏨 Hotel: $400 (EQUAL split)
   🍽️ Dinner: $150 (EXACT split)
   🚖 Taxi: $200 (PERCENT split)

3. Viewing balances...
   Bob owes Alice: $50.00
   Charlie owes Bob: $30.00
   Diana owes Alice: $100.00
```

## 🤝 Contributing

Found a bug or have an idea? We'd love your help!
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Made with ❤️ by Nagapranav - because splitting bills should be easy, not awkward!

### Services
- **UserService**: Manages user creation and retrieval
- **GroupService**: Manages group creation and membership
- **ExpenseService**: Handles expense creation and validation
- **BalanceService**: Tracks balances and simplifies transactions

## License

This project is open source and available for educational purposes.


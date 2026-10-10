# FinPilot AI Backend 🤖💰

FinPilot AI helps users track their income and expenses, manage budgets and financial goals, understand spending patterns, and receive AI-generated financial insights.

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [API Endpoints](#-api-endpoints)
- [Sample Responses](#-sample-responses)
- [Configuration](#-configuration)
- [Getting Started](#-getting-started)
- [Testing](#-testing)
- [Future Improvements](#-future-improvements)
- [Author](#-author)

---

## 🚀 Overview

FinPilot AI is a backend application designed to help users organize their personal finances in one place. It provides REST APIs for managing financial records and calculating useful summaries. JWT-based authentication helps protect endpoints and keeps financial data associated with the authenticated user.

The project also includes a Gemini AI advice endpoint intended to generate financial insights from relevant user data.

> **Note:** AI advice is informational and should not be treated as professional financial, tax, or investment advice.

---

## ✨ Features

### 🔐 Authentication and Security

- User registration and login
- JWT-based authentication
- BCrypt password hashing
- Protected REST APIs
- User-specific data access

### 💸 Expense Management

- Add, view, update, and delete expenses
- Organize expenses by category
- Generate category-based spending summaries

### 💰 Income Management

- Add, view, update, and delete income records

### 🎯 Financial Goal Management

- Create, update, and delete financial goals
- Track current savings against a target
- Calculate goal completion percentage

**Example**

| Metric | Value |
|---|---:|
| Target amount | ₹120,000 |
| Current savings | ₹59,500 |
| Goal progress | 49.58% |

### 📊 Budget Management

- Create monthly budgets
- Track spending against a budget
- Calculate the remaining budget

**Example**

| Metric | Value |
|---|---:|
| Monthly budget | ₹20,000 |
| Amount spent | ₹500 |
| Remaining budget | ₹19,500 |

### 📈 Dashboard and Reports

- Summarize total income, expenses, and savings
- View goal progress and budget usage
- Generate expense totals by category
- Identify the top spending category

### 🤖 Gemini AI Insights

- AI advice endpoint configured to use Google Gemini
- Generate financial insights using relevant income, expense, budget, savings, and goal information
- Potential to expand into spending recommendations and forecasting

---

## 🛠 Tech Stack

| Area | Technologies |
|---|---|
| Language | Java 24 |
| Backend Framework | Spring Boot |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL |
| Build Tool | Maven |
| AI Integration | Google Gemini AI |
| API Testing | Postman |
| Development Tools | IntelliJ IDEA, Git, GitHub |

---

## 📂 Project Structure

```text
finpilot-ai-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       ├── config/
│   │   │       ├── security/
│   │   │       └── exception/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

*The package names above are illustrative; adjust them to match the actual folders in your repository.*

---

## 🔌 API Endpoints

The following report endpoints are documented in this project overview. Confirm the exact paths and HTTP methods against your controller mappings.

| Feature | Endpoint | Purpose |
|---|---|---|
| Expense category report | `GET /reports/categories` | Returns expense totals grouped by category |
| Top spending category | `GET /reports/top-category` | Returns the category with the highest spending |
| AI financial advice | Check the AI advice controller mapping | Generates AI-based financial insights |

Other authentication, income, expense, budget, goal, and dashboard endpoints depend on the mappings implemented in the controllers.

---

## 🧾 Sample Responses

### Dashboard

```json
{
  "totalIncome": 60000,
  "totalExpense": 500,
  "savings": 59500
}
```

### Enhanced Dashboard

```json
{
  "totalIncome": 60000,
  "totalExpense": 500,
  "savings": 59500,
  "goalProgress": 49.58,
  "monthlyBudget": 20000,
  "remainingBudget": 19500
}
```

### Expense Category Report

`GET /reports/categories`

```json
[
  {
    "category": "Food",
    "totalAmount": 500
  }
]
```

### Top Spending Category

`GET /reports/top-category`

```json
{
  "category": "Food",
  "amount": 500
}
```

### AI Insight

```json
{
  "message": "Excellent! You have saved ₹59500 and completed 49.58% of your goal."
}
```

*These are illustrative examples, not guaranteed live API responses. Actual field names and values depend on your DTOs and application data.*

---

## ⚙️ Configuration

### 1. Prerequisites

Install the following before running the project:

- Java 24 (or the Java version configured in `pom.xml`)
- Maven
- MySQL Server
- A Google Gemini API key for AI features
- Git

### 2. Create a MySQL Database

Create a database in MySQL. For example:

```sql
CREATE DATABASE finpilot_ai;
```

Use the database name and credentials that match your application configuration.

### 3. Configure Environment Variables

Set the required values in your operating system or IDE run configuration. Example variable names:

```text
DB_URL=jdbc:mysql://localhost:3306/finpilot_ai
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key
```

Your `src/main/resources/application.properties` can reference them like this:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/finpilot_ai}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

gemini.api.key=${GEMINI_API_KEY}
```

Make sure the property names match the ones used by your application. If your project uses different database or Gemini property names, update these examples accordingly.

**Security reminder:** Never commit API keys, database passwords, JWT secrets, or other credentials to GitHub. Keep real secrets in environment variables and add local secret files to `.gitignore`.

---

## ▶️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ShubhamChougule13/finpilot-ai-backend.git
```

### 2. Open the Project Directory

```bash
cd finpilot-ai-backend
```

### 3. Configure the Database and Environment Variables

Create the database and set the required environment variables described above.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application is expected to start at:

```text
http://localhost:8080
```

This URL assumes the application uses Spring Boot's default port. If `server.port` is configured differently, use that port instead.

---

## 🧪 Testing

Use Postman or another API client to test the REST endpoints.

Suggested test flow:

1. Register a user.
2. Log in and obtain a JWT.
3. Send the token in the `Authorization` header for protected endpoints:

   ```text
   Authorization: Bearer <your_jwt_token>
   ```

4. Create income and expense records.
5. Create a budget and a financial goal.
6. Check dashboard and report endpoints.
7. Test the AI advice endpoint after configuring the Gemini API key.

Verify that users can access only their own financial records.

---

## 🗺️ Future Improvements

- Complete and strengthen Gemini AI integration
- Add Swagger/OpenAPI documentation
- Add automated unit and integration tests
- Add Docker support
- Deploy the backend to a cloud platform
- Add email notifications
- Build a React frontend
- Add expense trend visualizations
- Add goal achievement forecasting and budget optimization suggestions

---

## 👨‍💻 Author

**Shubham Chougule**

- GitHub: [ShubhamChougule13](https://github.com/ShubhamChougule13)
- Repository: [FinPilot AI Backend](https://github.com/ShubhamChougule13/finpilot-ai-backend)

---

## ⭐ Support

If you find this project useful, consider giving the repository a ⭐ on GitHub.

**FinPilot AI — Smart Finance Management with AI 🚀💰🤖**

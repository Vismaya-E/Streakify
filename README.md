Streakify - Habit Tracking MVP
Help people build life-changing habits through streak psychology and smart tracking.

1. Setup Instructions
   Prerequisites
   Java 17

Maven 3.x

PostgreSQL 15+

Database Setup
Open your PostgreSQL terminal or pgAdmin.

Create the database: CREATE DATABASE streakify_db;.

The tables will be automatically created on the first run due to ddl-auto=update.

Running the Application
Navigate to the root directory and run:

Bash
./mvnw spring-boot:run
The server will start at http://localhost:8080.

2. API Endpoints
   User Management
   POST /users: Register a new user.

GET /users/{id}: View user profile.

Habit Management
POST /habits: Create a new habit.

GET /users/{userId}/habits: View all habits for a specific user.

Habit Logs & Streaks
POST /habits/{habitId}/logs: Log habit completion (Automatic User Identification).

GET /habits/{habitId}/logs: View logs in descending order.

GET /users/{userId}/dashboard: View productivity insights and consistency score.

3. Business Rules Implemented
   Validation: Prevents logging for future dates or dates before a habit was created.

Duplicates: Restricts users to one log per habit per day.

Ownership: Automatically links logs to the correct habit owner.

4. Technical Stack
   Backend: Spring Boot 4.0.2.

Database: PostgreSQL.

Tools: Lombok for clean code and Jakarta Validation for data integrity.
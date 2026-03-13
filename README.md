
# Streakify Backend

Streakify is a habit-tracking application designed to help users build life-changing habits using streak psychology, smart tracking, and productivity insights.
This repository contains the backend implementation for Version 1.0 MVP, built with Spring Boot and PostgreSQL, and tested via Postman.

## Tech Stack
- Java
- Spring Boot
- PostgreSQL
- JPA / Hibernate
- Maven
- Postman

---

## Setup Steps

1. Clone the repository


      git clone https://github.com/Vismaya-E/Streakify
      
      cd streakify

2. Configure Database


   Create a PostgreSQL database named:

      CREATE DATABASE streakify_db;


   Update application.properties:

      spring.datasource.url=jdbc:postgresql://localhost:5432/streakify_db
      spring.datasource.username=postgres
      spring.datasource.password=yourpassword


3. Run the application


      ./mvnw spring-boot:run
   
   Server runs on:
   http://localhost:8080



## Database Schema

   users table

      id (PK)
      
      name
      
      email (unique)
      
      created_at

   habits table

      
      id (PK)
      
      name
      
      target_days_per_week
      
      user_id (FK → users.id)
      
      created_at

   habit_logs table
   
      id (PK)
      
      habit_id (FK → habits.id)
      
      log_date
      
      completed (boolean)
      
      Unique constraint: (habit_id, log_date)

## API Endpoints
   
   Users

      POST /users  
      GET /users/{id}  
      DELETE /users/{id}
   
   Habits

      POST /habits  
      GET /users/{userId}/habits  
      DELETE /habits/{id}
   
   Habit Logs

      POST /habits/{habitId}/logs  
      PUT /habits/{habitId}/logs/{date}  
      GET /habits/{habitId}/logs
   
   Streak

      GET /habits/{habitId}/streak
   
   Dashboard

      GET /users/{userId}/dashboard

---

## Sample Request and Response

 1.Create User 

Request:


       {"name": "Vismaya", "email": "vismaya@example.com"}


Response:
   
       
       {"id": 1, "name": "Vismaya", "email": "vismaya@example.com" ,"createdAt": "2026-03-12T16:58:33.438324"}



 2.Create Habit

Request:

    {"name": "Morning Workout", "target_days_per_week": 5, "userId": 1} 
Response: 

    {"id": 10, "name": "Morning Workout", "target_days_per_week": 5, "userId": 1,"createdAt": "2026-03-12 21:27:59"}

 3.Productivity Dashboard (GET)
 
    Response:

        {
            "totalHabits": 4,
            "activeHabits": 3,
            "completedToday": 2,
            "currentStreaks": [...],
            "consistencyScore": 82
            }
---

## Screenshots

### Create User
![Create User](screenshot/UserCreate.png)

### Get User
![View User](screenshot/UserView.png)

### Delete User
![Delete User](screenshot/UserDelete.png)

---

### Create Habit
![Create Habit](screenshot/HabitCreate.png)

### Get User Habits
![Get User Habits](screenshot/HabitView.png)

### Delete Habits
![Duplicate Habit](screenshot/HabitDelete.png)

---

###  Add Log  Days

Add Log 
![Adding Log](screenshot/LogHabitAdd.png)

Update the Log
![Update Log](screenshot/LogHabitUpdate.png)

View Log Habits  
![View Log Habits ](screenshot/LogHabitView.png)

---

### Fetch Streak
![Fetch Streak](screenshot/Streak.png)

---

### Dashboard
![Dashboard](screenshot/Dashboard.png)

---

### Negative Cases

Duplicate Log  
![Duplicate Log](screenshot/Duplicate_Log.png)

Future Date Cannot Be Added  
![Future Date](screenshot/Log-Future.png)
Non Existing User  
![User Not Found](screenshot/non-existing user.png)

Invalid email
![User Not Found](screenshot/invalid email.png)
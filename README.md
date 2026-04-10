# Student Management System

A complete, production-ready full-stack web application built with **Spring Boot**, **Spring Security**, **Spring Data JPA**, **Thymeleaf**, and **Bootstrap 5**.

## 🚀 Features

- **Authentication & Authorization**: Login, Registration, Role-based access (ADMIN/USER)
- **Student CRUD**: Add, View, Edit, Delete students
- **Search**: Search students by name, email, or department
- **Pagination & Sorting**: Navigate through large datasets
- **Responsive UI**: Beautiful Bootstrap 5 design
- **Security**: BCrypt password encryption, CSRF protection

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2.5, Java 17 |
| Security | Spring Security 6 |
| ORM | Spring Data JPA, Hibernate |
| Frontend | Thymeleaf, Bootstrap 5, Bootstrap Icons |
| Database | H2 (default), MySQL (configurable) |
| Build | Maven |
| Utilities | Lombok, SLF4J |

## 📁 Project Structure

```
src/main/java/com/studentmgmt/
├── StudentManagementApplication.java   # Entry point
├── config/
│   ├── SecurityConfig.java             # Spring Security config
│   └── DataInitializer.java            # Sample data seeder
├── controller/
│   ├── AuthController.java             # Login & Registration
│   ├── MainController.java             # Dashboard & Home
│   └── StudentController.java          # Student CRUD
├── dto/
│   ├── StudentDto.java                 # Student data transfer object
│   └── UserRegistrationDto.java        # Registration form data
├── entity/
│   ├── Student.java                    # Student database entity
│   ├── User.java                       # User database entity
│   └── Role.java                       # Role database entity
├── exception/
│   ├── GlobalExceptionHandler.java     # Centralized error handling
│   └── ResourceNotFoundException.java  # Custom 404 exception
├── repository/
│   ├── StudentRepository.java          # Student database queries
│   ├── UserRepository.java             # User database queries
│   └── RoleRepository.java             # Role database queries
├── security/
│   └── CustomUserDetailsService.java   # Loads users for authentication
└── service/
    ├── StudentService.java             # Student service interface
    ├── StudentServiceImpl.java         # Student business logic
    ├── UserService.java                # User service interface
    └── UserServiceImpl.java            # User registration logic
```

## 🏃 How to Run

### Prerequisites
- **Java 17** or later ([Download](https://adoptium.net/))
- **Maven** ([Download](https://maven.apache.org/download.cgi)) or use the Maven wrapper

### Option 1: Run with H2 (No Database Setup Needed!)

```bash
# Clone or navigate to the project directory
cd student-management-system

# Run the application
mvn spring-boot:run

# OR on Windows with Maven wrapper:
mvnw.cmd spring-boot:run
```

The app starts at: **http://localhost:8080**

### Option 2: Run with MySQL

1. Install MySQL and create the database:
   ```sql
   CREATE DATABASE student_management_db;
   ```

2. Update `src/main/resources/application-mysql.properties`:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Change the active profile in `application.properties`:
   ```properties
   spring.profiles.active=mysql
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Running in IntelliJ IDEA

1. Open IntelliJ → **File → Open** → Select the project folder
2. Wait for Maven to download dependencies (bottom progress bar)
3. Open `StudentManagementApplication.java`
4. Click the green **▶ Run** button next to the `main` method
5. Open browser at **http://localhost:8080**

### Running in VS Code

1. Install the **Extension Pack for Java** extension
2. Open the project folder in VS Code
3. Open `StudentManagementApplication.java`
4. Click **Run** above the `main` method
5. Open browser at **http://localhost:8080**

## 🔐 Default Login Credentials

| Role | Email | Password |
|------|-------|----------|
| **Admin** | admin@admin.com | admin123 |
| **User** | user@user.com | user123 |

> **Admin** can do everything including **deleting students**.  
> **User** can view, add, and edit students, but **cannot delete**.

## 🏗️ Architecture Explained

### Layered Architecture Flow

```
Browser Request → Controller → Service → Repository → Database
                      ↕              ↕
                   DTOs          Entities
                      ↕
                  Thymeleaf
                  (HTML View)
```

1. **Controller Layer**: Receives HTTP requests, calls services, returns views
2. **Service Layer**: Contains business logic, converts between DTOs and Entities
3. **Repository Layer**: Talks to the database using Spring Data JPA
4. **Entity Layer**: Java classes that map to database tables
5. **DTO Layer**: Data carriers between controller and service (decoupled from entities)

### Security Flow

```
Login Form → Spring Security Filter → CustomUserDetailsService → Database
                                              ↕
                                     BCrypt Password Check
                                              ↕
                                    Success → Dashboard
                                    Failure → Login Page + Error
```

## 📝 License

This project is for educational purposes.

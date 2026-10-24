# Employee Management System

Built with Spring Boot, MySQL, Thymeleaf and Lombok.
Supports employee registration, listing, updating and deletion,
with input validation and duplicate email checks.


## Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL / XAMPP
- Thymeleaf
- Lombok
- Maven


## How to Run

1. Install JDK 21 and start MySQL.
2. Create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS employee_db;
   ```
3. Copy `secrets.properties.example` to `secrets.properties`
   beside `pom.xml`, then enter your database credentials.
4. Run from the project folder:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
5. Open http://localhost:8080/employees.

Local credentials in `secrets.properties` are excluded from Git.
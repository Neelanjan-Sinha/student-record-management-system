# Student Record Management System

A desktop application built using **Java Swing, JDBC, and Oracle Database** to manage student records through a graphical user interface.

## Overview

The Student Record Management System provides a simple interface for storing, searching, updating, deleting, and viewing student information. It connects to an Oracle Database using JDBC and displays records in a JTable.

This project demonstrates Java GUI development, database connectivity, SQL operations, and event handling.

## Features

- **Add Student:** Insert new student records into the database.
- **Search Student:** Find a student using their unique ID.
- **Update Student:** Modify existing student information.
- **Delete Student:** Remove student records with confirmation.
- **View All Students:** Display stored records in a JTable.
- **Clear Form:** Clear the input fields.
- **Database Integration:** Store records persistently in Oracle Database.
- **Input Validation:** Check required fields and numeric inputs.

## Technologies Used

- Java
- Java Swing
- JDBC (Java Database Connectivity)
- Oracle Database
- SQL
- JTable and DefaultTableModel

## Student Details

The application manages the following information:

| Field | Description |
|---|---|
| Student ID | Unique identifier for each student |
| Name | Student's full name |
| Age | Student's age |
| Course | Student's course |
| Email | Student's email address |

## Project Structure

```text
student-record-management-system/
├── StudentManagement.java
├── database.sql
├── README.md
└── .gitignore
```

## Prerequisites

Before running the application, make sure you have:

1. Java JDK installed.
2. Oracle Database installed and running, or access to an Oracle database server.
3. Oracle SQL Developer or another Oracle SQL client.
4. Oracle JDBC driver (ojdbc JAR).
5. A configured Oracle database user.

## Database Setup

1. Connect to your Oracle database.
2. Open a SQL worksheet.
3. Execute the SQL statements in `database.sql` to create the `STUDENTS` table.
4. Make sure the table exists in the schema used by your Java application.

## JDBC Configuration

Update the database connection details in `StudentManagement.java`:

```java
static final String URL =
    "jdbc:oracle:thin:@localhost:1521/XEPDB1";
static final String USER = "your_username";
static final String PASSWORD = "your_password";
```

Replace the sample username, password, host, port, and service name with the values for your Oracle database.

**Security note:** Never commit actual database passwords or other credentials to GitHub. For a public deployment, use environment variables or a secure configuration mechanism.

## How to Run

Place the Oracle JDBC driver in your project directory or configure it in your IDE.

### Windows

Compile the program:

```bash
javac -cp ".;ojdbc11.jar" StudentManagement.java
```

Run the application:

```bash
java -cp ".;ojdbc11.jar" StudentManagement
```

Replace `ojdbc11.jar` with the filename of your downloaded Oracle JDBC driver.

On Linux or macOS, use `:` instead of `;` in the classpath.

Ensure that Oracle Database is running and the JDBC connection details are correct before launching the application.

## How to Use

1. Launch the application.
2. Enter the student's ID, name, age, course, and email.
3. Click **Add** to insert a new record.
4. Enter a student ID and click **Search** to find a record.
5. Modify the fields and click **Update** to save changes.
6. Select or enter a student ID and click **Delete** to remove a record.
7. Click **View All** to display all student records.
8. Click **Clear** to reset the form.

## Learning Outcomes

This project demonstrates:

- Object-oriented programming in Java.
- GUI development using Java Swing.
- JDBC connection and SQL query execution.
- CRUD operations in a relational database.
- Event-driven programming with action listeners.
- Displaying database records using JTable.

## Future Improvements

- Add email format validation.
- Improve exception handling and user feedback.
- Use environment variables for database configuration.
- Add sorting and filtering to the student table.
- Introduce a separate database access layer.
- Improve the interface design.

## License

This project is available for educational and learning purposes. If you want others to reuse or modify it under defined terms, consider adding an open-source license such as the MIT License.

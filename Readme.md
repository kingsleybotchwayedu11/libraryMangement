# Library Management Lab

## Project Overview

The **Library Management Lab** is a system designed for librarians to efficiently manage library operations. It provides functionalities to handle resources, patrons, and transactions while maintaining an intuitive workflow for common library tasks.

---

## Features

1. **Library Resource Management**
   - Add new resources (books) to the library's inventory.

2. **Patron Management**
   - Register patrons and manage their library accounts.

3. **Book Borrowing**
   - Allow patrons to borrow available books.
   - Track borrowing transactions and update book availability.

4. **Book Reservation**
   - Enable patrons to reserve books that are currently unavailable.
   - Automatically notify the next patron in line when a book is returned.

5. **Book Returns**
   - Manage the return of borrowed books.
   - Update records and check for pending reservations.

6. **Search Functionality**
   - Search books by title or genre.
   - Retrieve book details using a unique ID.

7. **User Management**
   - Retrieve patron details using their membership information.

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher.
- **MySQL**: Ensure MySQL is installed and running on your system.
- **IDE or Text Editor**: To edit and run the Java files if needed.

---

## Database Configuration

1. Navigate to `library.utils.databaseOperations.DatabaseConnection`.
2. Update the database connection settings with your MySQL credentials:
   ```java
   // Example
   private static final String URL = "jdbc:mysql://localhost:3306/library";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";

## Running the Application
1. From the root directory, navigate to the App.java file.
2. Run the App.java file using your IDE or the command line to launch the program

## Note
1. Ensure the database is correctly configured before running the application.
2. All necessary entities are already created in the MySQL database.
3. Modify configuration settings based on your local setup, such as database URL, username, and password.

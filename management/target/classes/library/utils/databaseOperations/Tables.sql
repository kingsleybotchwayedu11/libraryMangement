set Foreign_key_checks = 0;
CREATE TABLE IF NOT EXISTS Book (
    id VARCHAR(50) PRIMARY KEY,           
    title VARCHAR(255),                      
    location VARCHAR(255),                   
    totalCopies INT,                          
    totalBorrowed INT DEFAULT 0,             
    author VARCHAR(255),                     
    isbn VARCHAR(20) UNIQUE,                 
    genre VARCHAR(50)                         
);

CREATE TABLE IF NOT EXISTS Patron (
    userId VARCHAR(50) PRIMARY KEY,          
    firstName VARCHAR(100),                  
    lastName VARCHAR(100),                    
    role VARCHAR(50),                         
    address VARCHAR(255),                    
    email VARCHAR(100),                       
    phoneNumber VARCHAR(20),                  
    libraryCardId VARCHAR(50) UNIQUE          
);

CREATE TABLE IF NOT EXISTS Librarian (
    userId VARCHAR(50) PRIMARY KEY,           
    firstName VARCHAR(100),                  
    lastName VARCHAR(100),                    -- Librarian's last name
    password VARCHAR(255),                    -- Librarian's password (stored securely, hashed)
    role VARCHAR(50),                         -- Role of the librarian (e.g., 'staff', 'admin')
    address VARCHAR(255),                     -- Librarian's address
    email VARCHAR(100),                       -- Librarian's email address
    phoneNumber VARCHAR(20),                  -- Librarian's phone number
    userName VARCHAR(50) UNIQUE               -- Unique username for librarian login
);

CREATE TABLE IF NOT EXISTS BorrowingTransaction (
    transactionId VARCHAR(50) PRIMARY KEY,       -- Unique identifier for the borrowing transaction
    borrowedResourceId VARCHAR(50),              -- The ID of the borrowed resource (book)
    patronMemberCardId VARCHAR(50),              -- Library card ID of the patron who borrowed the item
    issuedLibrarian VARCHAR(50),                 -- Librarian who issued the resource to the patron
    borrowedDate DATETIME,                           -- The DATETIME the item was borrowed
    expectedReturnDate DATETIME,                     -- The expected return DATETIME for the item
    isTransactionClosed ENUM("close", "overdue", "active") DEFAULT:"active",   -- Whether the transaction is closed or still active
    FOREIGN KEY (borrowedResourceId) REFERENCES Book(bookId),  -- Foreign key linking to Book table
    FOREIGN KEY (patronMemberCardId) REFERENCES Patron(libraryCardId),  -- Foreign key linking to Patron table
    FOREIGN KEY (issuedLibrarian) REFERENCES Librarian(userId)  -- Foreign key linking to Librarian table
);

CREATE TABLE IF NOT EXISTS CheckInTransaction (
    checkInId VARCHAR(50) PRIMARY KEY,            -- Unique identifier for the check-in transaction
    transactionId VARCHAR(50),                    -- The ID of the borrowing transaction being closed
    checkInDate DATETIME,                             -- The date the item was returned
    acceptedBy VARCHAR(50),                       -- The librarian who processed the check-in
    FOREIGN KEY (transactionId) REFERENCES BorrowingTransaction(transactionId),  -- Foreign key linking to BorrowingTransaction
    FOREIGN KEY (acceptedBy) REFERENCES Librarian(userId)  -- Foreign key linking to Librarian table
);

set Foreign_key_checks = 0;
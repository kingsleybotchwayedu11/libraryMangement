package library.controllers;

import library.model.LibraryResource.*;
import java.util.*;

public class BooksController {

    /**
     * Handles functions related to books in the library system.
     */

    /**
     * Uploads a new book to the library system.
     * 
     * @param title       The title of the book.
     * @param location    The physical location of the book in the library.
     * @param totalCopies The total number of copies of the book available.
     * @param author      The author of the book.
     * @param genre       The genre of the book.
     * @return A StatusReport indicating success or failure of the book upload operation.
     */
    public static StatusReport uploadBook(String title, String location, int totalCopies, String author, String genre) {
        // Create a new Book object with a unique ID.
        Book newBook = new Book(UUID.randomUUID().toString(), title, location, totalCopies, 0, author, genre.toLowerCase());

        // Save the new book to the database and return a StatusReport.
        return newBook.saveToDatabase() ? 
            new StatusReport(true, "Book saved successfully", newBook) : 
            new StatusReport(false, "Couldn't save book");
    }

    /**
     * Retrieves all books currently available in the library.
     * 
     * @return A list of all books in the library.
     */
    public static List<Book> getAvailableBooks() {
        return Book.getAllBooks();
    }

    /**
     * Retrieves all books that match a given title.
     * 
     * @param title The title of the book(s) to search for.
     * @return A list of books with the specified title.
     */
    public static List<Book> getAllBooksByTitle(String title) {
        return Book.findByAttribute("title", title);
    }

    /**
     * Retrieves all books belonging to a specific genre.
     * 
     * @param genre The genre of the book(s) to search for.
     * @return A list of books that belong to the specified genre.
     */
    public static List<Book> getAllBooksGenre(String genre) {
        return Book.findByAttribute("genre", genre);
    }

    /**
     * Retrieves a specific book by its unique ID.
     * 
     * @param id The unique identifier of the book.
     * @return The Book object corresponding to the specified ID, or null if not found.
     */
    public static Book getBookById(String id) {
        return Book.getById(id);
    }
}

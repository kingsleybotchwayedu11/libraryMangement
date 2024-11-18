package library.controllers;
import library.model.LibraryResource.*;
import java.util.*;

public class BooksController {
/** handles book functions*/
    //get all availabble books
    public StatusReport uploadBook(String title, String location, int totalCopies, String author, String genre ) {
        Book newBook = new Book(UUID.randomUUID().toString(), title, location, totalCopies, 0, author, genre.toLowerCase());
        return newBook.saveToDatabase() ? 
        new StatusReport(true, "book saved successfully", newBook) : 
        new StatusReport(false, "couldnt save book");
    }

    public static List<Book> getAvailableBooks() {
        return Book.getAllBooks();
    }

    //get all books that match title
    public static List<Book> getAllBooksByTitle(String title) {
        return Book.findByAttribute("title", title);
    }

    //get book by genre
    public static List<Book> getAllBooksGenre(String genere) {
        return Book.findByAttribute("genre", genere);
    }

    //get book by id
    public static Book getBookById(String id) {
        return Book.getById(id);
    }


}
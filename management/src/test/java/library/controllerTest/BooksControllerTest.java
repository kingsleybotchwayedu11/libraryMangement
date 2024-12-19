
package library.controllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import library.model.LibraryResource.Book;
import library.controllers.BooksController;
import library.controllers.StatusReport;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

public class BooksControllerTest {
    @ParameterizedTest
    @CsvSource({
        "true",
        "false",
    })
    void testUploadBookWithDifferentData(boolean mockedStatus) throws SQLException {
        // Call the method under test
        Book mockedBook = mock(Book.class);
        when(mockedBook.saveToDatabase()).thenReturn(mockedStatus);
        StatusReport statusReport = BooksController.uploadBook(mockedBook);
        assertEquals(statusReport.getOperationStatus(), mockedStatus);
    }

    @ParameterizedTest
    @CsvSource({
        "empty, 0",
        "more, 2",
        "one, 1"
    })
    void getAvailableBooks(String indicator, int size) throws Exception{
        try (MockedStatic<Book> mockedBook = Mockito.mockStatic(Book.class)) {

            mockedBook.when(Book::getAllBooks).thenReturn (
            indicator.equals("more") ? new ArrayList<Book>(Arrays.asList(new Book(), new Book())) : 
            indicator.equals("one") ? new ArrayList<Book>(Arrays.asList(new Book())) 
            : new ArrayList<>()
            );

            List<Book> retrievedBooks = BooksController.getAvailableBooks();
            assertEquals(retrievedBooks.size(), size);
        }
    }


    
    @ParameterizedTest
    @CsvSource({
        "0, Comedy",
        "2, Fiction",
        "1, Sad"
    })
    void getAllBooksGenre(int size, String genre) throws SQLException {
        try (MockedStatic<Book> mockedBook = mockStatic(Book.class)) {
            // Mocking the static method findByAttribute of Book class
            mockedBook.when(() -> Book.findByAttribute("genre", genre)).thenReturn(
                genre.equals("Fiction") ? new ArrayList<>(Arrays.asList(new Book(), new Book())) :
                genre.equals("Sad") ? new ArrayList<>(Arrays.asList(new Book())) :
                new ArrayList<>()
            );

            // Call the method under test
            List<Book> foundBooks = BooksController.getAllBooksGenre(genre);

            // Assert the result based on the expected size
            assertEquals(size, foundBooks.size());
        }
    }
   

    @ParameterizedTest
    @CsvSource({
        "0, noBook",
        "2, bookTwo",
        "1, bookOne"
    })
    void getAllBooksTitle(int size, String title) throws SQLException {
        BooksController booksController  = new BooksController();
        try (MockedStatic<Book> mockedBook = mockStatic(Book.class)) {
            // Mocking the static method findByAttribute of Book class
            mockedBook.when(() -> Book.findByAttribute("title", title)).thenReturn(
                title.equals("bookTwo") ? new ArrayList<>(Arrays.asList(new Book(), new Book())) :
                title.equals("bookOne") ? new ArrayList<>(Arrays.asList(new Book())) :
                new ArrayList<>()
            );

            // Call the method under test
            List<Book> foundBooks = BooksController.getAllBooksByTitle(title);

            // Assert the result based on the expected size
            assertEquals(size, foundBooks.size());
        }
    }


    @ParameterizedTest
    @CsvSource({
        "goodBookId",
        "wrongBookId",
    })
    void getBookId(String bookId) throws SQLException {
        try (MockedStatic<Book> mockedBook = mockStatic(Book.class)) {
            // Mocking the static method findByAttribute of Book class
            mockedBook.when(() -> Book.getById(bookId)).thenReturn(
                bookId.equals("wrongBookId") ? null  : new Book()
                
            );

            // Call the method under test
            Book searchBook = BooksController.getBookById(bookId);

            // Assert the result based on the expected size
            if(bookId.equals("goodBookId"))
                assertNotNull(searchBook);
            else 
                assertNull(searchBook);
        }
    }
   
}

package library.controllerTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import library.controllers.BooksController;
import library.controllers.UserController;
import library.model.LibraryResource.Book;
import library.model.Users.Librarian;
import library.model.Users.Patron;

public class UserControllerTest {
@ParameterizedTest
    @CsvSource({
        "userAlreadyRegistered@xxx.com",
        "userNotRegistered@xxx.com"
    })
    void addUser(String email) throws SQLException{
       try(MockedStatic<Patron> mockPatronsStatic = mockStatic(Patron.class)) {
            Patron pat  = mock(Patron.class);
            when(pat.getEmail()).thenReturn(email);
            when(pat.saveToDatabase()).thenReturn(email.equals("userAlreadyRegistered@xxx.com") ? false : true);
            mockPatronsStatic.when(() -> Patron.findOne("email", email)).thenReturn(
                email.equals("userAlreadyRegistered@xxx.com") ? pat : null
            );
           var report = library.controllers.UserController.registerPatron(pat);
           if(email.equals("userAlreadyRegistered@xxx.com"))
                assertFalse(report.getOperationStatus());
            else 
                assertTrue(report.getOperationStatus());
       }
    }


    @ParameterizedTest
    @CsvSource({
        "registerUser, password",
        "notRegisteredUser, password",
        "registeredUserWitthWrongPassword, wrongPassword"
    })
    void login(String userName, String password) throws SQLException{
       try(MockedStatic<Librarian> mockPatronsStatic = mockStatic(Librarian.class)) {
            Librarian lib  = mock(Librarian.class);
            when(lib.getUserName()).thenReturn(userName);
            when(lib.getPassword()).thenReturn((password.equals("wrongPassword") ? "differentPassword" : password));

            mockPatronsStatic.when(() -> Librarian.findOne("userName", userName)).thenReturn(
                userName.equals("registerUser") ? lib : null
            );
           var report = library.controllers.UserController.loginLibarian(userName, password);
           if(userName.equals("registerUser"))
                assertTrue(userName,report.getOperationStatus());
            else 
                assertFalse(userName,report.getOperationStatus());
       }
    }
       
}

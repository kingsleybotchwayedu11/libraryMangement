package library.modelsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import library.model.LibraryResource.Book;
import library.model.LibraryResource.Genre;
import library.model.Users.Librarian;
import library.utils.sessions.LibrarianSession;

public class SessionTest { 
    
    @Test
    void librarianSession() {
        LibrarianSession sess = new LibrarianSession();
        Librarian librarian = new Librarian();
        sess.setLibrarian(librarian);
        assertNotNull(sess.getLoggedInLibrarian());
    }
   
}

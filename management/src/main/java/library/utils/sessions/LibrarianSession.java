package library.utils.sessions;
import library.model.Users.Librarian;

public class LibrarianSession {
    private  static Librarian currentLibrarian; //current login librarian
    
    public static void setLibrarian(Librarian logInLibrarian) {
        currentLibrarian = logInLibrarian;
    }

    public static Librarian getLoggedInLibrarian() {
        return currentLibrarian;
    }
}

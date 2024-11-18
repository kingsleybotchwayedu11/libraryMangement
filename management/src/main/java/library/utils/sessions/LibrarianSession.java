package library.utils.sessions;
import library.model.Users.Librarian;

public class LibrarianSession {
    private  static Librarian currentLibrarian; //current librarian
    
    //static 
    public static void setLibrarian(Librarian logInLibrarian) {
        currentLibrarian = logInLibrarian;
    }

    public static Librarian getLoggedInLibrarian() {
        return currentLibrarian;
    }
}

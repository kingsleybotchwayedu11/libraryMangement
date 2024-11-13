package library.model.Users;

/**
 * Defines the template for a librarian, extending from Patron.
 */
public class Librarian extends LibraryUser {
    private String userName; // Username used to log in for staff members


    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

}

package library.model.Users;

/**
 * Defines the template for a library member, extending from Patron.
 */
public class Patron extends LibraryUser {
    private String libraryCardId; // Unique identifier for the library member (library card)

    // Getter and Setter for libraryCardId
    public String getLibraryCardId() {
        return libraryCardId;
    }

    public void setLibraryCardId(String libraryCardId) {
        this.libraryCardId = libraryCardId;
    }
}

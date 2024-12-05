package library.model.LibraryResource;

import library.utils.databaseOperations.DatabaseOperationInterface;

public abstract class LibraryResource implements DatabaseOperationInterface {
    // Resource attributes
    protected String title;               // The title of the resource (book, magazine, etc.)
    protected String location;            // Location of the resource in the library
    protected int totalCopies;            // Total number of copies available in the library
    protected int totalBorrowed;          // Tracks how many of the resource have been borrowed
    protected String resourceType;
    protected String id;               // Type of resource (book, magazine, etc.)

    // Getter and Setter methods
    LibraryResource(String id, String title, String location, int totalCopies, int totalBorrowed) {
        this.title = title;
        this.location = location;
        this.totalCopies = totalCopies;
        this.totalBorrowed = totalBorrowed;
        this.id = id;
        
    }
    // Getter and Setter for title
    public boolean isAvailable() {
        return this.totalBorrowed < this.totalCopies;
    }
    public String getTitle() {
        return title;
    }
    public void incrementTotalBorrowed(){
        totalBorrowed += 1;
    }

    public void decrementTotalBorrowed(){
        totalBorrowed -= 1;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId () {
        return id;
    }
    // Getter and Setter for location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and Setter for totalCopies
    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    // Getter and Setter for totalBorrowed
    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    // Getter and Setter for resourceType
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    // Additional method to calculate remaining copies (optional)
    public int getRemainingCopies() {
        return totalCopies - totalBorrowed;
    }


    // toString method for easier logging/representation (optional)
    @Override
    public String toString() {
        return "LibraryResource{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", totalCopies=" + totalCopies +
                ", totalBorrowed=" + totalBorrowed +
                ", resourceType='" + resourceType + '\'' +
                '}';
    }
}

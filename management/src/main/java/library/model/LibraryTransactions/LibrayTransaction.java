/**
 * Abstract class template to handle library transactions
 */

 package library.model.LibraryTransactions;
 
 abstract class LibraryTransaction {
 protected String id; // the id that uniquly identify transactions
 protected String type; //the type of transaction, weather borrowed or checkin

 // Getters and Setters for id
public String getId() {
    return id;
}

public void setType(String type) {
    this.id = type;
}

public String getType() {
    return type;
}

public void setId(String id) {
    this.id = id;
}
 }
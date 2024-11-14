/**
 * Abstract class template to handle library transactions
 */

 package library.model.LibraryTransactions;

import library.utils.databaseOperations.DatabaseOperationInterface;

abstract class LibraryTransaction /*implements DatabaseOperationInterface */ {
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
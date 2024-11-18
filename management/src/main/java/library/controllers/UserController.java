package library.controllers;
import library.model.Users.*;
import java.util.UUID;

public class UserController {
     //access route for only admin use cases
     static StatusReport registerLibarian(String name, String address, String email, String phoneNumber, String userName, String password ) {
          //check if username exist
          Librarian checiIfSaved  = Librarian.findOne("email", "email");
          if(checiIfSaved != null)
               return new StatusReport(false, "user with the same email saved");
          //Get form pathron object
          Librarian newLibrarian = new Librarian(UUID.randomUUID().toString(),name, address, email, phoneNumber, userName, password);
          //Save patron information to database
          newLibrarian.saveToDatabase();
          return new StatusReport(true, "successfully saved", newLibrarian);
     }


     static public StatusReport registerPatron( String name,  String address, String email, String phoneNumber ) {
          //check if patron with the same email exist
          Patron patronSaved = Patron.findOne("email", email);
          //Get library member id
          if(patronSaved != null)
               return new StatusReport(false, "User already saved");
          String libraryCardId = UUID.randomUUID().toString().replace("_", "").substring(0,10); 
          //Get form pathron object
          Patron patron = new Patron(UUID.randomUUID().toString(),name, address, email, phoneNumber,libraryCardId);
          //Save patron information to database
          boolean isSaved = patron.saveToDatabase();
          return isSaved ? new StatusReport(true, "patron saved", patron) : new StatusReport(false, "couldn't save user"); 
     }

     //login libarian
     static public StatusReport  loginLibarian(String userName, String password) {
          //fetch the libarian from the user database
          Librarian libarian = Librarian.findOne("userName", userName);
          if(libarian == null)
               return new StatusReport(false, "libarian not registered");
          //check if passwords math
          if(!libarian.getPassword().equals(password)) 
               return new StatusReport(false, "passwords dont match");
          return new StatusReport(true, "successfully login", libarian);

     }

     
}
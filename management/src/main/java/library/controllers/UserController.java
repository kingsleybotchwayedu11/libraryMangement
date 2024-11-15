package library.controllers;
import library.model.Users.*;
import java.util.UUID;

public class UserController {
     //access route for only admin use cases
     static Patron registerLibarian(String name, String address, String email, String phoneNumber, String userName, String password ) {
          //check if username exist
          
          //Get library member id
          String libraryCardId = UUID.randomUUID().toString().replace("_", "").substring(0,10); 
          //Get form pathron object
          Patron patron = new Patron(UUID.randomUUID().toString(),name, address, email, phoneNumber,libraryCardId);
          //Save patron information to database
          patron.saveToDatabase();
          return patron; 
     }


     static Patron registerPatron( String name,  String address, String email, String phoneNumber ) {
          //check if patron with the same email exist
          Patron patronSaved = Patron.findOne("email", email);
          //Get library member id
          if(patronSaved != null)
               return null;
          String libraryCardId = UUID.randomUUID().toString().replace("_", "").substring(0,10); 
          //Get form pathron object
          Patron patron = new Patron(UUID.randomUUID().toString(),name, address, email, phoneNumber,libraryCardId);
          //Save patron information to database
          boolean isSaved = patron.saveToDatabase();
          return isSaved ? patron : null; 
     }
}
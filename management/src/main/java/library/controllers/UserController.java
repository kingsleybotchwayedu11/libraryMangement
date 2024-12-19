package library.controllers;
import library.model.Users.*;

import java.sql.SQLException;
import java.util.UUID;

public class UserController {
     //access route for only admin use cases


     static public StatusReport registerPatron( Patron patron )  throws SQLException{
          //check if patron with the same email exist
          Patron patronSaved = Patron.findOne("email", patron.getEmail());
          //Get library member id
          if(patronSaved != null)
               return new StatusReport(false, "User already saved");
          //Get form pathron object
          //Save patron information to database
          boolean isSaved = patron.saveToDatabase();
          return isSaved ? new StatusReport(true, "patron saved", patron) : new StatusReport(false, "couldn't save user"); 
     }

     //login libarian
     static public StatusReport  loginLibarian(String userName, String password) throws SQLException {
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
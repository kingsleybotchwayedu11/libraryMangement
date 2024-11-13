/**
 * Defines abstract template for all users that interact with a library
 */

 package library.model.Users;
 
 abstract class LibraryUser {
 
      protected String firstName;
      protected String lastName;
      protected String password;
      protected String userId; //unique id suited for database operation(primary key)
      protected String role; // specifies the roles of the user
      protected String address; //location of the user;
      protected String email;
     
 }

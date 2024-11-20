/**
 * template for book resource
 */
package library.model.LibraryResource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;


import library.utils.databaseOperations.DatabaseConnection;

public class Book  extends LibraryResource{
    private String genre;
    private String author;
    
    public Book(String id, String title, String location, int totalCopies, int totalBorrowed, String author,  String genre) {
        super(id, title, location, totalCopies, totalBorrowed);
        this.author = author;
        this.genre = genre;
        this.resourceType = "book";  // Default value for resource type
    }

    private boolean checkIfBookExist() throws Exception {
        final String selectQuery = "SELECT 1 from Book where id = ?";
        PreparedStatement checkIfExistQuery = this.getConnection().prepareStatement(selectQuery);
        checkIfExistQuery.setString(1, this.id);
        ResultSet resultSet = checkIfExistQuery.executeQuery();
        return resultSet.next();

    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
    @Override
    public boolean saveToDatabase() {
        try {
            PreparedStatement stmt;
            //check if book is already saved
            boolean bookIsAlreadySaved = checkIfBookExist();
            if(bookIsAlreadySaved) {
                //execute update query
                String updateSQL = "UPDATE Book " +
                               "SET title = ?, location = ?, totalCopies = ?, totalBorrowed = ?, author = ?,  genre = ? " +
                                "WHERE id = ?";
                stmt = this.getConnection().prepareStatement(updateSQL);
                    // bookId
                stmt.setString(1, this.title);      // title
                stmt.setString(2, this.location);   // location
                stmt.setInt(3,    this.totalCopies);   // totalCopies
                stmt.setInt(4,    this.totalBorrowed); // totalBorrowed
                stmt.setString(5, this.author);     // author
                stmt.setString(6, this.genre);
                stmt.setString(7, this.id); 
            } else {
                //insert book
                String insertSQL = "INSERT INTO Book (id, title, location, totalCopies, totalBorrowed, author,  genre) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = this.getConnection().prepareStatement(insertSQL);
                stmt.setString(1, this.id);     // bookId
                stmt.setString(2, this.title);      // title
                stmt.setString(3, this.location);   // location
                stmt.setInt(4,    this.totalCopies);   // totalCopies
                stmt.setInt(5,    this.totalBorrowed); // totalBorrowed
                stmt.setString(6, this.author);     // author
                stmt.setString(7, this.genre);
            }
           //execute statement
           int affectedRows = stmt.executeUpdate();
           return affectedRows >= 1 ? true : false;
    
            }catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    
    @Override
    public boolean deleteFromDatabase() {
        try {
        String sql = "DELETE FROM BOOK WHERE id = ?";
        PreparedStatement stmt = this.getConnection().prepareStatement(sql);
        stmt.setString(1, this.id);
        int affectedRows = stmt.executeUpdate();
        return affectedRows >= 1 ? true : false;
        }catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    private static Book formBookObjec(ResultSet rst) throws SQLException {
        return new Book(
                        rst.getString("id"),
                        rst.getString("title"),
                        rst.getString("location"),
                        rst.getInt("totalCopies"),
                        rst.getInt("totalBorrowed"),
                        rst.getString("author"),
                        rst.getString("genre"));
    }
    public static Book getById(String id) {
        //get connectoin
        try {
           final String selectQuery = "SELECT * from Book where id = ?";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           dbEntryQuery.setString(1, id);
           ResultSet bookEntry = dbEntryQuery.executeQuery();
           //check if there are rows
           if(bookEntry.next()) {
            return formBookObjec(bookEntry);
           }
        } catch(SQLException ex) {
        ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<Book> findByAttribute(String attribute, String value) {
        //get connectoin
        ArrayList<Book> books = new ArrayList<>();
        List<String> acceptedAttributes = Arrays.asList("id", "title", "location", "genre");
        if(!acceptedAttributes.contains(attribute))
            {
                System.out.println("Wrong attribute");
                return null;
            }
        try {
           final String selectQuery = "SELECT * FROM Book WHERE " + attribute + " LIKE ?";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           dbEntryQuery.setString(1, "%" + value + "%");
           ResultSet bookEntry = dbEntryQuery.executeQuery();
           //check if there are rows
           while (bookEntry.next()) {
                books.add(formBookObjec(bookEntry));
           }
        } catch(SQLException ex) {
        ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return books;
    }
   
    public static List<Book> getAllBooks(){
        ArrayList<Book> books = new ArrayList<>();        
        try {
           final String selectQuery = "SELECT * FROM Book";
           PreparedStatement dbEntryQuery = DatabaseConnection.getConnection().prepareStatement(selectQuery);
           ResultSet bookEntry = dbEntryQuery.executeQuery();
           //check if there are rows
           while (bookEntry.next()) {
                books.add(formBookObjec(bookEntry));
           }
        } catch(SQLException ex) {
        ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return books;
    }

    
}
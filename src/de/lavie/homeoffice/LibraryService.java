package de.lavie.homeoffice;

import com.mysql.cj.protocol.x.XProtocolError;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    DataBaseConnector DB = new DataBaseConnector();
    List<Book> bookList = new ArrayList<>();

    public LibraryService() {
    }

    public void addBook(String title, String author, String publisher, String ISBN) throws SQLException {
        Connection conn = DB.connectDB();

        CallableStatement callStmt;

        callStmt = conn.prepareCall("INSERT INTO books (title, author, publisher, ISBN) VALUES (?,?,?,?)");
        callStmt.setString(1, title);
        callStmt.setString(2, author);
        callStmt.setString(3, publisher);
        callStmt.setString(4, ISBN);
        callStmt.executeUpdate();

        callStmt.close();
        conn.close();
    }

    public void deleteBook(int bookID) throws SQLException {
        delete("books", bookID);
        delete("borrowing_dates", bookID);
    }

    public void delete(String table, int bookID) throws SQLException {
        Connection conn = DB.connectDB();

        String query ="DELETE FROM "+table+" WHERE book_id = ?";

        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setInt(1,bookID);
        prepStmt.executeUpdate();

        conn.close();
    }

    public void borrowBook(int bookID, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        if(!isBorrowed(bookID,startDate,endDate)) {
            Connection conn = DB.connectDB();

            CallableStatement callStmt;

            callStmt = conn.prepareCall("INSERT INTO borrowing_dates (book_id, borrowed_from, borrowed_to) VALUES (?,?,?)");
            callStmt.setInt(1, bookID);
            callStmt.setTimestamp(2, Timestamp.valueOf(startDate));
            callStmt.setTimestamp(3, Timestamp.valueOf(endDate));
            callStmt.executeUpdate();

            callStmt.close();
            conn.close();
        }
        else{
            System.out.println("\nThis book has already been borrowed in the given time span.");
        }

    }

    private boolean isBorrowed(int bookID, LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
        Connection conn = DB.connectDB();
        Statement stmt;
        ResultSet rs;

        boolean borrowed = false;

        int book_ID;
        LocalDateTime startDate;
        LocalDateTime endDate;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM borrowing_dates");

        while(rs.next()) {
            book_ID = rs.getInt("book_id");

            if(book_ID == bookID)
            {
                startDate = rs.getTimestamp("borrowed_from").toLocalDateTime();
                endDate = rs.getTimestamp("borrowed_to").toLocalDateTime();

                if (endDateTime.isAfter(startDate) && endDateTime.isBefore(endDate) ||
                        startDateTime.isAfter(startDate) && startDateTime.isBefore(endDate) ||
                        startDateTime.isBefore(startDate) && endDateTime.isAfter(endDate) ||
                        startDateTime.isEqual(startDate) || endDateTime.isEqual(endDate)) {
                    borrowed = true;
                }
            }

        }
        stmt.close();
        rs.close();
        conn.close();

        return borrowed;
    }

    public void showLibrary() throws SQLException {
        Connection conn = DB.connectDB();

        Statement stmt;
        ResultSet rs;

        int bookID;
        String title;
        String author;
        String publisher;
        String ISBN;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM books");

        while (rs.next()) {
            bookID = rs.getInt("book_id");
            title = rs.getString("title");
            author= rs.getString("author");
            publisher = rs.getString("publisher");
            ISBN = rs.getString("ISBN");
            System.out.format("\nBook %d:\n" +
                    "Title: %s | Author: %s | Publisher: %s | ISBN: %s\n",bookID,title,author,publisher,ISBN);
            showBorrowingDates(bookID);
        }
        stmt.close();
        rs.close();
        conn.close();
    }

    public void showBorrowingDates(int bookID) throws SQLException
    {
        Connection conn = DB.connectDB();
        Statement stmt;
        ResultSet rs;

        int book_ID;
        Timestamp startDate;
        Timestamp endDate;

        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM borrowing_dates");

        while(rs.next()) {
         book_ID = rs.getInt("book_id");

         if(book_ID == bookID)
         {
             startDate = rs.getTimestamp("borrowed_from");
             endDate = rs.getTimestamp("borrowed_to");
             System.out.format("Borrowed From: %s | To: %s\n",startDate,endDate);
         }

        }
        stmt.close();
        rs.close();
        conn.close();
    }
}
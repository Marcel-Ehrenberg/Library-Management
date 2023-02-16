package de.lavie.homeoffice;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String name;
    private String author;
    private String publisher;
    private String ISBN;
    List<Borrow> borrowList = new ArrayList<>();

    public Book(String name, String author, String publisher, String ISBN) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.ISBN = ISBN;
    }
}

package service;

import model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);  //aici nu mai trebuie sa fie cu Optional?
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
}

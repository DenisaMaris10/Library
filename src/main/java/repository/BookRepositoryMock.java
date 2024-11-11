package repository;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{
    private final List<Book> books;
    public BookRepositoryMock(){
        books = new ArrayList<>();
    }
    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) { //Optional inlocuitor pentru null
        return books.parallelStream() //pt un nr mic de elemente ar fi mai bine sa folosim doar stream
                .filter(it->it.getId().equals(id))
                .findFirst(); // findFirst returneaza tot un Optional
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}

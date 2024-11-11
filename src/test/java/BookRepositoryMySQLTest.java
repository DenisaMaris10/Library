import database.DatabaseConnectionFactory;
import model.Book;
import model.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {

    private static BookRepository bookRepository;

    @BeforeAll
    public static void setup(){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
    }

    @Test
    public void findAll(){
        List<Book> books = bookRepository.findAll();
        assertEquals(0, books.size());
    }

    @Test
    public void findById(){
        final Optional<Book> book = bookRepository.findById(1L);
        assertTrue(book.isEmpty());
    }

    @Test
    public void save(){
        assertTrue(bookRepository.save(new BookBuilder().setAuthor("Mihail Sadoveanu").setTitle("Baltagul").setPublishedDate(LocalDate.of(1930, 10, 2)).build()));
    }

    @Test
    public void delete(){
        assertTrue(bookRepository.delete(new BookBuilder().setAuthor("Mihail Sadoveanu").setTitle("Baltagul").setPublishedDate(LocalDate.of(1930, 10, 2)).build()));
    }
}

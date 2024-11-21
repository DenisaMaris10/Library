import controller.LoginController;
import database.DatabaseConnectionFactory;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import launcher.Launcher;
import launcher.LoginComponentFactory;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.LoginView;

import java.sql.Connection;
import java.time.LocalDate;

import static database.Constants.Schemas.PRODUCTION;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        final AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);

        final LoginView loginView = new LoginView(primaryStage);

        new LoginController(loginView, authenticationService);
    }
//    public static void main(String[] args){
////        System.out.println("Hello world!");
////
//        Book book = new BookBuilder()
//                .setTitle("Ion")
//                .setAuthor("Liviu Rebreanu")
//                .setPublishedDate(LocalDate.of(1910, 10, 20))
//                .build();
////        System.out.println(book);
////
////        BookRepository bookRepository = new BookRepositoryMock();
////        bookRepository.save(book);
////        bookRepository.save(new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950, 2, 10)).build());
////        System.out.println(bookRepository.findAll());
////        bookRepository.removeAll();
////        System.out.println(bookRepository.findAll());
//        //DatabaseConnectionFactory.getConnectionWrapper(false);
//        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
//        BookRepository bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<Book>());
//        BookService bookService = new BookServiceImpl(bookRepository);
//
//        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
//        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
//
//        AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
//
//        if(userRepository.existsByUsername("Alex")){
//            System.out.println("Username already present into the user table");
//        }
//        else {
//            authenticationService.register("Alex", "parola123!");
//        }
//
//        System.out.println(authenticationService.login("Alex", "parola123!"));
//        //bookService.save(book);
//        //System.out.println(bookService.findAll());
////        Book bookMoaraCuNoroc = new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950, 2, 10)).build();
////        bookService.save(bookMoaraCuNoroc);
////        System.out.println(bookRepository.findAll());
////        bookService.delete(bookMoaraCuNoroc);
////        bookService.delete(book);
////        System.out.println(bookRepository.findAll());
//    }


}

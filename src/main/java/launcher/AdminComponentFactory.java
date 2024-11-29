package launcher;

import controller.AdminController;
import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import repository.book.BookRepository;
import repository.order.OrderRepository;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import view.AdminView;
import view.BookView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;
    private final AuthenticationService authenticationService;
    private static volatile AdminComponentFactory instance;

    public static AdminComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, Long userId){
        if(instance == null){
            synchronized (EmployeeComponentFactory.class) {
                if(instance == null) {
                    instance = new AdminComponentFactory(componentsForTest, primaryStage, userId);
                }
            }
        }
        return instance;
    }

    private AdminComponentFactory(Boolean componentsForTest, Stage primaryStage, Long userId){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.userService = new UserServiceImpl(userRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        List<UserDTO> usersDTOs = UserMapper.convertUserListToUserDTOList(userService.findAll());
        this.adminView = new AdminView(primaryStage, usersDTOs);
        this.adminController = new AdminController(adminView, userService, rightsRolesRepository, authenticationService, userId);
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public AdminController getBookController() {
        return adminController;
    }

    public UserRepository getBookRepository() {
        return userRepository;
    }

    public UserService getBookService() {
        return userService;
    }

}

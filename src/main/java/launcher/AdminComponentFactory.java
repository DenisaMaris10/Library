package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import repository.report.ReportGenerationRepository;
import repository.report.ReportGenerationRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.rights_roles.RightsRolesService;
import service.rights_roles.RightsRolesServiceImpl;
import service.user.*;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;
    private final RightsRolesService rightsRolesService;
    private final AuthenticationService authenticationService;
    private final ReportGenerationRepository reportGenerationRepository;
    private final ReportGenerationService reportGenerationService;
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
        this.rightsRolesService = new RightsRolesServiceImpl(rightsRolesRepository);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.userService = new UserServiceImpl(userRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.reportGenerationRepository = new ReportGenerationRepositoryMySQL(connection);
        this.reportGenerationService = new EmployeesSumReportService(reportGenerationRepository);
        List<UserDTO> usersDTOs = UserMapper.convertUserListToUserDTOList(userService.findAll());
        this.adminView = new AdminView(primaryStage, usersDTOs);
        this.adminController = new AdminController(adminView, userService, rightsRolesService, authenticationService, reportGenerationService, userId);
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

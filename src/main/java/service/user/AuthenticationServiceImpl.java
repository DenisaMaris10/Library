package service.user;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;

import static database.Constants.Roles.CUSTOMER;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> customerRegister(String username, String password) {
        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER); // doar customerii vor putea sa se inregistreze direct de pe platforma, angajatii vor fi creati de catre admini
        return genericRegister(username, password, customerRole);
    }

    @Override
    public Notification<Boolean> genericRegister(String username, String password, Role role){
        //Prin criptare, de la un mesaj criptat ne putem intoarce la mesajul decriptat: mesaj -> fdnaklfnfjksgsgss -> mesaj
        // o parola simpla se va transforma intr-un hash
        // Dintr-un hash nu ma pot intoarce la mesajul initial
        // Hashing ex: parolasimpla -> fhalnjdfnslasjfd

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(role)) //lista imutabila si care are o singura copie
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();

        if (!userValid){
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(hashPassword(password));
            Notification<Boolean> saveResult = userRepository.save(user);
            if(saveResult.hasErrors()) {
                userRegisterNotification.addError(saveResult.getFirstError());
                userRegisterNotification.setResult(Boolean.FALSE);
            }
            else
                userRegisterNotification.setResult(Boolean.TRUE);
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, hashPassword(password));
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256 (256 - formatul, adica lungimea)
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8)); //transformam parola in bytes
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
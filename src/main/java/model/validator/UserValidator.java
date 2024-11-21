package model.validator;


import repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final int MIN_PASSWORD_LENGTH = 8;
    private final List<String> errors;
    private final UserRepository userRepository;

    public UserValidator(UserRepository user) {
        this.userRepository = user;
        this.errors = new ArrayList<>();
    }

    public boolean validate(String username, String password) {
//        validateUsername(user.getUsername());
//        validatePassword(user.getPassword());
        errors.clear();
        validateEmailUniqueness(username);
        validateEmail(username);
        validatePassword(password);

        return errors.isEmpty();
    }

    private void validateEmailUniqueness(String email){
        final boolean response = userRepository.existsByUsername(email);
        if (response) {
            errors.add("Email is already taken");
        }
    }

    private void validateEmail(String email){
        if(!email.matches(EMAIL_VALIDATION_REGEX)){
            errors.add("Email is not valid");
        }
    }
    private void validateUsername(String username){
        if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(username).matches()){
            errors.add("Email is not valid!");
        }
    }

    private void validatePassword(String password){
        if (password.length() < MIN_PASSWORD_LENGTH){
            errors.add(String.format("Password must be at least %d characters long!", MIN_PASSWORD_LENGTH));
        }

        if (!containsSpecialCharacter(password)){
            errors.add("Password must contain at least one special character.");
        }

        if (!containsDigit(password)){
            errors.add("Password must contain at least one digit!");
        }
    }

    private boolean containsSpecialCharacter(String password){
        if (password == null || password.trim().isEmpty()){
            return false;
        }
        // black list
        Pattern specialCharactersPattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher specialCharactersMatcher = specialCharactersPattern.matcher(password);

        return specialCharactersMatcher.find();
    }

    private boolean containsDigit(String password) {
        return Pattern.compile(".*[0-9].*").matcher(password).find();
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        return String.join("\n", errors);
    }
}
package service.user;


import model.User;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean delete(User user) {
        return userRepository.delete(user);
    }
}

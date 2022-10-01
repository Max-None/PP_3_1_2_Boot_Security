package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    void saveUser(User user, String[] authorities);

    List<User> index();

    User getUserById(int id);

    void deleteUser(int id);
}

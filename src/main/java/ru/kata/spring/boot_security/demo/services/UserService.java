package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void saveUser(User user, String[] authorities) {
        List<Role> userRoles= new ArrayList<>();
        for (String authority : authorities) {
            if (authority.equals("ROLE_USER")) {
                userRoles.add(roleRepository.getById(1));
            }
            if (authority.equals("ROLE_ADMIN")) {
                userRoles.add(roleRepository.getById(2));
            }
        }
        user.setRoles(userRoles);
        if (!user.getPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(userRepository.getById(user.getId()).getPassword());
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> index() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}

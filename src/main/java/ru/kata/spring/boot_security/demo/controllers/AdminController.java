package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.index());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "authorities" , required = false) String[] authorities) {
        List<Role> userRoles= new ArrayList<>();
        for (int i = 0; i < authorities.length; i++) {
            Role role = new Role();
            role.setName(authorities[i]);
            userRoles.add(role);
        }
        user.setRoles(userRoles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable ("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable ("id") int id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        boolean[] checkedRoles = new boolean[2];
        checkedRoles[0] = false;
        checkedRoles[1] = false;
        List<Role> setOfRoles = user.getRoles();
        for(int i = 0; i < setOfRoles.size(); i++) {
            checkedRoles[0] = setOfRoles.get(i).getAuthority().equals("ROLE_USER") || checkedRoles[0];
            checkedRoles[1] = setOfRoles.get(i).getAuthority().equals("ROLE_ADMIN") || checkedRoles[1];
        }
        model.addAttribute("checkedRoles", checkedRoles);
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable ("id") int id) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
}

package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.util.Set;

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
        userService.saveUser(user, authorities);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable ("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable ("id") int id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        boolean[] checkedRoles = new boolean[2];
        Set<Role> setOfRoles = user.getRoles();
        for (Role setOfRole : setOfRoles) {
            checkedRoles[0] = setOfRole.getAuthority().equals("ROLE_USER") || checkedRoles[0];
            checkedRoles[1] = setOfRole.getAuthority().equals("ROLE_ADMIN") || checkedRoles[1];
        }
        model.addAttribute("checkedRoles", checkedRoles);
        user.setPassword("");
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "authorities" , required = false) String[] authorities) {
        userService.saveUser(user, authorities);
        return "redirect:/admin";
    }
}

package es.deusto.spq.webclient.controller;

import es.deusto.spq.webclient.service.ServerApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserWebController {

    private final ServerApiService apiService;

    public UserWebController(ServerApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "users/register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String login,
                             @RequestParam String password,
                             Model model) {
        String result = apiService.registerUser(login, password);
        model.addAttribute("result", result);
        model.addAttribute("login", login);
        return "users/register";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", apiService.getAllUsers());
        return "users/list";
    }
}

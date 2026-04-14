package es.deusto.spq.webclient.controller;

import es.deusto.spq.webclient.service.ServerApiService;
import es.deusto.spq.serializable.MessageData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageWebController {

    private final ServerApiService apiService;

    public MessageWebController(ServerApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/post")
    public String showPostForm() {
        return "messages/post";
    }

    @PostMapping("/post")
    public String doPost(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam String message,
                         Model model) {
        MessageData result = apiService.postMessage(login, password, message);
        model.addAttribute("result", result != null ? result.getMessage() : "No response");
        return "messages/post";
    }

    @GetMapping("/list")
    public String showListForm() {
        return "messages/list";
    }

    @PostMapping("/list")
    public String listMessages(@RequestParam String login, Model model) {
        model.addAttribute("messages", apiService.getMessagesByUser(login));
        model.addAttribute("login", login);
        return "messages/list";
    }
}

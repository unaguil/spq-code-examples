package es.deusto.spq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.deusto.spq.persistence.Message;
import es.deusto.spq.persistence.User;
import es.deusto.spq.persistence.UserRepository;
import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    private final Counter userRegistrationCounter;
    private final Counter messagePostedCounter;
    private final Counter authFailureCounter;

    public UserController(MeterRegistry meterRegistry) {
        this.userRegistrationCounter = Counter.builder("user.registrations")
                .description("Total number of user registrations")
                .register(meterRegistry);
        this.messagePostedCounter = Counter.builder("message.posted")
                .description("Total number of messages posted successfully")
                .register(meterRegistry);
        this.authFailureCounter = Counter.builder("auth.failures")
                .description("Total number of failed authentication attempts")
                .register(meterRegistry);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String registerUser(@RequestBody UserData userData) {    
        logger.info("Checking whether the user already exists or not: '{}'", userData.getLogin());
        User user = userRepository.findById(userData.getLogin()).orElse(null);
        logger.info("User: {}", user);
        if (user != null) {
            logger.info("Setting password for user: {}", user);
            user.setPassword(userData.getPassword());
            userRepository.save(user);
            logger.info("Password set for user: {}", user);
        } else {
            logger.info("Creating user: {}", user);
            user = new User(userData.getLogin(), userData.getPassword());
            userRepository.save(user);
            logger.info("User created: {}", user);
        }
        userRegistrationCounter.increment();
        return "Saved";
    }

    @PostMapping(path = "/say")
    public @ResponseBody ResponseEntity<?> sayMessage(@RequestBody DirectMessage directMessage) {
        User user = userRepository.findById(directMessage.getUserData().getLogin()).orElse(null);
        if (user != null && user.getPassword().equals(directMessage.getUserData().getPassword())) {
            logger.info("User retrieved: {}", user);
            Message message = new Message(directMessage.getMessageData().getMessage());
            logger.info("Saving message: {}", message);
            user.addMessage(message);
            userRepository.save(user);
            MessageData messageData = new MessageData();
            messageData.setMessage(directMessage.getMessageData().getMessage());
            messagePostedCounter.increment();
            return ResponseEntity.ok(messageData);
        } else {
            authFailureCounter.increment();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login details supplied for message delivery are not correct");
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            UserData userData = new UserData();
            userData.setLogin(user.getLogin());
            userData.setPassword(user.getPassword());
            users.add(userData);
        }

        return users;
    }
}

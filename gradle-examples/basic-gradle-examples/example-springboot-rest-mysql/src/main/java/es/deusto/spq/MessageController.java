package es.deusto.spq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.deusto.spq.persistence.Message;
import es.deusto.spq.persistence.User;
import es.deusto.spq.persistence.UserRepository;
import es.deusto.spq.serializable.MessageData;

@Controller
@RequestMapping(path = "/messages")
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    @Autowired
	private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<MessageData> getMessagesByUser(@Param("login") String login) {
        logger.info("Getting all messages by user: '{}'", login);
        User user = userRepository.findById(login).orElse(null);
        if (user != null) {
            logger.info("User retrieved: {}", user);
            List<MessageData> messages = new ArrayList<>();
            for (Message message : user.getMessages()) {
                MessageData messageData = new MessageData();
                messageData.setMessage(message.getText());
                messages.add(messageData);
            }
            logger.info("Messages retrieved: {}", messages.size());
            return messages;
        } else {
            logger.info("User not found: '{}'", login);
            return Collections.emptyList();
        }
    }
}

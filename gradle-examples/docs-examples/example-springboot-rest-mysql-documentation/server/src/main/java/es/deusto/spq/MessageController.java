package es.deusto.spq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import es.deusto.spq.persistence.Message;
import es.deusto.spq.persistence.User;
import es.deusto.spq.persistence.UserRepository;
import es.deusto.spq.serializable.MessageData;

/**
 * REST endpoint to query messages for a given user.
 */
@Controller
@RequestMapping(path = "/messages")
@Tag(name = "Messages", description = "Operations for querying stored messages")
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Returns all messages authored by a user identified by login.
     *
     * @param login user identifier
     * @return list of messages or empty list when user does not exist
     */
    @GetMapping(path = "/all")
    @Operation(summary = "List all messages by user login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages returned")
    })
    public @ResponseBody Iterable<MessageData> getMessagesByUser(
            @Parameter(description = "User login", example = "alice")
            @RequestParam("login") String login) {
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

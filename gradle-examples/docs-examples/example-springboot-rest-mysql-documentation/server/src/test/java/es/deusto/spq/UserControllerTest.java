package es.deusto.spq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.spq.persistence.User;
import es.deusto.spq.persistence.UserRepository;
import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_createsNewUserAndReturnsSaved() throws Exception {
        UserData userData = new UserData();
        userData.setLogin("alice");
        userData.setPassword("pass123");

        when(userRepository.findById("alice")).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_updatesPasswordOfExistingUser() throws Exception {
        User existing = new User("alice", "oldpass");
        UserData userData = new UserData();
        userData.setLogin("alice");
        userData.setPassword("newpass");

        when(userRepository.findById("alice")).thenReturn(Optional.of(existing));

        mockMvc.perform(post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));

        verify(userRepository).save(existing);
    }

    @Test
    void sayMessage_withValidCredentials_returnsMessage() throws Exception {
        User user = new User("alice", "pass123");
        UserData userData = new UserData();
        userData.setLogin("alice");
        userData.setPassword("pass123");
        MessageData messageData = new MessageData();
        messageData.setMessage("Hello world");
        DirectMessage directMessage = new DirectMessage();
        directMessage.setUserData(userData);
        directMessage.setMessageData(messageData);

        when(userRepository.findById("alice")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/say")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(directMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello world"));
    }

    @Test
    void sayMessage_withInvalidPassword_returnsBadRequest() throws Exception {
        User user = new User("alice", "pass123");
        UserData userData = new UserData();
        userData.setLogin("alice");
        userData.setPassword("wrongpass");
        MessageData messageData = new MessageData();
        messageData.setMessage("Hello world");
        DirectMessage directMessage = new DirectMessage();
        directMessage.setUserData(userData);
        directMessage.setMessageData(messageData);

        when(userRepository.findById("alice")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/say")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(directMessage)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_returnsListOfUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(
                new User("alice", "pass"),
                new User("bob", "pass")));

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].login").value("alice"))
                .andExpect(jsonPath("$[1].login").value("bob"));
    }
}

package es.deusto.spq;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.deusto.spq.persistence.Message;
import es.deusto.spq.persistence.User;
import es.deusto.spq.persistence.UserRepository;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void getMessagesByUser_returnsMessagesForKnownUser() throws Exception {
        User alice = new User("alice", "pass");
        Message msg1 = new Message("Hello");
        Message msg2 = new Message("World");
        alice.addMessage(msg1);
        alice.addMessage(msg2);

        when(userRepository.findById("alice")).thenReturn(Optional.of(alice));

        mockMvc.perform(get("/messages/all").param("login", "alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getMessagesByUser_returnsEmptyListForUnknownUser() throws Exception {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/messages/all").param("login", "unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

package es.deusto.spq.webclient.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.deusto.spq.serializable.UserData;
import es.deusto.spq.webclient.service.ServerApiService;

@WebMvcTest(UserWebController.class)
class UserWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServerApiService apiService;

    @Test
    void showRegisterForm_returnsRegisterView() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"));
    }

    @Test
    void doRegister_callsServiceAndAddsResultToModel() throws Exception {
        when(apiService.registerUser("alice", "pw")).thenReturn("Saved");

        mockMvc.perform(post("/users/register")
                        .param("login", "alice")
                        .param("password", "pw"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/register"))
                .andExpect(model().attribute("result", "Saved"))
                .andExpect(model().attribute("login", "alice"));

        verify(apiService).registerUser("alice", "pw");
    }

    @Test
    void listUsers_callsServiceAndAddsUsersToModel() throws Exception {
        UserData alice = new UserData();
        alice.setLogin("alice");
        UserData bob = new UserData();
        bob.setLogin("bob");
        when(apiService.getAllUsers()).thenReturn(List.of(alice, bob));

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"));

        verify(apiService).getAllUsers();
    }
}

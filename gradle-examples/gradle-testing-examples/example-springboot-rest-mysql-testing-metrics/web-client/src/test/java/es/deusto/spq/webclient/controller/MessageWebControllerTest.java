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

import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.webclient.service.ServerApiService;

@WebMvcTest(MessageWebController.class)
class MessageWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServerApiService apiService;

    @Test
    void showPostForm_returnsPostView() throws Exception {
        mockMvc.perform(get("/messages/post"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages/post"));
    }

    @Test
    void doPost_callsServiceAndAddsResultToModel() throws Exception {
        MessageData response = new MessageData();
        response.setMessage("Hello world");
        when(apiService.postMessage("alice", "pw", "Hello world")).thenReturn(response);

        mockMvc.perform(post("/messages/post")
                        .param("login", "alice")
                        .param("password", "pw")
                        .param("message", "Hello world"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages/post"))
                .andExpect(model().attribute("result", "Hello world"));

        verify(apiService).postMessage("alice", "pw", "Hello world");
    }

    @Test
    void showListForm_returnsListView() throws Exception {
        mockMvc.perform(get("/messages/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages/list"));
    }

    @Test
    void listMessages_callsServiceAndAddsMessagesToModel() throws Exception {
        MessageData m1 = new MessageData();
        m1.setMessage("Hi");
        MessageData m2 = new MessageData();
        m2.setMessage("Hey");
        when(apiService.getMessagesByUser("alice")).thenReturn(List.of(m1, m2));

        mockMvc.perform(post("/messages/list")
                        .param("login", "alice"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages/list"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attribute("login", "alice"));

        verify(apiService).getMessagesByUser("alice");
    }
}

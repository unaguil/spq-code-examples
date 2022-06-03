package es.deusto.spq.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ObjectInputFilter.Status;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.DirectMessage;
import es.deusto.spq.pojo.UserData;


public class ExampleClientTest {

    @Mock
    private Client client;

    @Mock(answer=Answers.RETURNS_DEEP_STUBS)
    private WebTarget webTarget;

    @Captor
    private ArgumentCaptor<Entity<UserData>> userDataEntityCaptor;

    @Captor
    private ArgumentCaptor<Entity<DirectMessage>> directMessageEntityCaptor;

    private ExampleClient exampleClient;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        exampleClient = new ExampleClient(client, webTarget);
    }

    @Test
    public void testRegisterUser() {
        when(webTarget.path("register")).thenReturn(webTarget);

        Response response = Response.ok().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        assertTrue(exampleClient.registerUser("test-login", "passwd"));

        verify(webTarget.request(MediaType.APPLICATION_JSON)).post(userDataEntityCaptor.capture());
        assertEquals("test-login", userDataEntityCaptor.getValue().getEntity().getLogin());
        assertEquals("passwd", userDataEntityCaptor.getValue().getEntity().getPassword());
    }

    @Test
    public void testRegisterUserWithError() {
        when(webTarget.path("register")).thenReturn(webTarget);

        Response response = Response.serverError().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        assertFalse(exampleClient.registerUser("test-login", "passwd"));

        verify(webTarget.request(MediaType.APPLICATION_JSON)).post(userDataEntityCaptor.capture());
        assertEquals("test-login", userDataEntityCaptor.getValue().getEntity().getLogin());
        assertEquals("passwd", userDataEntityCaptor.getValue().getEntity().getPassword());
    }

    @Test
    public void testSayMessage() {
        when(webTarget.path("sayMessage")).thenReturn(webTarget);

        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(String.class)).thenReturn("server says hello");

        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        assertTrue(exampleClient.sayMessage("test-login", "passwd", "hello world"));

        verify(webTarget.request(MediaType.APPLICATION_JSON)).post(directMessageEntityCaptor.capture());
        assertEquals("hello world", directMessageEntityCaptor.getValue().getEntity().getMessageData().getMessage());
        assertEquals("test-login", directMessageEntityCaptor.getValue().getEntity().getUserData().getLogin());
        assertEquals("passwd", directMessageEntityCaptor.getValue().getEntity().getUserData().getPassword());
    }

    @Test
    public void testSayMessageWithError() {
        when(webTarget.path("sayMessage")).thenReturn(webTarget);

        Response response = Response.serverError().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        assertFalse(exampleClient.sayMessage("test-login", "passwd", "hello world"));

        verify(webTarget.request(MediaType.APPLICATION_JSON)).post(directMessageEntityCaptor.capture());
        assertEquals("hello world", directMessageEntityCaptor.getValue().getEntity().getMessageData().getMessage());
        assertEquals("test-login", directMessageEntityCaptor.getValue().getEntity().getUserData().getLogin());
        assertEquals("passwd", directMessageEntityCaptor.getValue().getEntity().getUserData().getPassword());
    }
}

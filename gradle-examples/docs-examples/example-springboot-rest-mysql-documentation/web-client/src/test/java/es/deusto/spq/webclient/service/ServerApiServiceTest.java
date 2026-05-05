package es.deusto.spq.webclient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;

class ServerApiServiceTest {

    private static final String BASE_URL = "http://localhost:8080";

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final ServerApiService service = new ServerApiService(restTemplate, BASE_URL);

    @Test
    void registerUser_postsToUsersAddAndReturnsBody() {
        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "/users/add"),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("Saved", HttpStatus.OK));

        String result = service.registerUser("alice", "pw");

        assertThat(result).isEqualTo("Saved");
        verify(restTemplate).exchange(
                Mockito.eq(BASE_URL + "/users/add"),
                Mockito.eq(HttpMethod.POST),
                Mockito.argThat(entity -> {
                    Object body = ((HttpEntity<?>) entity).getBody();
                    if (!(body instanceof UserData ud)) return false;
                    return "alice".equals(ud.getLogin()) && "pw".equals(ud.getPassword());
                }),
                Mockito.eq(String.class));
    }

    @Test
    void postMessage_postsToUsersSayAndReturnsMessageData() {
        MessageData response = new MessageData();
        response.setMessage("Hello");
        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "/users/say"),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(MessageData.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        MessageData result = service.postMessage("alice", "pw", "Hello");

        assertThat(result.getMessage()).isEqualTo("Hello");
        verify(restTemplate).exchange(
                Mockito.eq(BASE_URL + "/users/say"),
                Mockito.eq(HttpMethod.POST),
                Mockito.argThat(entity -> {
                    Object body = ((HttpEntity<?>) entity).getBody();
                    if (!(body instanceof DirectMessage dm)) return false;
                    return "alice".equals(dm.getUserData().getLogin())
                            && "Hello".equals(dm.getMessageData().getMessage());
                }),
                Mockito.eq(MessageData.class));
    }

    @Test
    void getAllUsers_getsFromUsersAllAndReturnsList() {
        UserData alice = new UserData();
        alice.setLogin("alice");
        UserData bob = new UserData();
        bob.setLogin("bob");
        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "/users/all"),
                Mockito.eq(HttpMethod.GET),
                Mockito.isNull(),
                Mockito.eq(UserData[].class)))
                .thenReturn(new ResponseEntity<>(new UserData[]{alice, bob}, HttpStatus.OK));

        List<UserData> result = service.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLogin()).isEqualTo("alice");
        assertThat(result.get(1).getLogin()).isEqualTo("bob");
    }

    @Test
    void getMessagesByUser_getsFromMessagesAllAndReturnsList() {
        MessageData m1 = new MessageData();
        m1.setMessage("Hi");
        when(restTemplate.exchange(
                Mockito.eq(BASE_URL + "/messages/all?login=alice"),
                Mockito.eq(HttpMethod.GET),
                Mockito.isNull(),
                Mockito.eq(MessageData[].class)))
                .thenReturn(new ResponseEntity<>(new MessageData[]{m1}, HttpStatus.OK));

        List<MessageData> result = service.getMessagesByUser("alice");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Hi");
    }
}

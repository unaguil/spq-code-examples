package es.deusto.spq.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.deusto.spq.serializable.UserData;

public class ExampleClientTest {

    @Test
    public void testRegisterUser() {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        String host = "localhost";
        String port = "8080";
        ExampleClient client = new ExampleClient(restTemplate, host, port);
        String url = "http://" + host + ":" + port + "/users/add";
        ResponseEntity<String> resp = new ResponseEntity<>("Created", HttpStatus.OK);

        Mockito.when(restTemplate.exchange(Mockito.eq(url), Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)))
                .thenReturn(resp);

        client.registerUser("alice", "pwd");

        Mockito.verify(restTemplate).exchange(
                Mockito.eq(url),
                Mockito.eq(HttpMethod.POST),
                Mockito.argThat(argument -> {
                    if (!(argument instanceof HttpEntity))
                        return false;
                    HttpEntity<?> he = (HttpEntity<?>) argument;
                    Object body = he.getBody();
                    if (!(body instanceof UserData))
                        return false;
                    UserData ud = (UserData) body;
                    return "alice".equals(ud.getLogin()) && "pwd".equals(ud.getPassword());
                }),
                Mockito.eq(String.class));
    }
}

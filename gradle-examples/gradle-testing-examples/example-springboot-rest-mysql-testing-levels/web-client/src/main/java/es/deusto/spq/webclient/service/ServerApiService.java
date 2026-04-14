package es.deusto.spq.webclient.service;

import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ServerApiService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ServerApiService(RestTemplate restTemplate,
                            @Value("${server.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public String registerUser(String login, String password) {
        UserData user = new UserData();
        user.setLogin(login);
        user.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserData> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(baseUrl + "/users/add", HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    public MessageData postMessage(String login, String password, String message) {
        UserData user = new UserData();
        user.setLogin(login);
        user.setPassword(password);

        MessageData md = new MessageData();
        md.setMessage(message);

        DirectMessage dm = new DirectMessage();
        dm.setUserData(user);
        dm.setMessageData(md);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DirectMessage> request = new HttpEntity<>(dm, headers);

        ResponseEntity<MessageData> response =
                restTemplate.exchange(baseUrl + "/users/say", HttpMethod.POST, request, MessageData.class);
        return response.getBody();
    }

    public List<UserData> getAllUsers() {
        ResponseEntity<UserData[]> response =
                restTemplate.exchange(baseUrl + "/users/all", HttpMethod.GET, null, UserData[].class);
        if (response.getBody() == null) return Collections.emptyList();
        return Arrays.asList(response.getBody());
    }

    public List<MessageData> getMessagesByUser(String login) {
        ResponseEntity<MessageData[]> response =
                restTemplate.exchange(baseUrl + "/messages/all?login=" + login,
                        HttpMethod.GET, null, MessageData[].class);
        if (response.getBody() == null) return Collections.emptyList();
        return Arrays.asList(response.getBody());
    }
}

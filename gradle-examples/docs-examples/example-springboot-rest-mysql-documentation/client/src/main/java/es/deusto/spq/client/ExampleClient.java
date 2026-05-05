package es.deusto.spq.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleClient {

    protected static final Logger logger = LogManager.getLogger();

    private static final String USER = "dipina";
    private static final String PASSWORD = "dipina";

    private RestTemplate restTemplate;
    private String baseUrl;

    public ExampleClient(RestTemplate restTemplate, String hostname, String port) {
        this.restTemplate = restTemplate;
        this.baseUrl = String.format("http://%s:%s", hostname, port);
    }

    public ExampleClient(String hostname, String port) {
        this(new RestTemplate(), hostname, port);
    }

    public void printUsers() {
        String url = baseUrl + "/users/all";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatusCode());
            try {
                List<UserData> users = Arrays
                        .asList(new ObjectMapper().readValue(response.getBody(), UserData[].class));
                for (UserData user : users) {
                    logger.info("User: {}", user.getLogin());
                }
            } catch (Exception e) {
                logger.error("Error parsing the response: {}", e.getMessage());
            }
        } else {
            String responseMessage = response.getBody();
            logger.info("* Message coming from the server: '{}'", responseMessage);
        }
    }

    public void registerUser(String login, String password) {
        String url = baseUrl + "/users/add";
        UserData userData = new UserData();
        userData.setLogin(login);
        userData.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserData> request = new HttpEntity<>(userData, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatusCode());
        } else {
            logger.info("User correctly registered");
        }
    }

    public void sayMessage(String login, String password, String message) {
        String url = baseUrl + "/users/say";
        DirectMessage directMessage = new DirectMessage();
        UserData userData = new UserData();
        userData.setLogin(login);
        userData.setPassword(password);

        directMessage.setUserData(userData);

        MessageData messageData = new MessageData();
        messageData.setMessage(message);
        directMessage.setMessageData(messageData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DirectMessage> request = new HttpEntity<>(directMessage, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatusCode());
        } else {
            String responseMessage = response.getBody();
            logger.info("* Message coming from the server: '{}'", responseMessage);
        }
    }

    public List<MessageData> getMessagesByUser(String login) {
        String url = baseUrl + "/messages/all?login=" + login;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatusCode());
        } else {
            try {
                return Arrays.asList(new ObjectMapper().readValue(response.getBody(), MessageData[].class));
            } catch (Exception e) {
                logger.error("Error parsing the response: {}", e.getMessage());
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // if (args.length != 2) {
        // logger.info("Use: java Client.Client [host] [port]");
        // System.exit(0);
        // }

        String hostname = "localhost";
        String port = "8080";

        ExampleClient exampleClient = new ExampleClient(hostname, port);
        exampleClient.printUsers();
        exampleClient.registerUser(USER, PASSWORD);
        exampleClient.sayMessage(USER, PASSWORD, "This is a test!...");

        List<MessageData> messages = exampleClient.getMessagesByUser(USER);
        if (messages != null) {
            for (MessageData message : messages) {
                logger.info("Message: {}", message.getMessage());
            }
        }
    }
}

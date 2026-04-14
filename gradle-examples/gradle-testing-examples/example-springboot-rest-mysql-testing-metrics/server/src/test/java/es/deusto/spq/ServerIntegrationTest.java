package es.deusto.spq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;

/**
 * Integration tests for the full REST API + MySQL persistence stack.
 * Uses Testcontainers to spin up a real MySQL instance automatically.
 *
 * Runs with: ./gradlew :server:integrationTest
 */
@Tag("integration")
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ServerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8")
            .withDatabaseName("messages")
            .withUsername("spq")
            .withPassword("spq");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void registerUser_persistsNewUser() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser1");
        userData.setPassword("pass1");

        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/users/add"), userData, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Saved");
    }

    @Test
    void registerUser_updatesExistingUser() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser2");
        userData.setPassword("initialPass");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);

        userData.setPassword("updatedPass");
        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/users/add"), userData, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Saved");
    }

    @Test
    void sayMessage_withValidCredentials_storesAndReturnsMessage() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser3");
        userData.setPassword("pass3");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);

        DirectMessage dm = new DirectMessage();
        dm.setUserData(userData);
        MessageData messageData = new MessageData();
        messageData.setMessage("Integration hello");
        dm.setMessageData(messageData);

        ResponseEntity<MessageData> response = restTemplate.postForEntity(
                url("/users/say"), dm, MessageData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Integration hello");
    }

    @Test
    void sayMessage_withWrongPassword_returnsBadRequest() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser4");
        userData.setPassword("pass4");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);

        UserData wrongCredentials = new UserData();
        wrongCredentials.setLogin("integrationUser4");
        wrongCredentials.setPassword("wrongpass");
        DirectMessage dm = new DirectMessage();
        dm.setUserData(wrongCredentials);
        MessageData messageData = new MessageData();
        messageData.setMessage("Should fail");
        dm.setMessageData(messageData);

        HttpClientErrorException.BadRequest exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForEntity(url("/users/say"), dm, String.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllUsers_returnsRegisteredUsers() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser5");
        userData.setPassword("pass5");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);

        ResponseEntity<UserData[]> response = restTemplate.getForEntity(
                url("/users/all"), UserData[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting("login")
                .contains("integrationUser5");
    }

    @Test
    void getMessagesByUser_returnsStoredMessages() {
        UserData userData = new UserData();
        userData.setLogin("integrationUser6");
        userData.setPassword("pass6");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);

        DirectMessage dm = new DirectMessage();
        dm.setUserData(userData);
        MessageData messageData = new MessageData();
        messageData.setMessage("Stored message");
        dm.setMessageData(messageData);
        restTemplate.postForEntity(url("/users/say"), dm, MessageData.class);

        ResponseEntity<MessageData[]> response = restTemplate.getForEntity(
                url("/messages/all?login=integrationUser6"), MessageData[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getMessage()).isEqualTo("Stored message");
    }
}

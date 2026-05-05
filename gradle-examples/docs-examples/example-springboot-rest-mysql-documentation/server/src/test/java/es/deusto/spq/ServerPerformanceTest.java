package es.deusto.spq;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import es.deusto.spq.serializable.DirectMessage;
import es.deusto.spq.serializable.MessageData;
import es.deusto.spq.serializable.UserData;

/**
 * Performance tests for critical REST endpoints using JUnitPerf.
 * Equivalent to ServerPerformanceTest in the Jersey/Maven baseline.
 *
 * Runs with: ./gradlew :server:performanceTest
 * HTML report: server/build/reports/junitperf/report.html
 */
@Tag("performance")
@ExtendWith(JUnitPerfInterceptor.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerPerformanceTest {

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig REPORTER_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator("build/reports/junitperf/report.html"))
            .build();

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    /**
     * Pre-seed the registered user that performance tests depend on.
     * Using PER_CLASS lifecycle, @BeforeAll has access to the injected port.
     */
    @BeforeAll
    void seedData() {
        restTemplate = new RestTemplate();
        UserData seedUser = new UserData();
        seedUser.setLogin("perfUser");
        seedUser.setPassword("perfPass");
        restTemplate.postForEntity(url("/users/add"), seedUser, String.class);
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000, warmUpMs = 500)
    void registerUser_underLoad() {
        UserData userData = new UserData();
        userData.setLogin("loadUser");
        userData.setPassword("loadPass");
        restTemplate.postForEntity(url("/users/add"), userData, String.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000, warmUpMs = 500)
    void sayMessage_underLoad() {
        UserData userData = new UserData();
        userData.setLogin("perfUser");
        userData.setPassword("perfPass");
        MessageData messageData = new MessageData();
        messageData.setMessage("Performance test message");
        DirectMessage dm = new DirectMessage();
        dm.setUserData(userData);
        dm.setMessageData(messageData);
        restTemplate.postForEntity(url("/users/say"), dm, MessageData.class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000, warmUpMs = 500)
    void getAllUsers_underLoad() {
        restTemplate.getForEntity(url("/users/all"), UserData[].class);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000, warmUpMs = 500)
    void getMessagesByUser_underLoad() {
        restTemplate.getForEntity(
                url("/messages/all?login=perfUser"), MessageData[].class);
    }
}

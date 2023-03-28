package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.print.attribute.standard.Media;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import categories.IntegrationTest;
import categories.PerformanceTest;
import es.deusto.spq.pojo.DirectMessage;
import es.deusto.spq.pojo.MessageData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.server.jdo.User;


@Category({IntegrationTest.class, PerformanceTest.class})
public class ResourcePerfTest {

    private static HttpServer server;
    private WebTarget target;

    @Rule 
	public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));
 
    @BeforeClass
    public static void prepareServer() throws Exception {
        // start the server
        server = Main.startServer();
    }

    @Before
    public void setUp() {
        System.out.println("EOOOOOOOOOOO");
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI).path("resource");

        prepareDatabase();
    }

    private void prepareDatabase() {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
            pm.makePersistent(new User("john", "1234"));
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdown();
    }

    // @Test
    // @JUnitPerfTest(threads = 20, durationMs = 2000)
    // @JUnitPerfTestRequirement(maxLatency = 2)
    // public void testGetHelloWorldPerf() {
    //     Response response = target.path("hello").request().get();
    //     assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
    //     assertEquals("Hello world!", response.readEntity(String.class));
    // }

    @Test
    @JUnitPerfTest(threads = 20, durationMs = 2000)
    @JUnitPerfTestRequirement(maxLatency = 2)
    public void testSayMessagePerf() {
        DirectMessage directMessage = new DirectMessage();
        UserData userData = new UserData();
        userData.setLogin("john");
        userData.setPassword("1234");
        directMessage.setUserData(userData);

        MessageData messageData = new MessageData();
        messageData.setMessage("This is a message!");
        directMessage.setMessageData(messageData);

        Response response = target.path("sayMessage")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(directMessage, MediaType.APPLICATION_JSON));

        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
    }
}
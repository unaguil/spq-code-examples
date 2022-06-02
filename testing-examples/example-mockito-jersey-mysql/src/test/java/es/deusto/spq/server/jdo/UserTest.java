package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;



public class UserTest {
    
    private User user;

    @Before
    public void setUp() {
        user = new User("test-login", "passwd");
    }

    @Test
    public void testGetLogin() {
        assertEquals("test-login", user.getLogin());
    }

    @Test
    public void testGetPassword() {
        assertEquals("passwd", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpasswd");
        assertEquals("newpasswd", user.getPassword());
    }

    @Test
    public void testGetMessages() {
        assertTrue(user.getMessages().isEmpty());
    }

    @Test
    public void testAddMessage() {
        user.addMessage(new Message("hello"));
        assertEquals(1, user.getMessages().size());
    }

    @Test
    public void testRemoveMessage() {
        Message message = new Message("hello");
        
        user.addMessage(message);
        assertEquals(1, user.getMessages().size());
        
        user.removeMessage(message);
        assertTrue(user.getMessages().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = String.format("User: login --> test-login, password --> passwd, messages --> []");
        assertEquals(expected, user.toString());
    }
}

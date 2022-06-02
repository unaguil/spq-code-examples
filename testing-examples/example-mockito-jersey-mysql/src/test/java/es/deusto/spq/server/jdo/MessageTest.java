package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {
    
    private Message message;

    @Before
    public void setUp() {
        message = new Message("Hello world!");
    }

    @Test
    public void getUser() {
        User user = new User("somelogin", "passwd");
        message.setUser(user);
        assertEquals(user, message.getUser());
    }
}   

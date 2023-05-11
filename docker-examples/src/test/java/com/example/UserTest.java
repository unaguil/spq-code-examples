package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
 
    private User u;

    @Before
    public void setUp() {
        u = new User("John", "Smith");
    }

    @Test
    public void testGetName() {
        assertEquals("John", u.getName());
    }
}

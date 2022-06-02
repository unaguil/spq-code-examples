package es.deusto.testing.mockito;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.app.mechanism.CoffeeContainer;

public class CoffeeContainerTest {
 
    @Test
    public void testNewWaterContainer() {
        CoffeeContainer coffeeContainer = new CoffeeContainer(3);
        assertEquals(3, coffeeContainer.getCurrentVolume());
    }
}

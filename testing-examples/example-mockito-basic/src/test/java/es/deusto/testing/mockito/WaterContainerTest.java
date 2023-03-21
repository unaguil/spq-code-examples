package es.deusto.testing.mockito;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.app.mechanism.WaterContainer;

public class WaterContainerTest {
 
    @Test
    public void testNewWaterContainer() {
        WaterContainer waterContainer = new WaterContainer(3);
        assertEquals(3, waterContainer.getCurrentVolume());
    }
}

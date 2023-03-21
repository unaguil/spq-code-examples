package es.deusto.testing.mockito;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.app.data.Portion;
import com.app.exceptions.NotEnoughException;
import com.app.mechanism.AbstractContainer;
import com.app.mechanism.WaterContainer;

public class AbstractContainerTest {
 
    class TestContainer extends AbstractContainer {

        protected TestContainer(int volume) {
            super(volume);
        }

    }

    TestContainer testContainer;

    @Before
    public void setUp() {
        testContainer = new TestContainer(3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IllegalAccessException {
        new WaterContainer(-1);
    }

    @Test
    public void testGetCurrentVolume() {
        assertEquals(3, testContainer.getCurrentVolume());
    }

    @Test
    public void testGetTotalVolume() throws NotEnoughException {
        assertEquals(3, testContainer.getTotalVolume());
        
        testContainer.getPortion(Portion.MEDIUM);
        assertEquals(3, testContainer.getTotalVolume());
    }

    @Test
    public void testGetPortion() throws NotEnoughException { 
        assertEquals(3, testContainer.getCurrentVolume());
        
        testContainer.getPortion(Portion.SMALL);
        assertEquals(2, testContainer.getCurrentVolume());
        
        testContainer.getPortion(Portion.MEDIUM);
        assertEquals(0, testContainer.getCurrentVolume());
    }

    @Test(expected=NotEnoughException.class)
    public void testGetPortionNotEnoughException() throws NotEnoughException {  
        testContainer.getPortion(Portion.MEDIUM);
        testContainer.getPortion(Portion.SMALL);
        testContainer.getPortion(Portion.SMALL);
    }

    @Test
    public void testRefillContainer() throws NotEnoughException {
        assertEquals(3, testContainer.getCurrentVolume());
        testContainer.getPortion(Portion.LARGE);
        assertEquals(0, testContainer.getCurrentVolume());

        testContainer.refillContainer();
        assertEquals(3, testContainer.getCurrentVolume());
    }
}

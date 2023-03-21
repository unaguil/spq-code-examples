package es.deusto.testing.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.app.data.Portion;
import com.app.exceptions.NotEnoughException;
import com.app.mechanism.CoffeeContainer;
import com.app.mechanism.CoffeeMachine;
import com.app.mechanism.interfaces.ICoffeeMachine;
import com.app.mechanism.interfaces.IContainer;

@RunWith(MockitoJUnitRunner.class)
public class CoffeeMachineTest {

	ICoffeeMachine coffeeMachine;

	// mock object 
	IContainer coffeeContainer = mock(CoffeeContainer.class);

	@Mock
	IContainer waterContainer; 	// another way to create a mock object
							 	// using an annotation

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		coffeeMachine = new CoffeeMachine(coffeeContainer, waterContainer);
	}

	@After
	public void tearDown() {
		coffeeContainer = null;
		waterContainer = null;
		coffeeMachine = null;
	}

	@Test
	public void testMakeCoffee() throws NotEnoughException {
		when(coffeeContainer.getPortion(Portion.LARGE)).thenReturn(true);
		when(waterContainer.getPortion(Portion.LARGE)).thenReturn(true);

		assertTrue(coffeeMachine.makeCoffee(Portion.LARGE));
	}

	@Test
	public void testNotEnoughCoffeeException() throws NotEnoughException {
		when(coffeeContainer.getPortion(Portion.SMALL)).thenReturn(false);
		when(waterContainer.getPortion(Portion.SMALL)).thenReturn(true);

		assertFalse(coffeeMachine.makeCoffee(Portion.SMALL));
	}

	@Test
	public void testNotEnoughWaterException() throws NotEnoughException {
		when(coffeeContainer.getPortion(Portion.SMALL)).thenReturn(true);
		when(waterContainer.getPortion(Portion.SMALL)).thenReturn(false);

		assertFalse(coffeeMachine.makeCoffee(Portion.SMALL));
	}

	@Test
	public void testGetWaterContainer() {
		assertEquals(waterContainer, coffeeMachine.getWaterContainer());
	}

	@Test
	public void testCoffeeContainer() {
		assertEquals(coffeeContainer, coffeeMachine.getCoffeeContainer());
	}
}

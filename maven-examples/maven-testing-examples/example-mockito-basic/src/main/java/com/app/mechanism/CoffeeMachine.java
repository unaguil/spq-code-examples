package com.app.mechanism;

import com.app.data.Portion;
import com.app.exceptions.NotEnoughException;
import com.app.mechanism.interfaces.ICoffeeMachine;
import com.app.mechanism.interfaces.IContainer;

public class CoffeeMachine implements ICoffeeMachine {

	private IContainer coffeeContainer;
	private IContainer waterContainer;

	public CoffeeMachine(IContainer cContainer, IContainer wContainer) {
		coffeeContainer = cContainer;
		waterContainer = wContainer;
	}

	@Override
	public boolean makeCoffee(Portion portion) throws NotEnoughException {

		boolean isEnoughCoffee = coffeeContainer.getPortion(portion);
		boolean isEnoughWater = waterContainer.getPortion(portion);

		return isEnoughCoffee && isEnoughWater;
	}

	@Override
	public IContainer getWaterContainer() {
		return waterContainer;
	}

	@Override
	public IContainer getCoffeeContainer() {
		return coffeeContainer;
	}

}

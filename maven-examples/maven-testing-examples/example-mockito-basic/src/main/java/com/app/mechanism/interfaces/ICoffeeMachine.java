package com.app.mechanism.interfaces;

import com.app.data.Portion;
import com.app.exceptions.NotEnoughException;

public interface ICoffeeMachine {

	public boolean makeCoffee(Portion portion) throws NotEnoughException;

	public IContainer getCoffeeContainer();

	public IContainer getWaterContainer();

}

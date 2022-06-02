package com.app.mechanism.interfaces;

import com.app.data.Portion;
import com.app.exceptions.NotEnoughException;

public interface IContainer {

	public boolean getPortion(Portion portion) throws NotEnoughException;

	public int getCurrentVolume();

	public int getTotalVolume();

	public void refillContainer();

}

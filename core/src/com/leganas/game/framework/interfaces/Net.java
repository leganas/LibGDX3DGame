package com.leganas.game.framework.interfaces;

public abstract class Net<T> {
	// Храним экземпляр игрового контроллера 
	public T gameController;

	public Net(T gameController) {
		super();
		this.gameController = gameController;
	}
}

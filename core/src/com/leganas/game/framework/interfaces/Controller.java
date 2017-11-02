package com.leganas.game.framework.interfaces;

import com.badlogic.gdx.utils.Disposable;

/**Абстрактный контроллер*/
public abstract class Controller<T> implements Disposable{
	
	public T items;
	
	public T getItems() {
		return (T) items;
	}

	public void setItems(T items) {
		this.items = items;
	}

	public abstract void init();

	public abstract void update(float delta);
	
	public abstract void render(float delta);

	@Override
	public void dispose() {
	}
}

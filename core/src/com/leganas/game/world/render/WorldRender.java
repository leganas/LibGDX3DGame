package com.leganas.game.world.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.world.GWorld;

public abstract class WorldRender  implements Disposable{
	protected GWorld world;
	
	
	public WorldRender() {
	}
	
	

	public WorldRender(GWorld world) {
		super();
		this.world = world;
	}

	

	public GWorld getWorld() {
		return world;
	}



	public void setWorld(GWorld world) {
		this.world = world;
	}



	public abstract void render(float delta);

	@Override
	public void dispose() {
	}
	
	public abstract Camera getCamera();
}

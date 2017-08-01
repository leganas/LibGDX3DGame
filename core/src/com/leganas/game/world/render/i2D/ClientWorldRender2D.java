package com.leganas.game.world.render.i2D;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.world.GWorld;

public class ClientWorldRender2D extends WorldRender2D implements Disposable{
	GWorld world;


	public ClientWorldRender2D(GWorld world) {
		super(world);
	}
	
	public void render(float delta){
		if (world != null) world.update(delta);
		// рисуем мир
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}

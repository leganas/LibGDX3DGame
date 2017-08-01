package com.leganas.game.world.render.i2D;

import com.leganas.game.world.GWorld;

public class ServerWorldRender2D extends WorldRender2D{

	public ServerWorldRender2D(GWorld world) {
		super(world);

	}
	
	public void render(float delta){
		world.update(delta);
		// рисуем мир
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}

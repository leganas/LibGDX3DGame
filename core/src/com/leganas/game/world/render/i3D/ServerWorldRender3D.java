package com.leganas.game.world.render.i3D;

import com.leganas.game.Assets;
import com.leganas.game.world.GWorld;

public class ServerWorldRender3D extends WorldRender3D{

	public ServerWorldRender3D(GWorld world) {
		super(world);

	}
	
	public void render(float delta){
		super.render(delta);
		Assets.textInfo = "| Entity count = " + world.entityController.items.size;
		// рисуем мир
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}

package com.leganas.game.world.render.i3D;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.Assets;
import com.leganas.game.world.GWorld;

public class ClientWorldRender3D extends WorldRender3D implements Disposable{


	public ClientWorldRender3D(GWorld world) {
		super(world);
	}
	
	@Override
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

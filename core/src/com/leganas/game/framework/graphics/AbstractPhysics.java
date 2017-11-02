package com.leganas.game.framework.graphics;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.world.entity.Entity;

public abstract class AbstractPhysics implements Disposable {
	
	/**Добавляет сущность в физический мир*/
	public abstract void addEntity(Entity ent);

	public abstract void removeEntity(Entity ent);
		
}

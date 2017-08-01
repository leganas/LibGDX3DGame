package com.leganas.game.world.entity.gameObject.i3D;

import com.leganas.game.framework.graphics.AbstractModel.model3DStatus;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.EntityUpdateType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;

public class Primitive3D extends Entity3D {
	public float direction=0;
	public int heals=100;

	@Override
	public void update(float delta) {
		// TODO Автоматически созданная заглушка метода
		super.update(delta);
		Model3DPhysics mod = (Model3DPhysics) model;
		if (mod.status == model3DStatus.outOfRange || status == EntityStatus.destroy) {
			EntityCommand.UpdateEntity update = new UpdateEntity();
			update.id = id;
			update.entityUpdateType = EntityUpdateType.Delete;
			entityListener.EntityEvent(update);
		}
	}

	@Override
	public void modelMessage(int modelID, Object msg) {
		// TODO Автоматически созданная заглушка метода
		
	}

	
}

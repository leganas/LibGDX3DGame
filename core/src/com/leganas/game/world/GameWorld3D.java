package com.leganas.game.world;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.leganas.game.Setting;
import com.leganas.game.framework.graphics.AbstractModel.model3DStatus;
import com.leganas.game.framework.graphics.engine3D.Physics;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.Entity.EntityStatus;
import com.leganas.game.world.entity.Entity.EntityType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.EntityUpdateType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;
import com.leganas.game.world.entity.gameObject.i3D.Entity3D;
import com.leganas.game.world.entity.gameObject.i3D.Primitive3D;

public class GameWorld3D extends GWorld{
	/**Генератор случайных числе*/
	Random random;
	/**Физика нашего мира + обработка столкновений в рамках этого мира*/
	public Physics physics;
	/**Сохраненное значение текущей гравитации*/
	public Vector3 gravity;
	/**Увеличилка временная*/

	
	public GameWorld3D() {
		super();
		random = new Random();
		gravity = new Vector3(0,-13,0);
		physics = new Physics(gravity);
		entityController.setPhysics(physics);
	    if (Setting.isServer()) generateworld();
		
	}
	
	private void generateworld() {
        // Создаем землю
		Matrix4 mtx = new Matrix4();
		mtx.setTranslation(new Vector3(0,-5,0));
		entityController.entityCommandManager.addEventToQueue(new EntityCommand.UpdateEntity(-1, -1, EntityType.Ground, EntityUpdateType.Create, "Ground", mtx));
	}
	
	float deltaP=0;

	@Override
	public void update(float delta) {
		// Симуляция игрового мира
		if (Setting.isServer()) physics.dynWorld.stepSimulation(delta, 5, 1f/60f);
		super.update(delta);
		if (id == 0) {
//			Gdx.app.log("","");
		}
		if (id == 1) {
//			Gdx.app.log("","");
		}
		deltaP += delta;
		if (physics.dynWorld.getNumCollisionObjects() < 30 && deltaP > 1) {
			Matrix4 mtx = new Matrix4();
			mtx.setTranslation(new Vector3(0,30,0));
			deltaP = 0;
//			Gdx.app.log("LGame","world " + id + " NumCollisionObjects " + physics.dynWorld.getNumCollisionObjects());
			entityController.entityCommandManager.addEventToQueue(new EntityCommand.UpdateEntity(-1, -1, EntityType.Primitive, EntityUpdateType.Create, null, mtx));
		}

	}

	@Override
	public void render(float delta) {
//		if (entityController.get(1) != null) Gdx.app.log("",""+((Model3D)((Entity3D)entityController.get(1)).model).instance.transform);
		super.render(delta);
	}

	@Override
	public void entityControllerMessage(Object msg) {
		if (msg instanceof String) {
			Gdx.app.log("LGame","World - " + id + (String)msg);
		}
		super.entityControllerMessage(msg);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
			int index1, boolean match1) {
		if (entityController.get(userValue0) == null) Gdx.app.log("LGame","world id = "+id+" | entity - "+userValue0 + " null"); 
		if (entityController.get(userValue1) == null) Gdx.app.log("LGame","world id = "+id+" | entity - "+userValue1 + " null"); 
		if (entityController.get(userValue0) != null && entityController.get(userValue1) != null)
		{
		 Gdx.app.log("LGame","world id = "+id+" contact : " + entityController.get(userValue0).type+" | "+entityController.get(userValue1).type);
		 Entity ent0,ent1;
		 ent0 = entityController.get(userValue0);
		 ent1 = entityController.get(userValue1);
		 if (ent0 instanceof Primitive3D) {
			 float damage = 0;
			 if (((Primitive3D)ent0).type == EntityType.Bullet) damage = -50;
			 if (((Primitive3D)ent0).type == EntityType.Primitive) damage = -50;
			 EntityCommand.UpdateEntity update = new UpdateEntity();
			 update.id = ent0.id;
			 update.heals = ent0.heals + damage;
			 update.entityUpdateType = EntityUpdateType.Damage;
			 entityController.entityCommandManager.addEventToQueue(update);
		 }
		 if (ent1 instanceof Primitive3D) {
			 float damage = 0;
			 if (((Primitive3D)ent1).type == EntityType.Bullet) damage = -50;
			 if (((Primitive3D)ent1).type == EntityType.Primitive) damage = -50;
			 EntityCommand.UpdateEntity update = new UpdateEntity();
			 update.id = ent1.id;
			 update.heals = ent1.heals + damage;
			 update.entityUpdateType = EntityUpdateType.Damage;
			 entityController.entityCommandManager.addEventToQueue(update);
		 }
		 
		 
		}
		return false;
	}

	public void onContactProcessed(int userValue0, int userValue1) {
	}
}

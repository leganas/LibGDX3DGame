package com.leganas.game.world.entity.gameObject.i3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.ArrayMap;
import com.leganas.game.Assets;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv;
import com.leganas.game.framework.graphics.engine3D.Physics;
import com.leganas.game.world.entity.Entity.EntityType;

public abstract class Constructor3D {
	// флаги дл€ задани€ фильтров столкновений моделей в физ. мире
	public final static short BULLET_FLAG = 1 << 6;
	public final static short ROBOT_FLAG = 1 << 7;
	public final static short GROUND_FLAG = 1 << 8;
	public final static short PRIMITIVE_FLAG = 1 << 9;
	public final static short ALL_FLAG = -1;

	
	/**—оздает 3D сущность заданного типа
	 * @param type - “ип сущности
	 * @param name - »м€ составной части модели (null) - если модель это цельный объект (используетс€ только при создании примитивов пока)
	 * @param playerID - сетевой идентификатор получаемый от сетевого (сервера или клиента) дл€ сущности типа Player3D
	 * @param id - внутри игровой идентификатор сущности дл€ физ. мира
	 * @param transform - матрица трансформации в 3D пространстве дл€ модельки
	 * @param impuls - начальный импульс который будет задан дл€ модели в пространстве физ. мира*/
	public static Entity3D create3DEntity(EntityType type, String name, int playerID, Matrix4 transform, Vector3 impuls){
		Entity3D ent = createModel3DBody(type, transform, name, impuls);
		ent.setType(type);
		if (name != null) ent.setName(name);
		ent.setPlayerID(playerID);
		return ent;
	}
	
	static Entity3D createModel3DBody(EntityType type, Matrix4 transform, String name, Vector3 impuls){
		Entity3D entity;
		Model3DPhysics model;
		int rnd=0;
		short typeModel=-2, typeFilter=-2; // -2 - это если не нужно учитывать тип модели или фильтра
		if (type == EntityType.Primitive){ 
			typeModel = PRIMITIVE_FLAG;
//			typeFilter = ALL_FLAG;
		}
		if (type == EntityType.Bullet){ 
			typeModel = BULLET_FLAG;
			typeFilter = PRIMITIVE_FLAG | ROBOT_FLAG;
		}
		if (type == EntityType.Player){ 
			typeModel = ROBOT_FLAG;
//			typeFilter = ALL_FLAG;
		}
		if (type == EntityType.Ground){ 
			typeModel = GROUND_FLAG;
//			typeFilter = ALL_FLAG;
		}
		if (type == EntityType.Primitive || type == EntityType.Bullet) {
			entity = new Primitive3D();
			// временно
			ArrayMap<String, ModelInstanceAdv.Constructor> con = MySyper2DGame.inst.assets.constructors;
			if (name == null) {
				rnd = MathUtils.random(con.size-1); 
			} else{
				rnd = con.indexOfKey(name);
			}
			entity.setName(con.getKeyAt(rnd));
//			Gdx.app.log("LGame","Type "+type+" "+entity.name + " | typeModel=" + typeModel + " | typeFilter = " + typeFilter);
			
			ModelInstanceAdv.Constructor temp = MySyper2DGame.inst.assets.constructors.values[rnd];

			model = createModel3D(entity,temp);
			model.environmentEconom = true;
			
			entity.setModel3D(model);
			model.instance.transform.set(transform);
			model.instance.body.setActivationState(Collision.DISABLE_DEACTIVATION);
			model.instance.body.proceedToTransform(model.instance.transform);
			model.instance.body.setCollisionFlags(model.instance.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			if (impuls != null){
				MySyper2DGame.inst.assets.sound.get("shot").play();
				model.instance.body.applyImpulse(impuls, new Vector3(0,0,0));
			}
			model.instance.setTransformlistener(model);
			
			if (typeModel != -2) model.instance.body.setContactCallbackFlag(typeModel); // флаг определ€ющий тип модельки
			if (typeFilter != -2) model.instance.body.setContactCallbackFilter(typeFilter); // флаг определ€ющий с кем нужно отслеживать столкновени€ (то-есть при столкновении с какими объектами будет срабатывать слушатель contacter)
			
			return entity;
		}
		if (type == EntityType.Player) {
			entity = new Player3D();
			// временно
			ModelInstanceAdv.Constructor temp = new ModelInstanceAdv.Constructor(MySyper2DGame.inst.assets.model3D.get(5), Physics.createConvexHullShape(MySyper2DGame.inst.assets.model3D.get(5), true, 0), 10f);
			model = createModel3D(entity,temp);
			model.environmentEconom = true;
			entity.setModel3D(model);
			model.instance.transform.set(transform);
			model.instance.body.setActivationState(Collision.DISABLE_DEACTIVATION);
			model.instance.body.proceedToTransform(model.instance.transform);
			model.instance.body.setAngularFactor(new Vector3(0f, 0f, 0f));
			model.instance.body.setCollisionFlags(model.instance.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			model.instance.setTransformlistener(model);
			((Player3D)entity).animController = new AnimationController(model.instance);
			((Player3D)entity).animController.setAnimation(((Player3D)entity).animateName, -1);

			if (typeModel != -2) model.instance.body.setContactCallbackFlag(typeModel); // флаг определ€ющий тип модельки
			if (typeFilter != -2) model.instance.body.setContactCallbackFilter(typeFilter); // флаг определ€ющий с кем нужно отслеживать столкновени€ (то-есть при столкновении с какими объектами будет срабатывать слушатель contacter)

			return entity;
		}
		if (type == EntityType.Ground) {
			entity = new Ground3D();
	        ModelInstanceAdv.Constructor temp = new ModelInstanceAdv.Constructor(MySyper2DGame.inst.assets.model3D.get(4), Bullet.obtainStaticNodeShape(MySyper2DGame.inst.assets.model3D.get(4).nodes), 0f);
			model = createModel3D(entity,temp);
			entity.setModel3D(model);
			model.instance.transform.set(transform);
			model.instance.body.proceedToTransform(model.instance.transform);
//			model.instance.body.setUserValue(id);
			model.instance.body.setCollisionFlags(model.instance.body.getCollisionFlags()| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

			if (typeModel != -2) model.instance.body.setContactCallbackFlag(typeModel); // флаг определ€ющий тип модельки
			if (typeFilter != -2) model.instance.body.setContactCallbackFilter(typeFilter); // флаг определ€ющий с кем нужно отслеживать столкновени€ (то-есть при столкновении с какими объектами будет срабатывать слушатель contacter)
			
			return entity;
		}
		return null;
	}

	// местные методы приватные дл€ уменьшени€ количества текста в методе create3DEntity
	public static Model3DPhysics createModel3D(Entity3D ent, ModelInstanceAdv.Constructor contructor){
		Model3DPhysics model = new Model3DPhysics();
		model.instance = contructor.create();
        return model;
	}
	

}

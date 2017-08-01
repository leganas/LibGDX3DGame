package com.leganas.game.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.leganas.game.Setting;
import com.leganas.game.framework.graphics.AbstractPhysics;
import com.leganas.game.framework.interfaces.Controller;
import com.leganas.game.world.entity.Entity.EntityListener;
import com.leganas.game.world.entity.cmdEngine.EntityCommandManager;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;


public class EntityController extends Controller<IntMap<Entity>> implements Disposable, EntityListener{
	public interface EntityControllerListener{
		public void entityControllerMessage(Object msg);
	}
	
	public static IntMap<Integer> entityID_worldID;
	
	public EntityCommandManager entityCommandManager;
	public EntityControllerListener listener;
	public AbstractPhysics physics;
	public int worldid;
	public EntityController(int worldid) {
		this.worldid = worldid;
		items = new IntMap<Entity>();
		if (entityID_worldID == null) entityID_worldID = new IntMap<Integer>(); 
		entityCommandManager = new EntityCommandManager(this);
	}
	
	
	/**Ќаходит ID сущности к которой прив€зан игрок с номером playerID
	 * если -1 то нет такого игрока среди сущностей данного мира*/
	public int getEntityIDformPlayerID(int playerID){
		for (Entry<Entity> tree : new IntMap.Entries<Entity>(items)) {
			if (tree.value.playerID == playerID) return tree.value.id;
		}
		return -1;
	}
	
	
	public AbstractPhysics getPhysics() {
		return physics;
	}



	public void setPhysics(AbstractPhysics physics) {
		this.physics = physics;
	}



	public void setListener(EntityControllerListener listener) {
		this.listener = listener;
	}


	public synchronized int getAcceptID() {
		int id=-1;
		// так были разные ID дл€ каждого мира 
/*		do {
		    id++;
		} while ((id > items.size) || items.containsKey(id));*/
		
		// один массив ID сущностей дл€ всех миров из-за тупости контактера
		do {
			id++;
		} while ((id > entityID_worldID.size) || entityID_worldID.containsKey(id));
		return id;
		
	}
	
	public synchronized void put(int id,Entity item){
		synchronized (items) {
			item.setEntityListener(this);
			items.put(id, item);
			entityID_worldID.put(id, worldid);
			if (Setting.isServer())
				physics.addEntity(item);
			listener.entityControllerMessage(" new Entity id = " + id);
		}
	}
	
	public synchronized int add(Entity item){
		int id = getAcceptID();
//		Gdx.app.log("LGAme","set id = "+ id);
		put(id,item);
		return id;
	}
	
	public void remove(int key){
		synchronized (items) {
			physics.removeEntity(items.get(key));
			items.remove(key);
			entityID_worldID.remove(key);
			Gdx.app.log("LGame", "World - " + worldid + " Entity є" + key + " delete");
		}
	}
	
	public Entity get(int key){
		synchronized (items) {
			return items.get(key);
		}
	}
	
	public int getID(Entity item){
		return items.findKey(item, true, -1);
	}

	
	@Override
	public void init() {
		// TODO јвтоматически созданна€ заглушка метода
		
	}

	public void update(float delta) {
		synchronized (items) {
			for (Entry<Entity> tree : new IntMap.Entries<Entity>(items))
				if (tree.value != null) tree.value.update(delta);
		}
		entityCommandManager.process();
	}

	@Override
	public void dispose() {
		synchronized (items) {
			for (Entry<Entity> tree : new IntMap.Entries<Entity>(items))
				if (tree.value != null) tree.value.dispose();
			items.clear();
		}
	}

	@Override
	public void render(float delta) {
	}



	@Override
	public void EntityEvent(Object event) {
		if (event instanceof EntityCommand) {
			entityCommandManager.addEventToQueue((EntityCommand)event);
		}
	}

}

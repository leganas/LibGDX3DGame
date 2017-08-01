package com.leganas.game.world.entity.cmdEngine;

import com.leganas.game.framework.interfaces.Manager;
import com.leganas.game.world.entity.EntityController;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;


public class EntityCommandManager extends Manager<EntityCommand>{
	EntityController entityController;
	
	
	public EntityCommandManager(EntityController entityManager){
		super(entityManager, -1);
		this.entityController = entityManager;
	}
	
	public void process() {
		synchronized (eventQueue) {
			for (EntityCommand event : eventQueue) {
				if (event != null) entityController.listener.entityControllerMessage(event.ApplyCommand(entityController));
			}
			eventQueue.clear();
		}
	}

	public void addEventToQueue(EntityCommand event) {
		eventQueue.add(event);
	}
}

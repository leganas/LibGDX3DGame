package com.leganas.game.controller.cmdEngine;

import com.leganas.game.controller.cmdEngine.command.ClientGameCommand;
import com.leganas.game.framework.interfaces.Controller;
import com.leganas.game.framework.interfaces.Manager;


/**Менеджер для управления событиями получаемыми от ClientGameController*/
public class ClientGameCommandManager extends Manager<ClientGameCommand>{
	
	public ClientGameCommandManager(Controller<?> controller, int id) {
		super(controller,id);
	}

	
	public void process() {
		 synchronized (eventQueue) {
			 for (ClientGameCommand event : eventQueue) {
				 if (event instanceof ClientGameCommand) {
					 event.Apply(controller,id);
				 }
			 }
			 eventQueue.clear();
		 }
	}
}

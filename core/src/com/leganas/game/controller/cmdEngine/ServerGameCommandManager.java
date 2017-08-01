package com.leganas.game.controller.cmdEngine;

import com.leganas.game.controller.cmdEngine.command.ServerGameCommand;
import com.leganas.game.framework.interfaces.Controller;
import com.leganas.game.framework.interfaces.Manager;


public class ServerGameCommandManager extends Manager<ServerGameCommand>{
	

	public ServerGameCommandManager(Controller<?> controller) {
		super(controller,-1);
	}

	public void process() {
		 synchronized (eventQueue) {
			 for (ServerGameCommand event : eventQueue) {
				 event.Apply(controller,id);
			 }
			 eventQueue.clear();
		 }
	}
}

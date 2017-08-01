package com.leganas.game.controller.cmdEngine.command;

import com.badlogic.gdx.Gdx;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.framework.interfaces.Command;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.world.GameWorld3D;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.render.i3D.ServerWorldRender3D;

/**Команды серверу на выполнение (Event)*/
public abstract class ServerGameCommand extends Command<ServerGameController>{
	
	public ServerGameCommand() {
	}
	
	public static class CreateNewWorld extends ServerGameCommand {
		public String name;
		public CreateNewWorld() {
			super();
		}
		public CreateNewWorld(String name) {
			super();
			this.name = name;
		}
		
		public Event ApplyCommand(ServerGameController controller) {
			GWorld wd = new GameWorld3D();
			wd.setId(controller.getAcceptID());
			wd.setName(name);
			wd.setWorldRender(new ServerWorldRender3D(wd));
			wd.setListener(controller);
			controller.add(wd);
			Gdx.app.log("LGame", "Server create new World № "+wd.getId()+" | "+ name);
			
			return null;
		}
	}
	
}

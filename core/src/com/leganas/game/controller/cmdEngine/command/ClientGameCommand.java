package com.leganas.game.controller.cmdEngine.command;

import com.badlogic.gdx.Gdx;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.framework.interfaces.Command;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.world.GameWorld3D;

/**Команды серверу на выполнение (Event)*/
public abstract class ClientGameCommand extends Command<ClientGameController>{

	public ClientGameCommand() {
	}
	
	public static class CreateWorld extends ClientGameCommand {
		public int id;
		public String name;
		public CreateWorld() {
			super();
		}
		public CreateWorld(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		
		@Override
		public Event ApplyCommand(ClientGameController controller) {
			controller.items = new GameWorld3D();
			controller.items.setId(id);
			controller.items.setName(name);
			controller.render.setWorld(controller.items);
			controller.items.setWorldRender(controller.render);
			controller.items.setListener(controller);
			Gdx.app.log("LGame","netClient : RequestJoinToWorld "+ id);
			controller.client.sendMessage(new ClientMessage.RequestJoinToWorld(id));
			return null;
		}
		
	}
	
}

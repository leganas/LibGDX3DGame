package com.leganas.game.network.packeges.serverTOclient;

import com.badlogic.gdx.Gdx;
import com.leganas.game.Assets;
import com.leganas.game.Setting;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.controller.ClientGameController.ClientState;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.framework.interfaces.Message;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;

/**Ответы сервера на запросы клиентов*/
public abstract class ServerMessage extends Message<ClientGameController>{
	/**Реализация серверного сообщения (на стороне клиента)*/
	public abstract ClientMessage ResponseServerMessage(ClientGameController controller);
	

	@Override
	public Event ResponseMessage(ClientGameController controller, int id) {
		// Глушим базовый метод по средством вызова своего собственного абстрактного replyServerMessage
		return ResponseServerMessage(controller);
	}


		/**Возвращаем тех. информацию о сервере*/
	public static class ReturnServerInfo extends ServerMessage{

		/**Возвращаем тех. информацию о сервере*/
		public ReturnServerInfo() {
			super();
		}

		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
			return null;
		}

	}
	
	/**Возвращаем ID и имена всех игровых миров доступных на сервере*/
	public static class ReturnAllWorldId extends ServerMessage{
		
		public int[] worldID;
		public String[] worldName;

		/**Возвращаем ID и имена всех игровых миров доступных на сервере*/
		public ReturnAllWorldId() {
			super();
		}

		public ReturnAllWorldId(int[] worldID, String[] worldName) {
			super();
			this.worldID = worldID;
			this.worldName = worldName;
		}

		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
			controller.setWorldID(worldID);
			controller.setWorldName(worldName);
			controller.clientState = ClientState.waitUserSelectWorld;
			return null;
		}

	}
	
	/**Ответ клиенту на запрос подключения к игровому миру*/
	public static class ReturnAcceptJoinToWorld extends ServerMessage{
		boolean flag;
	
		public ReturnAcceptJoinToWorld() {
			super();
		}
		
		
	
		public ReturnAcceptJoinToWorld(boolean flag) {
			super();
			this.flag = flag;
		}
	
		


		public boolean isFlag() {
			return flag;
		}



		public void setFlag(boolean flag) {
			this.flag = flag;
		}



		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
			Gdx.app.log("LGame","netClient : ReturnAcceptJoinToWorld " + isFlag());
			if (isFlag()) {
				controller.clientState = ClientState.requestAcceptNewPlayerWorld; // переходим в режим получения ID для Игрока если вход в игровой мир разрешён 
				Gdx.app.log("LGame","netClient controller.items " + controller.items);
				Gdx.app.log("LGame","netClient : RequestAcceptNewPlayerWorld "+ controller.worldID[controller.items.getId()]);
				controller.client.sendMessage(new ClientMessage.RequestAcceptNewPlayerWorld(controller.items.getId(), Setting.getClientName()));
				controller.clientState = ClientState.wait;
			}
			return null;
		}
	
	}

	/**Возвращает ID сущности игрока созданного сервером по запросу конкретного клиента 
	 * (является командой для создания этим клиентом сущности типа EntityType.Player с указанным id)
	 * данная сущность привязывается к этому NETклиенту как его представление в игровом мире*/
	public static class ReturnAcceptNewPlayer extends ServerMessage{
		int entityid;
		
		public ReturnAcceptNewPlayer() {
			super();
		}


		public ReturnAcceptNewPlayer(int entityid) {
			super();
			this.entityid = entityid;
		}
	
	
		public int getEntityid() {
			return entityid;
		}


		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
//			controller.clientEntityID = entityid;
			Assets.playerId = entityid; // временно еще и сюда запихнём чтобы не усложнять 
			//controller.items.entityController.entityCommandManager.addEventToQueue(event);
			Gdx.app.log("LGame","netClient : ReturnAcceptNewPlayer " + Assets.playerId);
			controller.clientState = ClientState.requestWorldState;
			Gdx.app.log("LGame","netClientMsg : RequestWorldState "+ controller.worldID[controller.items.getId()]);
			controller.client.sendMessage(new ClientMessage.RequestWorldState(controller.items.getId()));
			controller.clientState = ClientState.readry;
			return null;
		}
	
	
	
	}

	/**Возвращаем комплект обновлений для клиента ServerUpdate с предопределенным EntityUpdate.Create
	 * что является командой для клиента на на создание новых сущностей указанных в переданном комплекте ServerUpdate*/
	public static class ReturnWorldState extends ServerMessage{
		public ServerUpdate update;

		/**Возвращаем комплект обновлений для клиента ServerUpdate с предопределенным EntityUpdate.Create
		 * что является командой для клиента на на создание новых сущностей указанных в переданном комплекте ServerUpdate*/
		public ReturnWorldState() {
			super();
		}

		public ReturnWorldState(ServerUpdate update) {
			super();
			this.update = update;
		}

		public ServerUpdate getUpdate() {
			return update;
		}

		public void setUpdate(ServerUpdate update) {
			this.update = update;
		}

		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
			Gdx.app.log("LGame "," ServerMessage.Apply ReturnWorldState");
			update.applyUpdate(controller.items);
			return null;
		}

	}
	
	public static class UniversalMessage extends ServerMessage{
		public Object msg;

		public UniversalMessage(Object msg) {
			super();
			this.msg = msg;
		}

		public UniversalMessage() {
			super();
		}

		public Object getMsg() {
			return msg;
		}

		public void setMsg(Object msg) {
			this.msg = msg;
		}

		@Override
		public ClientMessage ResponseServerMessage(ClientGameController controller) {
			 if (msg instanceof ServerUpdate) {
				 ServerUpdate mesage = (ServerUpdate) msg;
				 if (!Setting.isServer() && Setting.isClient()){
					 Gdx.app.log("LGame","netClient : ServerUpdate.Aplay size - " + mesage.getUpdateSize());
					 mesage.applyUpdate(controller.items);
				 }
			 }
			return null;
		}
	}
}

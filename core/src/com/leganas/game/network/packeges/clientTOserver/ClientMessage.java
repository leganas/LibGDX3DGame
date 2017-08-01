package com.leganas.game.network.packeges.clientTOserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.MySyper2DGame.GameState;
import com.leganas.game.Setting;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.framework.interfaces.Message;
import com.leganas.game.network.packeges.serverTOclient.ServerMessage;
import com.leganas.game.network.packeges.serverTOclient.ServerUpdate;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.GameWorld3D;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.Entity.EntityType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.EntityUpdateType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;
import com.leganas.game.world.entity.gameObject.i3D.Player3D;

/**Сообщения net клиента к серверу (запросы, команды на передвижение персонажа и.т.д.)*/
public abstract  class ClientMessage extends Message<ServerGameController>{
	/**Реализация клиентского сообщения (на стороне сервера)*/
	public abstract ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID);
	
	@Override
	public Event ResponseMessage(ServerGameController controller, int id) {
		return ResponseClientMessage((ServerGameController) controller, id);
	}

	/**Запрос информации о сервере*/
	public static class RequestServerInfo extends ClientMessage{
		int id;
		String name;
		
		public RequestServerInfo() {
			super();
		}

		@Override
		public ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
		    Gdx.app.log("LGame", "Server ReturnServerInfo to client №" + netClientID);
			return null;
		}

	}
	
	/**Запрос списка всех игровых миров*/
	public static class RequestAllWorldId extends ClientMessage{

		public RequestAllWorldId() {
			super();
		}
		
		public ServerMessage.ReturnAllWorldId ResponseClientMessage(ServerGameController controller, int netClientID) {
			// если клиент запросил список всех игровых миров и их ID
			Gdx.app.log("LGame", "Server ReturnAllWorldId to client №" + netClientID);
			int[] worldID = new int[controller.items.size];
			String[] worldName = new String[controller.items.size];
			int i=0;
			for (final Entry<GWorld> tree : controller.items.entries()) {
				worldID[i] = tree.value.getId();
				worldName[i] = tree.value.getName();
				i++;
			}
			ServerMessage.ReturnAllWorldId sMsg = new ServerMessage.ReturnAllWorldId();
			sMsg.worldID = worldID;
			sMsg.worldName = worldName;
			return sMsg;
		}

	}
	
	/**Запрос на подключение к игровому миру*/
	public static class RequestJoinToWorld extends ClientMessage{
		int worldID;
		public RequestJoinToWorld() {
			super();
		}
		
		public RequestJoinToWorld(int worldID) {
			super();
			this.worldID = worldID;
		}

		public int getWorldID() {
			return worldID;
		}
		public void setWorldID(int worldID) {
			this.worldID = worldID;
		}

		public ServerMessage.ReturnAcceptJoinToWorld ResponseClientMessage(ServerGameController controller, int netClientID) {
			Gdx.app.log("LGame", "Server ReturnAcceptJoinToWorld to client №" + netClientID);
			ServerMessage.ReturnAcceptJoinToWorld msg = new ServerMessage.ReturnAcceptJoinToWorld(true);
			return msg;
		}

	}

	/**	Запрос на создание игрока в игровом мире и получение для него ID*/
	public static class RequestAcceptNewPlayerWorld extends ClientMessage{
		int worldID;
		String name;

		public RequestAcceptNewPlayerWorld() {
			super();
		}


		public RequestAcceptNewPlayerWorld(int worldID, String name) {
			super();
			this.worldID = worldID;
			this.name = name;
		}

		

		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public int getWorldID() {
			return worldID;
		}

		public void setWorldID(int worldID) {
			this.worldID = worldID;
		}


		@Override
		public ServerMessage.ReturnAcceptNewPlayer ResponseClientMessage(ServerGameController controller, int netClientID) {
			 Gdx.app.log("LGame", "Server send to client № "+ netClientID +" ReturnAcceptNewPlayer");
			 
			 GWorld world =  controller.items.get(getWorldID());

				 // Если передаем 2D координату (возможно нужно тоже Matrix пока не знаю)
//				 Transform2D transform = new Transform2D(new Vector2(0,0));
			 int entityid;
			 synchronized (world) {
					// мы в данном случае работаем с 3D по сему передаем Transform3D
					Matrix4 mtx = new Matrix4();
					// Позиция (x,y,z)
					mtx.setTranslation(new Vector3(0,10,0));
					entityid = world.entityController.getAcceptID();
					EntityCommand event = new EntityCommand.UpdateEntity(netClientID, entityid, EntityType.Player,EntityUpdateType.Create, getName(), mtx);
					world.entityController.entityCommandManager.addEventToQueue(event);
					synchronized (controller.playerID_WorldID) {
						controller.playerID_WorldID.put(netClientID, world.getId());
					}
					ServerMessage.ReturnAcceptNewPlayer msg = new ServerMessage.ReturnAcceptNewPlayer(entityid);
					return msg;
				}
		}
	}
	
	
	/**Запрос состояния всех сущностей в игровом мире 
	 * (клиент ожидает в качестве ответа набор инструкций для создания всех сушностей мира и их координаты)*/
	public static class RequestWorldState extends ClientMessage{
		int worldID;
		
		public int getWorldID() {
			return worldID;
		}

		public void setWorldID(int worldID) {
			this.worldID = worldID;
		}

		public RequestWorldState() {
			super();
		}

		public RequestWorldState(int worldID) {
			super();
			this.worldID = worldID;
		}

		@Override
		public ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
			 Gdx.app.log("LGame", "Server send to client № "+ netClientID +" RequestWorldState");
			 int id = getWorldID();
			 ServerUpdate update = controller.createServerUpdate(controller.items.get(id),EntityUpdateType.Create,false);
			 ServerMessage.ReturnWorldState msg = new ServerMessage.ReturnWorldState();
			 msg.update = update;
			return msg;
		}
	}
	
	/**Обычный текстовый запрос (используется для различных целей 
	 * и введен в код для упрощения программирования на начальных 
	 * этапах проектирования логики клиент-сервер)*/
	public class StringMessage extends ClientMessage{
		int playerID;
		String key;
		
		public int getPlayerID() {
			return playerID;
		}

		public void setPlayerID(int playerID) {
			this.playerID = playerID;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public StringMessage(int playerID, String key) {
			super();
			this.playerID = playerID;
			this.key = key;
		}

		public StringMessage() {
			super();
		}

		@Override
		public ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
			return null;
		}

	}
	
	
	/**Сообщение содержащие строку (временно) с командами InputProcessor от клиента 
	 * (когда пользователь тискает на кнопки и пытается управлять персонажем)*/
	public static class ClientInputMsg extends ClientMessage{
		/**На какую сущность будет распространятся действие 
		 * (нужно передать id сущности игрока) -1 пусть сервер сам ищет*/
		public int entityPlayerID = -1; 
		/**Само сообщение*/
		public String msg;
		/**down - true; up - false*/
		public boolean status;
		/**Дополнительный числовой параметр 
		 * (для точпада, степень отклонения передается по модулю число от 0 до 1)*/
		public float param=-2; 
		
		/**Сообщение содержащие строку (временно) с командами InputProcessor от клиента 
		 * (когда пользователь тискает на кнопки и пытается управлять персонажем)*/
		public ClientInputMsg() {
			super();
		}
		
		
		
		public int getEntityPlayerID() {
			return entityPlayerID;
		}


		public void setEntityPlayerID(int entityPlayerID) {
			this.entityPlayerID = entityPlayerID;
		}



		public String getMsg() {
			return msg;
		}



		public void setMsg(String msg) {
			this.msg = msg;
		}



		public boolean isStatus() {
			return status;
		}



		public void setStatus(boolean status) {
			this.status = status;
		}



		public float getParam() {
			return param;
		}



		public void setParam(float param) {
			this.param = param;
		}



		/**Сообщение содержащие строку (временно) с командами InputProcessor от клиента 
		 * (когда пользователь тискает на кнопки и пытается управлять персонажем)*/
		public ClientInputMsg(String key,boolean status) {
			super();
			this.msg = key;
			this.status = status;
		}
		
		/**Сообщение содержащие строку (временно) с командами InputProcessor от клиента 
		 * (когда пользователь тискает на кнопки и пытается управлять персонажем)
		 * @param msg - само сообщение
		 * @param param - дополнительный параметр (к примеру степень поворота на лево отличная от 1 при повороте джойстиком)*/
		public ClientInputMsg(String msg,boolean status, float param) {
			super();
			this.msg = msg;
			this.status = status;
			this.param = param;
		}

		@Override
		public synchronized ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
			// Тут мы реагируем на нажатые кнопки пользователем (ну  в общем случае)
			if (param != -2)	{
				// если не равен -2 то управляли тачпадом
//				Gdx.app.log("LGame", "ClientInput touchpad| "+netClientID +" | "+ msg + " | " + status + " | " + param);
			} else {
				// если равен -2 то управляли кнопками
				param = 1; // присваиваем значение 1 чтобы двигаться в нужном направлении на 100%
//				Gdx.app.log("LGame", "ClientInput key| "+netClientID +" | "+ msg + " | " + status + " | " + param);
			}
			if (controller.playerID_WorldID.get(netClientID) == null) return null;
			int worldid = controller.playerID_WorldID.get(netClientID); // получаем id мира к которому привязан клиент
			GWorld world = controller.items.get(worldid); // получаем сам мир
			if (msg == "null") return null;
			int entid =-1;
			if (entityPlayerID == -1) {
				// если пользователь не указал сущность над которой нужно производить операцию то ищем сами
				entid = findPlayerIdFromWorld(world, netClientID);
				if (entid == -1) return null; // если не нашли выходим
			}
//			Gdx.app.log("LGame", "move entity " + entid);
			Entity ent = world.entityController.items.get(entid);
			if (ent instanceof Player3D) {
				Player3D plr = (Player3D) ent;
				if (msg.equals("null")) return null;
				EntityCommand.UpdateEntity cmd = null;
				if (msg.equals("skip_move")) {
					plr.forward = false;
					plr.back = false;
					plr.left = false;
					plr.right = false;
					plr.rotateAngle = 0;
					
					cmd = new UpdateEntity();
					cmd.entityUpdateType = EntityUpdateType.setAnim;
					cmd.id = ent.id;
					cmd.animateName = "robot_rig|Stay";
					cmd.animLoop = -1;
					cmd.animSetType = true;
				}
				if (msg.equals("forward")) {
					plr.forward = status;
					if (status == true) cmd = setAnim(plr.id, "robot_rig|Walk", -1, true); 
						else cmd = setAnim(plr.id, "robot_rig|Stay", -1, true); 
				} 
				if (msg.equals("back")) {
					plr.back = status;
					if (status == true) cmd = setAnim(plr.id, "robot_rig|Back", -1, true); 
					else cmd = setAnim(plr.id, "robot_rig|Stay", -1, true); 
				} 
				if (msg.equals("left")) {
					plr.left = status;
					if (status == true) cmd = setAnim(plr.id, "robot_rig|Left", -1, true); 
					else cmd = setAnim(plr.id, "robot_rig|Stay", -1, true); 
				} 
				if (msg.equals("right")) {
					plr.right = status;
					if (status == true) cmd = setAnim(plr.id, "robot_rig|Right", -1, true); 
					else cmd = setAnim(plr.id, "robot_rig|Stay", -1, true); 
				} 
				if (msg.equals("rotate_left")) {
					if (status == false) {
						plr.rotateAngle = 0;
					} else plr.rotateAngle = param;
				} 
				if (msg.equals("rotate_right")) {
					if (status == false) {
						plr.rotateAngle = 0;
					} else plr.rotateAngle = -param;
				} 
				if (msg.equals("jump")) {
					plr.jump = 1;
					if (status == true) cmd = setAnim(plr.id, "robot_rig|Jump", 1, true); 
					else cmd = setAnim(plr.id, "robot_rig|Stay", -1, false); 
				} 
				if (msg.equals("fire")) {
					plr.fire = status;
				}
				// отправляем команду на выполнение контроллеру сущностей
				// он уже сам потом разошлёт её клиентам
				if (cmd != null) world.entityController.entityCommandManager.addEventToQueue(cmd); 
			}
			return null;
		}
		
		UpdateEntity setAnim(int id, String name, int animLoop, boolean animSetType){
			UpdateEntity cmd = new UpdateEntity();
			cmd.entityUpdateType = EntityUpdateType.setAnim;
			cmd.id = id;
			cmd.animateName = name;
			cmd.animLoop = animLoop;
			cmd.animSetType = animSetType;
			return cmd;
		}
		
		/**Поиск сущности привязанной к конкретному сетевому клиенту*/
		int findPlayerIdFromWorld(GWorld world, int netClientID) {
			synchronized (world.entityController.items) {
				for (Entry<Entity> tree : new IntMap.Entries<Entity>(world.entityController.items)) {
					if (tree.value != null) {
						if (tree.value.playerID == netClientID)
							return tree.value.id;
					}
				}
			}
			return -1;
		}
	}

	/**Универсальное сообщение для пересылки любых данных*/
	public static class UniversalMessage extends ClientMessage{
		Object msg;

		public UniversalMessage(Object msg) {
			super();
			this.msg = msg;
		}

		public UniversalMessage() {
			super();
			// TODO Автоматически созданная заглушка конструктора
		}

		public Object getMsg() {
			return msg;
		}

		public void setMsg(Object msg) {
			this.msg = msg;
		}

		@Override
		public ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
			// TODO Автоматически созданная заглушка метода
			return null;
		}
	}
	
	/**Сигнал пользователя о дисконекте
	 * генерируется клиентом для отключения от сервера
	 * или самим сервером при дисконекте клиента*/
	public static class DisconectPlayer extends ClientMessage{
		
		public DisconectPlayer() {
			super();
		}

		@Override
		public ServerMessage ResponseClientMessage(ServerGameController controller, int netClientID) {
			Gdx.app.log("LGame"," Disconect " + netClientID+ " player | from " + controller.playerID_WorldID.get(netClientID) + " world");
			GameWorld3D world = (GameWorld3D) controller.get(controller.playerID_WorldID.get(netClientID));
			if (MySyper2DGame.gameState == GameState.Dispose) return null;
			// ищем номер сущности игрока
			int entid =world.entityController.getEntityIDformPlayerID(netClientID);
			if (entid != -1) {
				// если нашли то ебошим его (entityCommandManager) сам отошлёт обновления клиентам
				EntityCommand.UpdateEntity update = new UpdateEntity();
				update.entityUpdateType = EntityUpdateType.Delete;
				update.id = entid;
				world.entityController.entityCommandManager.addEventToQueue(update);
			}
			return null;
		}
	}
}

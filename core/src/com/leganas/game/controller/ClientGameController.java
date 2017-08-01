package com.leganas.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.Assets;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.Setting;
import com.leganas.game.MySyper2DGame.GameState;
import com.leganas.game.controller.ServerGameController.ServerGameState;
import com.leganas.game.controller.cmdEngine.ClientGameCommandManager;
import com.leganas.game.controller.cmdEngine.command.ClientGameCommand;
import com.leganas.game.framework.interfaces.Controller;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.network.client.AbstractClient;
import com.leganas.game.network.client.AbstractClient.NetClientListener;
import com.leganas.game.network.client.NetClient;
import com.leganas.game.network.packeges.GeneralMessages.ChatMessage;
import com.leganas.game.network.packeges.GeneralMessages.UpdateNames;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage.RequestAllWorldId;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage.RequestServerInfo;
import com.leganas.game.network.packeges.serverTOclient.ServerMessage;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.GWorld.WorldListener;
import com.leganas.game.world.render.i3D.ClientWorldRender3D;

public class ClientGameController extends Controller<GWorld> implements WorldListener, NetClientListener, Disposable{
	public class Update implements Runnable {
		public long delta,deltaP = 0;
		public long lastTime = System.nanoTime();
		@Override
		public void run() {
			while(MySyper2DGame.gameState != GameState.Dispose){
//				waitRenderFinish();
				// В этот поток нужно пихать все операции над миром
				long correntTime = System.nanoTime();
				delta = correntTime-lastTime; 
				deltaP += delta;
				float deltaPMili = (float)( (float)deltaP / 1000000000f);
				// Запускаем процесс обновления мира с частотой 60 кадров в секунду
				if (deltaPMili >= 0.016f) {
//					Gdx.app.log("LGame", " Client tick update | " + deltaPMili);
					if (clientState != ClientState.readry) { 
						if (clientState == ClientState.requestJoinToWorld) {
							// Это нужно перенести в Stage MainMenu экрана
							int num = 0;// !!!!!!! Это нужно выбрать в меню , к какому миру  подключатся
							if (worldID.length == 0) return;
							if (Setting.isServer() && Setting.isClient()){ 
								synchronized (worldID) {
									items = MySyper2DGame.serverGameController.get(worldID[num]);
									render.setWorld(items);
									items.setWorldRender(render);
									client.sendMessage(new ClientMessage.RequestJoinToWorld(num));
									Gdx.app.log("LGame", "netClientMsg : serverWorld = clientWorld №" + items.id);
									Assets.worldrender = items.id; // временно
								}
							} else {
								Gdx.app.log("LGame","netClientMsg : NEW ClientWorld");
								clientCommandManager.addEventToQueue(new ClientGameCommand.CreateWorld(worldID[num],worldName[num]));
							}
							clientState = ClientState.wait;
						}

						if (clientState == ClientState.waitUserSelectWorld) {
							// тут выводим меню выбора игрового мира
							// пока временно ждем когда сервак пришлет нам список доступных миров
							do {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} while (worldID.length < 0);
							clientState = ClientState.requestJoinToWorld;
						}
						
						if (clientState == ClientState.requestAcceptNewPlayerWorld) {
							
						}
						if (clientState == ClientState.requestWorldState) {
						}
					}
					// на стороне клиента временно мир не обновляем (т.к. ебаово получается с локальным клиентом)
//					if (items != null && !Setting.isServer()) items.update(deltaPMili); 
					deltaP = 0;
				}
				// Обрабатываем очередь команд от сервера
				if (client != null) clientCommandManager.process();
				// обрабатываем на прямую контроллер команд для Entity
				if (items != null) items.entityController.entityCommandManager.process();
				lastTime = correntTime;
				// Делаем sleep если deltaMili < 0.010, Это нужно чтобы не нагружать излишне систему этим потоком 
				// Соответственно этот метод будем выполнятся с интервалом в 1 миллисекунду 
				// (можно чаще сделать но нагрузка увеличится на CPU) Thread.sleep(0,500);
				if (deltaPMili < 0.016f){
				try {
//					Gdx.app.log("LGame"," client update thead sleep | deltaPMili = " + 16 - (long) (deltaPMili * 1000));
					Thread.sleep(16 - (long) (deltaPMili * 1000));
				} catch (InterruptedException e) {
				}
				} else {
//					Gdx.app.log("LGame"," client update | deltaPMili = " + deltaPMili);
				}
			}
			Gdx.app.log("LGame", "Client update Thread stop");
		}
		
	}

	public enum ClientState{
		create,
		init,
		requestAllWorldId,
		wait,
		waitUserSelectWorld,
		requestJoinToWorld,
		requestAcceptNewPlayerWorld,
		requestWorldState,
		readry
	}
	/**ID сущности к которой привязан наш клиент (это наш персонаж)*/
//    public int clientEntityID = -1;
	/**Менеджер заданий для клиента*/
    public ClientGameCommandManager clientCommandManager;
	/**Сетевой клиент*/
	public AbstractClient client;
	/**Состояние ClientGameController*/
	public ClientState clientState = ClientState.create; 
	/**Список подключенных к серверу клиентов*/
	String[] clientList; 
	
	// Доступные для подключения миры на стороне сервера
	public int[] worldID;
	public String[] worldName; // их имена
	// флаг начала конца процесса рендеринга;
	boolean renderFlag=false;
	// отдельный поток который вызывает метод Update и формирует обновления для клиентов
	Thread threadUpdate;
	// ClientWorldRender3D
	public ClientWorldRender3D render;
	
	public ClientGameController() {
		super();
		client = new NetClient(this,Setting.getClientHost());
		client.setNetClientListener(this);
		clientCommandManager = new ClientGameCommandManager(this,-1);
		clientState = ClientState.init;
		
		render = new ClientWorldRender3D(items);

		threadUpdate = new Thread(new Update());
		threadUpdate.setName("Client Update");
		threadUpdate.start();

	}
	
	
	@Override
	public void init() {
		if (clientState != ClientState.init) return;
		Gdx.app.log("LGame","netClientMsg : RequestServerInfo " + " and RequestAllWorldId");
		client.sendMessage(new RequestServerInfo());
		client.sendMessage(new RequestAllWorldId());
		clientState = ClientState.wait;
	}


	@Override
	public void update(float delta) {
	}
	
	void waitRenderFinish(){
		while (renderFlag != false){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
//			Gdx.app.log("LGame", " WaitRenderFinish ");
		}
	}

	
	@Override
	public void render(float delta) {
		// С ожиданием этим это спорный момент нужно тестировать
		renderFlag = true;
		if (items != null){
		synchronized (items) {
				items.render(delta);
			}
		}
		renderFlag = false;
	}


	@Override
	public void netClientMessage(final Object object) {
		// если получены обновления от сервера  то применяем их к миру
		// всё в отдельном потоке чтобы не задерживать поток netClient
		 final ClientGameController controller = this;
		 if (object instanceof ServerMessage) {
			 new Thread("Update") {
				 public void run () {
					 if (MySyper2DGame.gameState != GameState.Dispose){
						 Event mesage = ((ServerMessage)object).ResponseServerMessage(controller);
					 	if (mesage != null) client.sendtoTCP(mesage);
					 }
				 }
			 }.start();
		 }
 
		if (object instanceof UpdateNames) {
			UpdateNames updateNames = (UpdateNames)object;
			clientList = updateNames.names;
			Gdx.app.log("LGame","netClientMsg : " + updateNames.names[0] + " connected");
			return;
		}

		if (object instanceof ChatMessage) {
			ChatMessage chatMessage = (ChatMessage)object;
			Gdx.app.log("LGame","netClient ChatMessage : " + chatMessage.text);
			return;
		}
	}

	public ClientState getClientState() {
		return clientState;
	}


	public void setClientState(ClientState clientState) {
		this.clientState = clientState;
	}


	public String[] getClientList() {
		return clientList;
	}


	public void setClientList(String[] clientList) {
		this.clientList = clientList;
	}


	public int[] getWorldID() {
		return worldID;
	}


	public void setWorldID(int[] worldID) {
		this.worldID = worldID;
	}


	public String[] getWorldName() {
		return worldName;
	}


	public void setWorldName(String[] worldName) {
		this.worldName = worldName;
	}

	@Override
	public void dispose() {
		if (client != null) client.dispose();
		if (items != null) items.dispose();
	}


	@Override
	public void worldMessage(int worldid, Object msg) {
		// TODO Автоматически созданная заглушка метода
		
	}

}
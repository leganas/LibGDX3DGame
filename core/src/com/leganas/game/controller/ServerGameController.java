package com.leganas.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.MySyper2DGame.GameState;
import com.leganas.game.Setting;
import com.leganas.game.controller.cmdEngine.ServerGameCommandManager;
import com.leganas.game.controller.cmdEngine.command.ServerGameCommand;
import com.leganas.game.framework.graphics.engine3D.Contacter.PhisicsContactListener;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.graphics.engine3D.Physics;
import com.leganas.game.framework.interfaces.Controller;
import com.leganas.game.network.client.ClientInfo;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.network.packeges.clientTOserver.ClientUpdate;
import com.leganas.game.network.packeges.serverTOclient.ServerMessage;
import com.leganas.game.network.packeges.serverTOclient.ServerUpdate;
import com.leganas.game.network.server.AbstractServer;
import com.leganas.game.network.server.AbstractServer.NetServerListener;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.GWorld.WorldListener;
import com.leganas.game.world.GameWorld3D;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.EntityUpdateType;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;
import com.leganas.game.world.entity.gameObject.i3D.Player3D;
import com.leganas.game.network.server.NetServer;

public class ServerGameController extends Controller<IntMap<GWorld>> implements WorldListener,NetServerListener, PhisicsContactListener, Disposable{
	public class Update implements Runnable {
			public long delta,deltaP = 0;
			public long lastTime = System.nanoTime();
			@Override
			public void run() {
				while(MySyper2DGame.gameState != GameState.Dispose){
					long correntTime = System.nanoTime();
					delta = correntTime-lastTime; 
					deltaP += delta;
					float deltaPMili= ((float)deltaP / 1000000000f);
					
//					deltaPMili = Math.min(1f/10f, Gdx.graphics.getDeltaTime());
					// ��������� ������� ���������� ���� � �������� 60 ������ � �������
					if (deltaPMili >= 0.016f) {
//						Gdx.app.log("LGame", " tick update | " + deltaPMili);
						deltaP = 0;
//						Gdx.app.log("LGame", " deltaP | " + deltaP);
						if (items != null) {
							synchronized (items) {
								for (final Entry<GWorld> tree : new IntMap.Entries<GWorld>(items))
								{
//								 Gdx.app.log("LGame","World | "+tree.key + " update | delta time" + deltaPMili);
								 if (Setting.isMobile()) {
									tree.value.update(Math.min(1f/10f, Gdx.graphics.getDeltaTime()));
//									tree.value.update(deltaPMili);
								 } else {
									tree.value.update(Math.min(1f/10f, Gdx.graphics.getDeltaTime()));
//									tree.value.update(deltaPMili);
								 }
								 
								 ServerUpdate update;
								 update = createServerUpdate(tree.value, EntityUpdateType.Update, true);
					//			 Gdx.app.log("LGame","world � " + tree.value.id + " | update count = " + update.getUpdateSize());
								 if (update.getUpdateSize() > 0) sendUpdateToAllPlayerWorld(update, tree.key, true);
								}
							}
						}
					}
					lastTime = correntTime;
					// ������ sleep ���� deltaPMili < 0.05f, ��� ����� ����� �� ��������� ������� ������� ���� ������� 
					// �������������� ���� ����� ����� ���������� � ���������� � 1 ������������ 
					// (����� ���� ������� �� �������� ���������� �� CPU) Thread.sleep(0,500);
					if (deltaPMili < 0.016f) { 
						try {
//						Gdx.app.log("LGame"," server update thread sleep | deltaPMili = " + (long) (deltaPMili * 1000));
						
						Thread.sleep(16 - (long) (deltaPMili * 1000));
					} catch (InterruptedException e) {}
					}  else {
//						Gdx.app.log("LGame"," server update | deltaPMili = " + deltaPMili);
					}
				}
				Gdx.app.log("LGame", "Server update Thread stop");
			}
			
		}
	public enum ServerGameState{
		init,
		ready
	}
	public static ServerGameState serverGameState = ServerGameState.init;
	public AbstractServer server;
	public ServerGameCommandManager serverGameCommandManager; 
	public IntMap<Integer> playerID_WorldID;
	// ����� ��� ������� ��������� (-1 �� �����)
	public int renderWorld = 0;
	// ��������� ����� ������� �������� ����� Update � ��������� ���������� ��� ��������
	Thread threadUpdate;
	// ���� ������ ����� �������� ����������;
	boolean renderFlag=false;
	
	public ServerGameController() {
		super();
		server = new NetServer(this);
		server.setGameServerListener(this);
		items = new IntMap<GWorld>();
		playerID_WorldID = new IntMap<Integer>();
		serverGameCommandManager = new ServerGameCommandManager(this);
		Physics.init();
		Physics.contacter.setContactListener(this);

		threadUpdate = new Thread(new Update());
		threadUpdate.setName("Server Update");
		threadUpdate.start();
	}

	@Override
	public void init() {
		serverGameCommandManager.addEventToQueue(new ServerGameCommand.CreateNewWorld("TestWorld1"));
//		serverGameCommandManager.addEventToQueue(new ServerGameCommand.CreateNewWorld("TestWorld2"));
//		serverGameCommandManager.addEventToQueue(new ServerGameCommand.CreateNewWorld("TestWorld3"));
	}

	@Override
	public void update(float delta) {
		serverGameCommandManager.process();
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
		renderFlag = true;
		if (items != null && renderWorld != -1) { 
			synchronized (items) {
				if (items.get(renderWorld) != null) items.get(renderWorld).render(delta);
			}
		}
		
//		Gdx.app.log("LGame", " RenderFinish ");
		renderFlag = false;
	}
	
	public int getAcceptID() {
		int id=-1;
		synchronized (items) {
			do {
				id++;
			} while ((id > items.size) || items.containsKey(id));
		}
		return id;
	}
	
	public synchronized void put(int id,GWorld item){
		items.put(id, item);
	}
	
	public synchronized void add(GWorld item){
		put(getAcceptID(),item);
	}
	
	public synchronized GWorld get(int key){
		return items.get(key);
	}
	
	public synchronized int getID(GWorld item){
		return items.findKey(item, true, -1);
	}

	@Override
	public void worldMessage(final int worldid, final Object msg) {
		serverGameState = ServerGameState.ready; // ��������� ����� ��� ������ ������ ������ � ����� �������
		// ��� �� ��������� ��������� �� ������� ����� � ������������ �� � ��������� ������
//		Gdx.app.log("LGame"," server send worldMessage to all player | " + msg); 
		new Thread("ClientMessage") {
			   public void run () 
			   {
				 if (msg instanceof EntityCommand.UpdateEntity){
					ServerUpdate update = new ServerUpdate();
					update.addUpdate((UpdateEntity)msg);
					sendUpdateToAllPlayerWorld(update,worldid, false);
				 }
			   }
		 }.start();
	}

	@Override
	public void netServerMessage(final int netClientID,final Object msg) {
		 // ���� �������� ���������� �� �������  �� ��������� �� � ����
		 if (msg instanceof ClientUpdate) {
			 ClientUpdate mesage = (ClientUpdate) msg;
			 synchronized (items) {
				mesage.applyUpdate(items.get(mesage.worldID));
			}
		 }

		 final ServerGameController controler = this;
		 // ��������� ��� ��� ������� ��� netServer 
		 // (���� ��� ��������� �� �������� �� ��������� � ���� ���� ����� �� ��������)
		 // �� ����������� � ��������� ������ (��� ���� ��� �� �� ����������� ����� netServer)
		 if (msg instanceof ClientMessage) {
		  new Thread("ClientMessage") {
			   public void run () 
			   {
				ServerMessage mesage = ((ClientMessage) msg).ResponseClientMessage(controler, netClientID);
				if (mesage != null) {
					server.sendtoTCP(netClientID, mesage);
				}
			   }
		  }.start();
		 }
	}
	
	/**�������� ����� ���������� ���� �������� �������� ���� World*/
	public void sendUpdateToAllPlayerWorld(ServerUpdate update,int worldId, boolean UDPflag){
		/**���������� ServerUpdate � ������������� ��������� ��������� � ��������*/
		if (update.getUpdateSize() == 0) {
//			Gdx.app.log("LGame","Server ServerUpdate size null canseled send");
			return;
		}
		ServerMessage.UniversalMessage msg = new ServerMessage.UniversalMessage();
		msg.msg = update; 
		sendtoAllPlayerWorld(msg,worldId, UDPflag);
	}
	
	public void sendtoAllPlayerWorld(Object msg, int worldId, boolean UDPflag){
		synchronized (playerID_WorldID) {
			for (Entry<Integer> tree : playerID_WorldID.entries()) {
				if (tree.value == worldId)
					if (tree.key != ClientInfo.UserID) {
						if (UDPflag) server.sendtoUDP(tree.key, msg); else server.sendtoTCP(tree.key, msg);
//						Gdx.app.log("LGame"," send to client � " + tree.key + " | " + msg);
					}
			}
		}
	}
	
	public ServerUpdate createServerUpdate(GWorld world, EntityUpdateType typeUpdate, boolean transformCheck){
		ServerUpdate update;
		IntMap<Entity> entity = world.entityController.getItems();
		synchronized (entity) {
				update = new ServerUpdate();
				for (Entry<Entity> tree : new IntMap.Entries<Entity>(entity)) {
					if (tree.value != null){
					boolean key = true;
					// 1) ��������� ����� �� ���� �������� ������� ������������� (������ ���� �� ����� ����� �������� ��������)
					// 2) ��������� ������������������ �� ������
					if (transformCheck && typeUpdate != EntityUpdateType.Create)
							if (!tree.value.model.isTransformFlag()) key = false;

					// ���� key == true , ������ ��� ����� ������ ���������� ��� ��������
					if (key) {
						UpdateEntity entUpdate = new UpdateEntity();
						entUpdate.setEntityUpdateType(typeUpdate);
						entUpdate.id = tree.value.id;
						entUpdate.name = tree.value.name;
						entUpdate.playerID = tree.value.playerID;
						entUpdate.type = tree.value.type;
						if (tree.value.model instanceof Model3DPhysics) {
							Model3DPhysics model = (Model3DPhysics) tree.value.model;
							entUpdate.transform = model.instance.transform;
							if (tree.value instanceof Player3D) {
								entUpdate.direction = ((Player3D)tree.value).direction;
							}
//							Gdx.app.log("LGame", " Model3D "+tree.value.name+" transform present");
						}
						update.addUpdate(entUpdate);
						tree.value.model.setTransformFlag(false);
					}
					}
				}
					
			}
		return update;
	}

	@Override
	public void dispose() {
		MySyper2DGame.gameState = GameState.Dispose;
		if (server != null) server.dispose();
			if (items != null){
				for (Entry<GWorld> tree : new IntMap.Entries<GWorld>(items)) tree.value.dispose();
				items.clear();
			}
	}
	
	// ������� ��������� � �������� �������� ������� �������� ����
	@Override
	public boolean onContactAdded(int worldID0, int userValue0, int partId0, int index0, boolean match0, int worldID1,
			int userValue1, int partId1, int index1, boolean match1) {
		if (worldID0 == worldID1) {
			((GameWorld3D)items.get(worldID0)).onContactAdded(userValue0, partId0, index0, match0, userValue1, partId1, index1, match1);
		}
		return false;
	}

	@Override
	public void onContactProcessed(int worldID0, int userValue0, int worldID1, int userValue1) {
		if (worldID0 == worldID1) ((GameWorld3D)items.get(worldID0)).onContactProcessed(userValue0, userValue1);
	}

}

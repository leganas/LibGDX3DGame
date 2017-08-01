package com.leganas.game.network.server;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.framework.interfaces.Net;



public abstract class AbstractServer  extends Net<ServerGameController> implements Disposable{
	
	public AbstractServer(ServerGameController gameController) {
		super(gameController);
	}

	public interface NetServerListener {
		/**ћетод вызываетс€ сервером дл€ передачи событий полученных от клиентов или своих собственных*/
		public void netServerMessage(int connectionID,Object event);
	}
	NetServerListener netServerListener;

	public void setGameServerListener(NetServerListener gameServerListener) {
		this.netServerListener = gameServerListener;
	}

	public int getCollClient(){
		return 0;
	}

	public abstract void sendToAllUDP(Object message);
	
	public abstract void sendToAllTCP(Object message); 
	
	public abstract void sendtoTCP(int connectionID, Object message);
	
	public abstract void sendtoUDP(int connectionID, Object message);

	@Override
	public void dispose() {
	}
}

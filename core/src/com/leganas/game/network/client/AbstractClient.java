package com.leganas.game.network.client;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.framework.interfaces.Net;


public abstract class AbstractClient extends Net<ClientGameController> implements Disposable{
	String name;

	
	public AbstractClient(ClientGameController clientGameController) {
		super(clientGameController);
	}
	
	/**Реализуется контроллером игры типа ClientGameController для получения сообщений от сетевого клиента*/
	public interface NetClientListener {
		/**Вызывает сетевой клиент*/
		public void netClientMessage(Object event);
	}
	NetClientListener netClientListener;
	
	
	public void setNetClientListener(NetClientListener netClientListener) {
		this.netClientListener = netClientListener;
	}

	public void sendMSGtoListener(Object event){
		netClientListener.netClientMessage(event);
	}
	
	public abstract void sendtoTCP(Object message);
	
	public  void sendMessage(Object message){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public void dispose() {
	}
}

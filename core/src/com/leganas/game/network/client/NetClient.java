
package com.leganas.game.network.client;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.Setting;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.network.Network;
import com.leganas.game.network.packeges.GeneralMessages.RegisterName;


public class NetClient extends AbstractClient{
	Client client;
	
	
	
	public NetClient(ClientGameController clientGameController) {
		super(clientGameController);
	}

	public NetClient (final ClientGameController clientGameController, String myhost) {
		super(clientGameController);
		client = new Client(256000,256000);
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
				ClientInfo.UserID = client.getID();
			}

			public void received (Connection connection, Object object) {
				netClientListener.netClientMessage(object);
			}

			public void disconnected (Connection connection) {
				Gdx.app.log("LGame", "netClient server down");
			}
		});

		String input = myhost;
		final String host = input.trim();
		
		input = Setting.getClientName();
		name = input.trim();

		// Запускаем клиента в отдельном потоке
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(50000, host, Network.portTCP, Network.portUDP);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
//					ex.printStackTrace();
					Gdx.app.log("LGame", " ERROR Client failed connection to : " + host);
					MySyper2DGame.inst.assets.dispose();
					if (MySyper2DGame.serverGameController != null) MySyper2DGame.serverGameController.dispose();
					System.exit(0);
				}
			}
		}.start();
	}
	
	
	
	@Override
	public void sendtoTCP(Object message) {
		// TODO Автоматически созданная заглушка метода
		sendMessage(message);
	}

	public  void sendMessage(Object message){
	try {
		client.sendTCP(message);
	} catch (Exception e) {
	}
	}

	@Override
	public void dispose() {
		client.stop();
		Gdx.app.log("LGame", "netClient disconect from the server and dispose");
	}
}


package com.leganas.game.network.server;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.network.Network;
import com.leganas.game.network.packeges.GeneralMessages.ChatMessage;
import com.leganas.game.network.packeges.GeneralMessages.RegisterName;
import com.leganas.game.network.packeges.GeneralMessages.UpdateNames;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage.DisconectPlayer;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;

public class NetServer extends AbstractServer{
	Server server;
	
	public NetServer (ServerGameController serverGameController) {
		super(serverGameController);
		server = new Server(256000,256000) {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new GameConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);
		
		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				GameConnection connection = (GameConnection)c;

				if (object instanceof RegisterName) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (connection.name != null) return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					// Send a "connected" message to everyone except the new client.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = "Server : chatMessage - "+name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					// Send everyone a new list of connection names.
					updateNames();
					return;
				}

				if (object instanceof ChatMessage) {
					// Ignore the object if a client tries to chat before registering a name.
					if (connection.name == null) return;
					ChatMessage chatMessage = (ChatMessage)object;
					// Ignore the object if the chat message is invalid.
					String message = chatMessage.text;
					if (message == null) return;
					message = message.trim();
					if (message.length() == 0) return;
					// Prepend the connection's name and send to everyone.
					chatMessage.text = connection.name + ": " + message;
					netServerListener.netServerMessage(connection.getID(),message);
					server.sendToAllTCP(chatMessage);
					return;
				}
				// В противном случае всё отсылаем нашему ServerGameController
//				if (object instanceof ClientMessage) Gdx.app.log("LGame", "Server получил сообщение от клиента");
				netServerListener.netServerMessage(connection.getID(),object);

			}

			public void disconnected (Connection c) {
				GameConnection connection = (GameConnection)c;
				if (connection.name != null) {
					// Announce to everyone that someone (with a registered name) has left.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
					ClientMessage.DisconectPlayer msg = new DisconectPlayer();
					netServerListener.netServerMessage(c.getID(), msg);
				}
			}
		});
		try {
			server.bind(Network.portTCP, Network.portUDP);
			server.start();
		} catch (Exception e) {
			Gdx.app.log("LGame", " ERROR Server already running");
			MySyper2DGame.inst.dispose();
			System.exit(0);
		}
		
//		Log.set(Log.LEVEL_DEBUG);
	}
	
	public int getCollClient(){
		return server.getConnections().length;
	}
	
	@Override
	public void dispose(){
		server.stop();
		Gdx.app.log("LGame", "Server stop");
    }
	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList<String> names = new ArrayList<String>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			GameConnection connection = (GameConnection)connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}
	
	public void sendToAllUDP(Object message){
		server.sendToAllUDP(message);
	}
	
	public void sendToAllTCP(Object message){
		server.sendToAllTCP(message);
	} 
	
	public void sendtoTCP(int connectionID, Object message){
		server.sendToTCP(connectionID, message);
	} 
	
	public void sendtoUDP(int connectionID, Object message){
		server.sendToUDP(connectionID, message);
	}

	/**Для примера создал так дальше будем всё делать*/
/*	public synchronized void sendMessage(Object object){
		if (object instanceof ServerMessage) {
			((ServerMessage)object).massage = "testmesage";
			server.sendToAllTCP(object);
		}
	}*/

	// This holds per connection state.
	static class GameConnection extends Connection {
		public String name;
	}
}

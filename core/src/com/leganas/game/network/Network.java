
package com.leganas.game.network;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.leganas.game.controller.cmdEngine.command.ServerGameCommand;
import com.leganas.game.network.packeges.GeneralMessages.ChatMessage;
import com.leganas.game.network.packeges.GeneralMessages.RegisterName;
import com.leganas.game.network.packeges.GeneralMessages.UpdateNames;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.network.packeges.clientTOserver.ClientUpdate;
import com.leganas.game.network.packeges.serverTOclient.ServerMessage;
import com.leganas.game.network.packeges.serverTOclient.ServerUpdate;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int portTCP = 8900;
	static public final int portUDP = 8902;

	
	
	public static int getPorttcp() {
		return portTCP;
	}

	public static int getPortudp() {
		return portUDP;
	}

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(float[].class);
		kryo.register(int[].class);
		kryo.register(Object.class);
		kryo.register(String[].class);
		kryo.register(Object[].class);
		kryo.register(Vector2.class);
		kryo.register(Vector3.class);
		kryo.register(Matrix4.class);
		kryo.register(Vector2.class);
		kryo.register(Array.class);
		kryo.register(IntMap.class);
		
		kryo.register(RegisterName.class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		
		kryo.register(ClientMessage.class);
		kryo.register(ClientMessage.UniversalMessage.class);
		kryo.register(ClientMessage.RequestServerInfo.class);
		kryo.register(ClientMessage.RequestAllWorldId.class);
		kryo.register(ClientMessage.RequestJoinToWorld.class);
		kryo.register(ClientMessage.RequestAcceptNewPlayerWorld.class);
		kryo.register(ClientMessage.RequestWorldState.class);
		kryo.register(ClientMessage.StringMessage.class);
		kryo.register(ClientMessage.ClientInputMsg.class);
		kryo.register(ClientMessage.DisconectPlayer.class);
		kryo.register(ClientUpdate.class);

		kryo.register(ServerMessage.class);
		kryo.register(ServerMessage.UniversalMessage.class);
		kryo.register(ServerMessage.ReturnServerInfo.class);
		kryo.register(ServerMessage.ReturnAllWorldId.class);
		kryo.register(ServerMessage.ReturnAcceptJoinToWorld.class);
		kryo.register(ServerMessage.ReturnAcceptNewPlayer.class);
		kryo.register(ServerMessage.ReturnWorldState.class);
		kryo.register(ServerUpdate.class);
		kryo.register(Entity.EntityType.class);
		kryo.register(UpdateEntity.class);
		kryo.register(UpdateEntity.EntityUpdateType.class);
		
	}

}

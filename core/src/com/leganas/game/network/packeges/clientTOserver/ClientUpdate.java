package com.leganas.game.network.packeges.clientTOserver;

import com.badlogic.gdx.utils.Array;
import com.leganas.game.world.GWorld;

/**Пакет обновлений для сервера посылаемый через сеть клиентом*/
public class ClientUpdate {
	public int worldID = -1;
	Array <ClientMessage> clientMessage;
	public ClientUpdate() {
		super();
		clientMessage = new Array<ClientMessage>();
	}
	
	public void addUpdate(ClientMessage update){
		synchronized (clientMessage) {
			clientMessage.add(update);
		}
	}
	
	
	public int getWorldID() {
		return worldID;
	}

	public void setWorldID(int worldID) {
		this.worldID = worldID;
	}

	public void applyUpdate(GWorld world) {
/*		for (ClientInput update : clientInput){
	      	 if (update.getKey().equals("forward/down")) {
	      		 world.entityManager.player.get(update.getPlayerID()).forward = true;
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Walk";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Walk", -1);
	      		 }
	      	 if (update.getKey().equals("back/down")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Back";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Back", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).back = true;
	      		 }
	      	 if (update.getKey().equals("right/down")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Right";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Right", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).right = true;
	      	 }
	      	 if (update.getKey().equals("left/down")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Left";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Left", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).left = true;
	      	 }
	      	 if (update.getKey().equals("rotate_left/down")) {world.entityManager.player.get(update.getPlayerID()).rotateAngle = 1;}
	      	 if (update.getKey().equals("rotate_right/down")) {world.entityManager.player.get(update.getPlayerID()).rotateAngle = -1;}
	      	 if (update.getKey().equals("jump/down")) {
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Jump", 1);
	      		 if (world.entityManager.player.get(update.getPlayerID()).jump <= 0) world.entityManager.player.get(update.getPlayerID()).jump = 1;
	         }
	      	 if (update.getKey().equals("fire/down")) {world.entityManager.player.get(update.getPlayerID()).fire = true;}

	      	 if (update.getKey().equals("forward/up")) {
	      		 world.entityManager.player.get(update.getPlayerID()).forward = false;
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Stay";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Stay", -1);
	      		}
	      	 if (update.getKey().equals("back/up")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Stay";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Stay", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).back = false;
	         }
	      	 if (update.getKey().equals("right/up")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Stay";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Stay", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).right = false;
	      	 }
	      	 if (update.getKey().equals("left/up")) {
	      		 world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Stay";
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Stay", -1);
	      		 world.entityManager.player.get(update.getPlayerID()).left = false;
	      	 }
	      	 if (update.getKey().equals("rotate_left/up")) {world.entityManager.player.get(update.getPlayerID()).rotateAngle = 0;}
	      	 if (update.getKey().equals("rotate_right/up")) {world.entityManager.player.get(update.getPlayerID()).rotateAngle = 0;}
	      	 if (update.getKey().equals("jump/up")) {
	      		 world.entityManager.player.get(update.getPlayerID()).setAnimateToQueue(world.entityManager.player.get(update.getPlayerID()).animateName, -1);
	      	 }
	      	 if (update.getKey().equals("fire/up")) {world.entityManager.player.get(update.getPlayerID()).fire = false;}
	      	 if (update.getKey().equals("all/up")) {
	      		 try {
					world.entityManager.player.get(update.getPlayerID()).forward = false;
					world.entityManager.player.get(update.getPlayerID()).back = false;
					world.entityManager.player.get(update.getPlayerID()).right = false;
					world.entityManager.player.get(update.getPlayerID()).left = false;
					world.entityManager.player.get(update.getPlayerID()).rotateAngle = 0;
					world.entityManager.player.get(update.getPlayerID()).animateName = "robot_rig|Stay";
					world.entityManager.player.get(update.getPlayerID()).setAnimate("robot_rig|Stay", -1);
				} catch (Exception e) {
				}
	      	 }
		}*/
	}
	
	public void clearUpdate(){
		synchronized (clientMessage) {
			clientMessage.clear();
		}
	}
}

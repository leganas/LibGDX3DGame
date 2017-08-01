package com.leganas.game.network.packeges.serverTOclient;

import com.badlogic.gdx.utils.Array;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.UpdateEntity;

/**Пакет обновлений для клиента посылаемый через сеть сервером*/
public class ServerUpdate {
	public Array<UpdateEntity> updateEntity;
	
	public ServerUpdate() {
		updateEntity = new Array<UpdateEntity>();
	}
	
	public void addUpdate(UpdateEntity update){
		synchronized (updateEntity) {
			updateEntity.add(update);
		}
	}
	
	public void applyUpdate(GWorld world){
		for (UpdateEntity update : updateEntity){
			world.entityController.entityCommandManager.addEventToQueue(update);
//			Gdx.app.log("LGame",""+update.entityUpdateType+" : "+ update.name+" | " + update.transform);
		}
	}
	
	public int getUpdateSize(){
		return updateEntity.size;
	}
	
	public void clearUpdate(){
		synchronized (updateEntity) {
			updateEntity.clear();
		}
	}

}

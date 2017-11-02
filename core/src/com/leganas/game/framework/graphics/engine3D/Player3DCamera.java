package com.leganas.game.framework.graphics.engine3D;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.leganas.game.world.GameWorld3D;


public class Player3DCamera {
	/**Перспективная камера*/
    public PerspectiveCamera cam;
    GameWorld3D world;
    
    float directionPlusX = 0,
    	  directionPlusY = 0,
    	  directionPlusZ = 0;
    
    public float zoom = 25;
    
    
    
	public float getDirectionPlusX() {
		return directionPlusX;
	}


	public void setDirectionPlusX(float directionPlusX) {
		this.directionPlusX = directionPlusX;
	}


	public float getDirectionPlusY() {
		return directionPlusY;
	}


	public void setDirectionPlusY(float directionPlusY) {
		this.directionPlusY = directionPlusY;
	}


	public float getDirectionPlusZ() {
		return directionPlusZ;
	}


	public void setDirectionPlusZ(float directionPlusZ) {
		this.directionPlusZ = directionPlusZ;
	}


	public Player3DCamera(GameWorld3D world) {
		super();
		this.world = world;
	}


	public void setCamToEntity(int id){
		Model3D model = (Model3D) world.entityController.items.get(id).model;
        Vector3 posP = new Vector3();
        if (id != -1) model.instance.transform.getTranslation(posP); else {
        	posP.x = 0;
        	posP.y = 13;
        	posP.z = -50;
        }
       
    	cam.position.set(posP.x+0f,posP.y+13f,posP.z -20f);
    	cam.lookAt(0,posP.y+13,0);
    	cam.near = 1f;
    	cam.far = 10000f;
    	cam.update();
	}
}

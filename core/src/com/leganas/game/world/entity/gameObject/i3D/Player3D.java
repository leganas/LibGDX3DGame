package com.leganas.game.world.entity.gameObject.i3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand;
import com.leganas.game.world.entity.cmdEngine.command.EntityCommand.EntityUpdateType;

public class Player3D extends Entity3D {
	public boolean forward = false;
	public boolean back = false;
	public boolean right = false;
	public boolean left = false;
    public boolean fire = false;
    public float jump = 0;
    public float rotateAngle = 0;
    public float direction = 0, oldDir = 0;
    public float speed = 10;

    public String animateName="robot_rig|Stay";
    public AnimationController animController;
    public boolean updateFlag = false;
	
	float deltaPUpdate = 0;
	
	

	public Player3D() {
		super();
	}
	
	public void waitUpdateAnim(){
		do {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		} while(updateFlag != false);
	}

	@Override
	public void update(float delta) {
		deltaPUpdate += delta;
		super.update(delta);
		Model3DPhysics mod = (Model3DPhysics) model;
    	if (direction != oldDir) model.setTransformFlag(true);
		oldDir = direction;

		if(rotateAngle != 0){
            direction = (direction + rotateAngle*360*delta)%360;
            mod.instance.transform.rotate(new Vector3(0f, 1f, 0f), rotateAngle*360*delta);
        }
        if(forward){
        	mod.instance.transform.trn((float)(speed * delta*Math.sin(Math.toRadians(direction))),
                0f, (float)(speed * delta*Math.cos(Math.toRadians(direction))));
        }
        if(back){
        	mod.instance.transform.trn(-(float)(speed * delta*Math.sin(Math.toRadians(direction))),
               0f, -(float)(speed * delta*Math.cos(Math.toRadians(direction))));
        }
        if(right){
        	mod.instance.transform.trn((float)(speed * delta*Math.sin(Math.toRadians(direction-90))),
                0f, (float)(speed * delta*Math.cos(Math.toRadians(direction-90))));
        }
        if(left){
        	mod.instance.transform.trn((float)(speed * delta*Math.sin(Math.toRadians(direction+90))),
        					   0f, (float)(speed * delta*Math.cos(Math.toRadians(direction+90))));
        }
        if(jump > 0){
        	jump = jump - delta;
        	mod.instance.transform.trn(0f, delta*10, 0f);
     		Vector3 posP = new Vector3(0,0,0);
     		mod.instance.transform.getTranslation(posP);
     		Gdx.app.log("player ", posP.y+"");
        }
        if(fire) {
//			Gdx.app.log("Player ", id+" fire");
        	if (deltaPUpdate >= 0.1f) {
        		deltaPUpdate = 0;
        		// получаем координаты модельки нашего игрока
        		Model3DPhysics model = (Model3DPhysics) this.model;
        		ModelInstanceAdv inst = (ModelInstanceAdv) model.instance;
        		Vector3 posP = new Vector3();
        		inst.transform.getTranslation(posP);
        		posP.y = posP.y + 5;
        		
        		// дальше тут уже формируем пульку
        		
        		// вычисляем импульс который мы предадим пульке
        		Vector3 impuls = new Vector3((float) (10 * 50 * Math.sin(Math.toRadians(direction))), 0f,
					(float) (10 * 50 * Math.cos(Math.toRadians(direction))));
        		
    			Matrix4 mtx = new Matrix4(); // новая матрица трансформации для пульки
    			mtx.setTranslation(posP);
    			EntityCommand.UpdateEntity update = new EntityCommand.UpdateEntity(-1, -1, EntityType.Bullet, EntityUpdateType.Create, "ball", mtx, impuls);
        		entityListener.EntityEvent(update);
        	}
        }

        try {
        	mod.instance.body.proceedToTransform(mod.instance.transform);
		} catch (Exception e) {
			
		}
	}

	public void setAnimateToQueue(String name, int loop){
		try {
			animController.queue(name, loop, 1, null, 1);
		} catch (Exception e) {
		}
	}

	public void setAnimate(String name, int loop){
		try {
			animController.setAnimation(name, loop);
		} catch (Exception e) {
		}
	}

	@Override
	public void modelMessage(int modelID, Object msg) {
		// TODO Автоматически созданная заглушка метода
		
	}
	
	
	
}

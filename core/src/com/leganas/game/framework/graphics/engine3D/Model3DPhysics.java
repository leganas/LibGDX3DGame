package com.leganas.game.framework.graphics.engine3D;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.leganas.game.framework.graphics.AbstractModel;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv.TransformListener;

public class Model3DPhysics extends AbstractModel<ModelInstanceAdv> implements TransformListener{
	/**Отключает использование света для отдельно взятой модели 
	 * на мобильных устройствах, дабы не тупило (пока так будем делать)*/
	public boolean environmentEconom = false;
	public Model3DPhysics() {
		super();
	}

	public Model3DPhysics(Model model,btRigidBody.btRigidBodyConstructionInfo bodyInfo) {
		super();
		instance = new ModelInstanceAdv(model,bodyInfo);
		instance.setTransformlistener(this);
		status = model3DStatus.ready;
	}
	
	protected static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
	
	protected Vector3 preview;
	/**Метод вызывается системой при преобразовании модели*/
	@Override
	public void transformEvent(Matrix4 transform) {
		Vector3 posMan = new Vector3();
        transform.getTranslation(posMan);
        if (posMan.x > 200 || posMan.x < -200 || 
        	posMan.y > 200 || posMan.y < -200 ||
        	posMan.z > 200 || posMan.z < -200) 
        {
         status = model3DStatus.outOfRange;	
        }
        if (preview != null) {
        	if (round(posMan.x,3) != round(preview.x,3) ||
        		round(posMan.y,3) != round(preview.y,3) ||
        		round(posMan.z,3) != round(preview.z,3)) 
        	{
        		transformFlag=true;
//        		Gdx.app.log("LGame ",this.instance.model.toString()+"Entity transform X " + round(posMan.x,3) + " Y " + round(posMan.y,3) + " Z "  + round(posMan.z,3));
        		} else 
        		{
//       		 Gdx.app.log("Entity stay","");
        		}
        } else {
//    		Gdx.app.log("LGame ","Entity transform"+" X " + round(posMan.x,3) + " Y " + round(posMan.y,3) + " Z "  + round(posMan.z,3));
        	transformFlag=true;
        }
    	preview = posMan;
	}
	
	public void setTransform(Matrix4 transform) {
		this.instance.transform.set(transform);
	}

	
}

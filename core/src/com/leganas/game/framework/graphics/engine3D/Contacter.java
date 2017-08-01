package com.leganas.game.framework.graphics.engine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.leganas.game.world.entity.EntityController;

/**Объект расширяющий ContactListener , в нем реализуется реакция на столкновения
 * UserValueID общий для всех игровых миров посему приходится извращатся*/
public class Contacter extends ContactListener{
	public interface PhisicsContactListener {
		/**Метод вызывается встроенным обработчиком столкновений*/
        public boolean onContactAdded (int worldID0,int userValue0, int partId0, int index0, boolean match0,
				   					   int worldID1,int userValue1, int partId1, int index1, boolean match1);        
        public void onContactProcessed(int worldID0,int userValue0, int worldID1,int userValue1);
    }
	private static PhisicsContactListener listener;
	public void setContactListener(PhisicsContactListener listener) {
			Contacter.listener = listener;
	}
	
	int getWorldID(int entityID){
		return EntityController.entityID_worldID.get(entityID);
	}
	
	/**Вызывается при возникновении столкновения между фигурами
     * имеет несколько вариаций для @Override*/
	@Override
    public boolean onContactAdded (int userValue0, int partId0, int index0, boolean match0,
            					   int userValue1, int partId1, int index1, boolean match1){
		// посылаем полученное столкновение слушающему нас контроллеру миров
		if (listener != null) listener.onContactAdded(getWorldID(userValue0),userValue0, partId0, index0, match0, 
													  getWorldID(userValue0),userValue1, partId1, index1, match1);
		return true;
    }
    @Override
    public void onContactProcessed(int userValue0, int userValue1){
    	if (listener != null) listener.onContactProcessed(getWorldID(userValue0),userValue0, 
    													  getWorldID(userValue1),userValue1);
    }
}

package com.leganas.game.world.entity;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.framework.graphics.AbstractModel;
import com.leganas.game.framework.graphics.AbstractModel.ModelListener;

/**Класс описывающий игровую сущность*/
public abstract class Entity implements Disposable, ModelListener{
	
	public enum EntityType {
		Player,
		Bullet,
		Primitive,
		Ground
	}
	
	public enum EntityStatus{
		init,
		ready,
		destroy
	}
	
	public EntityStatus status = EntityStatus.init;
	
	/**Равен не -1 только если сущность это моделька игрока*/
	public int playerID = -1;
	
	/**id Сущности*/
	public int id = -1; // Изначально -1 , id для массива сущностей
	
	/**Тип сущности*/
	public EntityType type;
	
	/**Имя сущности*/
	public String name;
	
	/**Графическое представление сущности*/
	public AbstractModel<?> model;

	/**Жизнь сущьности в процентах*/
	public float heals=100;
	
	public Entity() {
		super();
	}
	
	/**Создает сущность без графического представления*/
	public Entity(int playerID, int id, EntityType type, String name) {
		super();
		this.playerID = playerID;
		this.id = id;
		this.type = type;
		this.name = name;
		status = EntityStatus.ready;
	}
	
	
	/**Создает сущность с полным набором параметров включая модель*/
	public Entity(int playerID, int id, EntityType type, String name, AbstractModel<?> model) {
		super();
		this.playerID = playerID;
		this.id = id;
		this.type = type;
		this.name = name;
		this.model = model;
		status = EntityStatus.ready;
	}
	
	protected AbstractModel<?> getModel() {
		return model;
	}

	protected void setModel(AbstractModel<?> model) {
		this.model = model;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public interface EntityListener {
		/**Метод вызывается игровым объектом для последующей его обработки слушателем*/
		public void EntityEvent(Object event);
	}

	/**Listener для информирования создавшего сущность о событиях с ней произошедших*/
	public EntityListener entityListener;

	
	public void update(float delta) {
	}
	
	/**Назначение слушателя событий сущности*/
	public void setEntityListener(EntityListener entityListener) {
		this.entityListener = entityListener;
	}
	
	protected static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
	
	@Override
	public void dispose() {
	}
}

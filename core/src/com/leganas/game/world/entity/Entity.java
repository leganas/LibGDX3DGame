package com.leganas.game.world.entity;

import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.framework.graphics.AbstractModel;
import com.leganas.game.framework.graphics.AbstractModel.ModelListener;

/**����� ����������� ������� ��������*/
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
	
	/**����� �� -1 ������ ���� �������� ��� �������� ������*/
	public int playerID = -1;
	
	/**id ��������*/
	public int id = -1; // ���������� -1 , id ��� ������� ���������
	
	/**��� ��������*/
	public EntityType type;
	
	/**��� ��������*/
	public String name;
	
	/**����������� ������������� ��������*/
	public AbstractModel<?> model;

	/**����� ��������� � ���������*/
	public float heals=100;
	
	public Entity() {
		super();
	}
	
	/**������� �������� ��� ������������ �������������*/
	public Entity(int playerID, int id, EntityType type, String name) {
		super();
		this.playerID = playerID;
		this.id = id;
		this.type = type;
		this.name = name;
		status = EntityStatus.ready;
	}
	
	
	/**������� �������� � ������ ������� ���������� ������� ������*/
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
		/**����� ���������� ������� �������� ��� ����������� ��� ��������� ����������*/
		public void EntityEvent(Object event);
	}

	/**Listener ��� �������������� ���������� �������� � �������� � ��� ������������*/
	public EntityListener entityListener;

	
	public void update(float delta) {
	}
	
	/**���������� ��������� ������� ��������*/
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

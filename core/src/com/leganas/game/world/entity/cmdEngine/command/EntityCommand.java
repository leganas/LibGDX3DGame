package com.leganas.game.world.entity.cmdEngine.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.leganas.game.Assets;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.Setting;
import com.leganas.game.framework.graphics.effects.BlueExplosion;
import com.leganas.game.framework.graphics.effects.BulletHit;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.interfaces.Command;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.framework.utils.Pooler;
import com.leganas.game.world.entity.gameObject.i3D.Constructor3D;
import com.leganas.game.world.entity.gameObject.i3D.Entity3D;
import com.leganas.game.world.entity.gameObject.i3D.Player3D;
import com.leganas.game.world.entity.gameObject.i3D.Primitive3D;
import com.leganas.game.world.entity.Entity.EntityStatus;
import com.leganas.game.world.entity.Entity.EntityType;
import com.leganas.game.world.entity.EntityController;

/**����� ������ ������������ ���������� �������� �� ����������*/
public abstract class EntityCommand extends Command<EntityController>{
	public enum EntityUpdateType {
		Create, // ������� ����� ��������
		Delete,
		Damage,
		setAnim,
		Update // �������� ������������
	}

	/**����� ���������� ��� ��������, ����� : ������� , ����������, �����������, ������ ��������, ��������*/
	public static class UpdateEntity extends EntityCommand{
		public int playerID = -1;
		public int id;
		public EntityType type;
		public EntityUpdateType entityUpdateType;
		public String name;
		public Object transform;
		public Vector3 impuls;
	    public float direction = 0;
	    public String animateName;
	    public int animLoop = -1;
	    public boolean animSetType = true; // true - ���������� �������� ������, false - ���������� �������� ���������� ����� ���������
	    public float heals;

		
		/**������ ���������� ��� ��������*/
		public UpdateEntity() {
			super();
		}

		/**����� ���������� �������� ��� ������� �������� � ��������� �����������*/
		public UpdateEntity(int playerID, int id, EntityType type, EntityUpdateType entityUpdateType, String name,
				Object transform) {
			super();
			this.playerID = playerID;
			this.id = id;
			this.type = type;
			this.entityUpdateType = entityUpdateType;
			this.name = name;
			this.transform = transform;
		}
		
		
		/**����� ���������� �������� ��� ������� �������� � ��������� �����������*/
		public UpdateEntity(int playerID, int id, EntityType type, EntityUpdateType entityUpdateType, String name,
				Object transform, Vector3 impuls) {
			super();
			this.playerID = playerID;
			this.id = id;
			this.type = type;
			this.entityUpdateType = entityUpdateType;
			this.name = name;
			this.transform = transform;
			this.impuls = impuls;
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

		public EntityUpdateType getEntityUpdateType() {
			return entityUpdateType;
		}

		public void setEntityUpdateType(EntityUpdateType entityUpdateType) {
			this.entityUpdateType = entityUpdateType;
		}

		public Object getTransform() {
			return transform;
		}
		
		/**��������� �������� �� �� ���� ���*/
		boolean isRenderWorld(int worldid){
			if (Setting.isClient()) 
				if (Assets.worldrender == worldid || !Setting.isServer()) return true;
			return false;
		}

		public Event ApplyCommand(EntityController controller) {
			 if (entityUpdateType == EntityUpdateType.Create) {
				 //if (controller.items.get(id) != null) return null;
				 int entityId = id;
				 Entity3D ent = Constructor3D.create3DEntity(getType(), getName(), getPlayerID(),(Matrix4) transform, impuls);
				 if (ent != null) {
					 if (entityId != -1) {
						 // ���� id != -1 �� �������� ��� ����� id � ��������� �� ������� �������
						 controller.put(entityId,ent);
					 } else {
						 // ���� id = -1 �� �������� ��������� �� ������� ������� (�����) 
						 entityId = controller.add(ent);
					 }
					 controller.get(entityId).setId(entityId); // ��������� � ����� ����� �������� �������� � id
					 setName(ent.name); // ������������� ��� ���������� �� �������� ��������� ��������, ����� �� �������� ��������� ����� ��
					 setId(entityId);
				 }
			 }
			 if (entityUpdateType == EntityUpdateType.Update) {
//				 Gdx.app.log("LGame", "Update entity" + id);
				 // ��������� ��� �������������� ��������
				 if (controller.items.get(id) != null){
					 Entity3D ent = (Entity3D) controller.items.get(id);
					 if (ent instanceof Player3D) {
						 ((Player3D) ent).direction = direction;
						 ((Player3D) ent).oldDir = direction;
						 ((Player3D) ent).animateName = animateName;
						 ((Player3D) ent).heals = heals;
					 }
					 Model3DPhysics model = (Model3DPhysics) ent.model;
					 model.instance.transform.set((Matrix4)transform);
				 }
			 }
			 if (entityUpdateType == EntityUpdateType.Delete) {
				try {
					if (isRenderWorld(controller.worldid)) {
						// ���� ���� ��� �������������� �� ������ ���������� ������ ������ (�� ����� ������ ���������)
						Entity3D ent = (Entity3D) controller.items.get(id);
						if (ent.type == EntityType.Primitive || ent.type == EntityType.Player) {
							Model3DPhysics model3d = (Model3DPhysics) ent.model;
							Vector3 posP = Pooler.v3();
							model3d.instance.transform.getTranslation(posP);
							new BlueExplosion(posP);
							MySyper2DGame.inst.assets.sound.get("explosion1").play();
						}
					} 
				} catch (Exception e) {
				}
				controller.remove(id);
			 }
			 if (entityUpdateType == EntityUpdateType.Damage) {
				 if (controller.get(id) != null) {
					 Gdx.app.log("LGame","Entity - " + id + " damage | heals = " +heals);
					 if (isRenderWorld(controller.worldid)) {
							// ���� ���� ��� �������������� �� ������ ���������� ������ ���� ��� ������������
							Entity3D ent = (Entity3D) controller.items.get(id);
							Model3DPhysics model3d = (Model3DPhysics) ent.model;
							Vector3 posP = Pooler.v3();
							model3d.instance.transform.getTranslation(posP);
							new BulletHit(posP);
							MySyper2DGame.inst.assets.sound.get("damage").play();
					 }
					 controller.get(id).heals = heals;
					 if (controller.get(id).heals <= 0) controller.get(id).status = EntityStatus.destroy;
				 }
			 }
			 
			 if (entityUpdateType == EntityUpdateType.setAnim) {
				 if (controller.items.get(id) != null){
					 Entity3D ent = (Entity3D) controller.items.get(id);
					 if (ent instanceof Player3D) {
						 ((Player3D) ent).waitUpdateAnim();
						 if (animSetType) ((Player3D) ent).setAnimate(animateName, animLoop); else ((Player3D) ent).setAnimateToQueue(animateName, animLoop); 
					 }
				 }
			 }
			 /* ������ ���������� ����� �� ��� ������ ����, 
			  ����� ServerGameController ��� ��������� ��� ����
			  ����� ������� ��� �������� ����� ��������� �� ���� ��������
			  ��� ��� ��� ������� ����� ��������� �� �� �������*/ 
			 return this;
		}
	}
}

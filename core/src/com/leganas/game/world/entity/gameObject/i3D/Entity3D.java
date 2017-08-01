package com.leganas.game.world.entity.gameObject.i3D;

import com.leganas.game.framework.graphics.AbstractModel;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv;
import com.leganas.game.world.entity.Entity;

/**����������� �������� � 3D ��������������*/
public abstract class Entity3D extends Entity{
	
	@Override
	protected void setModel(AbstractModel<?> model) {
		super.setModel(model);
	}
	
	public void setModel3D(AbstractModel<ModelInstanceAdv> model) {
		this.model = (Model3DPhysics) model;
	}

	@Override
	public void modelMessage(int modelID, Object msg) {
		// TODO ������������� ��������� �������� ������
		
	}

	@Override
	public void setId(int id) {
		// TODO ������������� ��������� �������� ������
		super.setId(id);
		((Model3DPhysics)model).instance.body.setUserValue(id);
	}
	
	
}

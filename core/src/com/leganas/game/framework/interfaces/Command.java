package com.leganas.game.framework.interfaces;

/**����������� ����� ����������� Event ���� ������� ��� ���� ����*/
public abstract class Command<T> extends Event{
	public abstract Event ApplyCommand(T controller);
	
	@Override
	public Event Apply(Controller<?> controller, int id) {
		return  ApplyCommand((T) controller);
	}

}

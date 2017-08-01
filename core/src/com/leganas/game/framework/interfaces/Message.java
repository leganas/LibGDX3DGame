package com.leganas.game.framework.interfaces;

public abstract class Message<T> extends Event{
	/**����� ����������� ����� �� Message
	 * @param controller - ���������� ��� ������ �������� ����� ����������� �����
	 * @param id - ������������� ���������� �������*/
	public abstract Event ResponseMessage(T controller, int id);

	/** ������� ����� ������� ��������� ����� ����������� replyMessage ����� �� Message*/
	@Override
	public Event Apply(Controller<?> controller, int id) {
		// ������� ����� ������� ��������� ����� ����������� ����� �� Message
		return  ResponseMessage((T) controller,id);
	}
}

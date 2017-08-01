package com.leganas.game.framework.interfaces;

/**����������� ������� ��� ���� ���� 
 * (��� ����� ���� ������� �� ���������� � ����������� ���� ���������)*/
public abstract class Event {
	/**����� ����������� ������� �� �������
	 * @param controller - ���������� � ������� �������� ����� ����������� ����������
	 * @param id - ������������� ���������� ������� (-1 ���� ����������)*/
	public abstract Event Apply(Controller<?> controller, int id);
}

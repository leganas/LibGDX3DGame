package com.leganas.game.screen.gameScreen.ui.inpit;

import com.leganas.game.network.packeges.clientTOserver.ClientMessage.ClientInputMsg;

/**��������� �������� ����� ������������ �����*/
public interface UserInput {
	/**���������� ������� ����������������� ����� ���������
	 * (���� ���������� ��������� ClientMessage)*/
	public void inputEvent(ClientInputMsg msg);
}

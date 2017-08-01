package com.leganas.game.screen.gameScreen.ui.inpit;

import com.leganas.game.network.packeges.clientTOserver.ClientMessage.ClientInputMsg;

/**Интерфейс обратной связи контроллеров ввода*/
public interface UserInput {
	/**Пересылает событие пользовательского ввода слушателю
	 * (типа Клиентское Сообщение ClientMessage)*/
	public void inputEvent(ClientInputMsg msg);
}

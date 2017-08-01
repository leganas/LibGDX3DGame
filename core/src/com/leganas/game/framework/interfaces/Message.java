package com.leganas.game.framework.interfaces;

public abstract class Message<T> extends Event{
	/**Метод реализующий ответ на Message
	 * @param controller - Контроллер при помощи которого будет сформирован ответ
	 * @param id - идентификатор пославшего команду*/
	public abstract Event ResponseMessage(T controller, int id);

	/** Базовый метод который реализует через абстрактный replyMessage ответ на Message*/
	@Override
	public Event Apply(Controller<?> controller, int id) {
		// Базовый метод который реализует через абстрактный ответ на Message
		return  ResponseMessage((T) controller,id);
	}
}

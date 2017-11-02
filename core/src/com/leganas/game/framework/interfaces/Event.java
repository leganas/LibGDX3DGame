package com.leganas.game.framework.interfaces;

/**Абстрактное событие для чего либо 
 * (Это может быть команда на исполнение в последующем либо сообщение)*/
public abstract class Event {
	/**Метод реализующий реакцию на событие
	 * @param controller - Контроллер с помощью которого будет реализовано выполнение
	 * @param id - идентификатор создавшего событие (-1 если неизвестен)*/
	public abstract Event Apply(Controller<?> controller, int id);
}

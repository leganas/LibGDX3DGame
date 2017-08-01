package com.leganas.game.framework.interfaces;

import com.badlogic.gdx.utils.Array;

/**����������� �������� ������� (���������� � ������� � ����������)
 * T - ��� �������*/
public abstract class Manager<T extends Event>{
	/**��������� �������� ����� ����� ������������ ���������*/
	public interface ManagerListener {
		public void ListenerMessage(Object msg);
	}
	
	
	protected Array<T> eventQueue = new Array<T>();
	public Controller<?> controller;
	public int id;
	public ManagerListener listener;
	
	public Manager(Controller<?> controller, int id) {
		super();
		this.controller = controller;
		this.id = id;
	}
	
	
	public void process(){
		 synchronized (eventQueue) {
			 for (T event : eventQueue) {
				 ((Event)event).Apply(controller,id);
			 }
			 eventQueue.clear();
		 }
	}
	
	public void setListener(ManagerListener listener) {
		this.listener = listener;
	}

	public synchronized void addEventToQueue(T event) {
		eventQueue.add(event);
	}
}

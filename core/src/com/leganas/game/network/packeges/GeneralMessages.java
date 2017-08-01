package com.leganas.game.network.packeges;

public class GeneralMessages{
	/**Ѕазовые пакеты используютс€ сервером и клиентом*/
	
	static public class RegisterName{
		public String name;

	}

	static public class UpdateNames{
		public String[] names;

	}

	static public class ChatMessage{
		public String text;
		
		public ChatMessage(String text) {
			super();
			this.text = text;
		}

		public ChatMessage() {
			super();
		}

		
	}

}

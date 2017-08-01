package com.leganas.game.framework.utils;

import com.badlogic.gdx.Gdx;

public class Logs {
	enum logsState {
		on,
		off
	}
	
	public static boolean printWorldID;
	public static void print(String str1, String str2){
		Gdx.app.log(str1, str2);
	}
}

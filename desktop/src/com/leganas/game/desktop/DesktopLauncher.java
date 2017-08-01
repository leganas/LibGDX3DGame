package com.leganas.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.framework.interfaces.CallbackMain;

public class DesktopLauncher   implements CallbackMain{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MySyper2DGame(new DesktopLauncher()), config);
	}

	@Override
	public void showAdMob(boolean show) {
		// Показываем рекламу
		Gdx.app.log("LGame", "Admod desktop show - " + show);
	}
}

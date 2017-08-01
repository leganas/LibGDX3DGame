package com.leganas.game.screen.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.leganas.game.Assets;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.screen.AbstractScreen;
import com.leganas.game.screen.mainMenu.stage.MainMenuStage;

public class MainMenuScreen extends AbstractScreen{
	
	MainMenuStage stage;
	
	public MainMenuScreen() {
		super();
		// Первый экран игры после загрузочного
		// Инициализируем хранилище данных
		MySyper2DGame.inst.assets.init();
		stage = new MainMenuStage();
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		Gdx.app.log("LGame", "Main screen show");

//		MySyper2DGame.assets.music.get("fon").play();
		

	}
	

	@Override
	public void render(float delta) {
		// TODO Автоматически созданная заглушка метода
        Gdx.gl.glViewport ( 0 , 0 , Gdx.graphics.getWidth (), Gdx.graphics.getHeight ()); // оределяем область видимости
        Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
//        MySyper2DGame.inst.setScreen(new GameScreen());
	}

	@Override
	public void resize(int width, int height) {
		// TODO Автоматически созданная заглушка метода
	}

	
	@Override
	public void pause() {
		// TODO Автоматически созданная заглушка метода
		super.pause();
		Gdx.app.log("LGame", "Main screen pause");
	}

	@Override
	public void resume() {
		// TODO Автоматически созданная заглушка метода
		super.resume();
		Gdx.app.log("LGame", "Main screen resume");
	}

	@Override
	public void hide() {
		// TODO Автоматически созданная заглушка метода
		Gdx.app.log("LGame", "Main screen hide");
		stage.dispose();
	}
	
	
}

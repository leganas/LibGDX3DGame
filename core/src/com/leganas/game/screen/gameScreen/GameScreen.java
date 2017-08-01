package com.leganas.game.screen.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.Setting;
import com.leganas.game.MySyper2DGame.GameState;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.controller.ServerGameController.ServerGameState;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.network.packeges.clientTOserver.ClientUpdate;
import com.leganas.game.screen.AbstractScreen;
import com.leganas.game.screen.gameScreen.ui.UserInterface;
import com.leganas.game.screen.gameScreen.ui.UserInterface.UserInterfaceListener;

public class GameScreen extends AbstractScreen implements InputProcessor, UserInterfaceListener{
    InputMultiplexer multiplexer;
    UserInterface ui;

	
	public GameScreen() {
		super();
        if (Setting.isServer()) MySyper2DGame.serverGameController = new ServerGameController();
        if (Setting.isClient()) MySyper2DGame.clientGameController = new ClientGameController();
        
		multiplexer = new InputMultiplexer(); 
		if (Setting.isClient()) {
			ui = new UserInterface();
			ui.setListener(this);
            multiplexer.setProcessors(ui.getInputProcessors());
        }
        multiplexer.addProcessor(this);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		/**ѕоток дл€ ожидани€ готовности сервера
		 * дл€ варианта Server and Client*/
		new Thread("WaitServerCreateWorld") {
			   public void run () 
			   {
			    if (Setting.isServer()) while (ServerGameController.serverGameState != ServerGameState.ready){
			    	try {Thread.sleep(1); // ждем пока сервер создаст мир , иначе робот робот хуй по€витс€
						} catch (InterruptedException e) {
						}
			    	}
			    /** огда дождались сразу запрашиваем вход и все дела*/
			    if (Setting.isClient()) {
			    	if (MySyper2DGame.serverGameController != null) Gdx.app.log("LGame", "Main menu : "+MySyper2DGame.serverGameController.getItems().size + " World registred from the Server");
			    		MySyper2DGame.clientGameController.init();
			    }
			   }
		 }.start();
	}

	@Override
	public void show() {
		// ƒелаем init серверу , там внутри (временно, сразу без запроса) создаетс€ мир
    	if (Setting.isServer()) MySyper2DGame.serverGameController.init();
//    	MySyper2DGame.inst.assets.music.get("fon").play();
	}

	@Override
	public void render(float delta) {
		if (MySyper2DGame.gameState == GameState.Dispose || MySyper2DGame.gameState == GameState.Stop) return;
        Gdx.gl.glViewport ( 0 , 0 , Gdx.graphics.getWidth (), Gdx.graphics.getHeight ()); // определ€ем область видимости
//        Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Setting.isClient()) MySyper2DGame.clientGameController.update(delta);
        if (Setting.isServer()) MySyper2DGame.serverGameController.update(delta);
        
		// ќтрисовываем то что нужно
		if (Setting.isClient()) MySyper2DGame.clientGameController.render(delta); 
		 else 
			if (Setting.isServer()) MySyper2DGame.serverGameController.render(delta);
			
		if (Setting.isClient()){
			ui.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO јвтоматически созданна€ заглушка метода
		
	}

	@Override
	public void hide() {
		if (ui != null) ui.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	if (Setting.isClient()) {
    		if (MySyper2DGame.serverGameController != null) Gdx.app.log("LGame", "Main menu : "+MySyper2DGame.serverGameController.getItems().size + " World registred from the Server");
    	}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO јвтоматически созданна€ заглушка метода
		return false;
	}

	/**ѕринимаем от пользовательского интерфейса команды (обрабатываем если они должны выполнитс€ у нас)
	 * если нет то отправл€ем на сервер*/
	@Override
	public void userInterfaceEvent(Event msg) {
		if (MySyper2DGame.clientGameController != null) {
			if (msg instanceof ClientMessage){ 
				ClientUpdate update = new ClientUpdate(); 
				update.addUpdate((ClientMessage) msg);
				MySyper2DGame.clientGameController.client.sendMessage(msg);
			}
		}
	}

}

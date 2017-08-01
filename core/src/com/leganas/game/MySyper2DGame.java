package com.leganas.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.leganas.game.controller.ClientGameController;
import com.leganas.game.controller.ServerGameController;
import com.leganas.game.framework.interfaces.CallbackMain;
import com.leganas.game.framework.utils.Pooler;
import com.leganas.game.screen.loading.LoadingScreen;



public class MySyper2DGame  extends Game{
	public enum GameState {
		Off,
		Create,
		Init,
		Running,
		Pause,
		Stop,
		Dispose
	}
	public static GameState gameState= GameState.Off;
	public static MySyper2DGame inst;
	public Assets assets;
    public AssetManager manager;
    public static ClientGameController clientGameController;
    public static ServerGameController serverGameController;

    /**Через него осуществляется связь с внешней средой Android, Desktop*/
    public CallbackMain handler;

    public MySyper2DGame (CallbackMain handler){
    	this.handler = handler;
    	MySyper2DGame.inst = this;
    	Pooler.init();
    }
    
    
	
	@Override
	public void create () {
		Gdx.app.log("LGame", "Game create");
    	manager = new AssetManager();
    	assets = new Assets();
        setScreen(new LoadingScreen());
	}

	
	
	@Override
	public void pause() {
		super.pause();
		Gdx.app.log("LGame", "Game pause");
	}


	@Override
	public void resume() {
		super.resume();
		Gdx.app.log("LGame", "Game resume");
	}
	
	


	@Override
	public void dispose () {
		super.dispose();
		gameState= GameState.Dispose;
		manager.dispose();
		assets.dispose();
		if (clientGameController != null) clientGameController.dispose();
		if (serverGameController != null) serverGameController.dispose();
		Gdx.app.log("LGame", "Game dispose all resources and close app");
	}
}

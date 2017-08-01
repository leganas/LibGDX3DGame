package com.leganas.game.screen.mainMenu.stage;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryonet.Client;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.Setting;
import com.leganas.game.Setting.ServerType;
import com.leganas.game.network.Network;
import com.leganas.game.screen.gameScreen.GameScreen;
import com.leganas.game.screen.mainMenu.stage.actor.ButtonActor;


public class MainMenuStage extends Stage{

	int scrX, scrY;
    float scalingX, scalingY;

    TextureAtlas buttons;
    ButtonActor btnServer, btnServerClient, btnClient;

    protected Label label; // Label - надпись
    protected BitmapFont font; // шрифт битмаповский для рисования в пространстве OpenGL
    protected StringBuilder stringBuilder; // билдер для сборки строк из бит мап шрифта
    
    
    
	public MainMenuStage() {
		super();
        scrX = Gdx.graphics.getWidth();
        scrY = Gdx.graphics.getHeight();
        float rat = (float)scrX / (float)scrY;
        scalingX = scrX/1500f;
        scalingY = scrY/500f;
        
//        Gdx.app.log("!!!","" + scrX + " | " + scrY + " | rat = " + rat);
        
        font = new BitmapFont(); // новый шрифт (по умолчанию там загружается таумс нью роман)
        buttons = new TextureAtlas(Gdx.files.internal("atlas/main_menu.atlas"));
        
        
        
        btnServer = new ButtonActor(buttons.findRegion("server"), scrX/6, (scrY/2)+150,scalingX,scalingY, "server");
        btnServerClient = new ButtonActor(buttons.findRegion("serverclient"), scrX/6, (scrY/2),scalingX,scalingY, "serverclient"); 
        btnClient = new ButtonActor(buttons.findRegion("client"), scrX/6, (scrY/2)-150,scalingX,scalingY, "client");
        
        addActor(btnServer);
        addActor(btnServerClient);
        addActor(btnClient);
	}

	@Override
	public void draw() {
		// TODO Автоматически созданная заглушка метода
		super.draw();
	}

	String aName;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Actor actor = hit(screenX, scrY-screenY, true);
        if(actor == null){
            aName = "null";
            return false;
        }else{
            Gdx.app.log("LGame"," Tx = " +  screenX + " | Ty = " + screenY + " hit = " + actor.getName()) ;
            aName = actor.getName();
        	if (aName.equals("server")) {
	            Setting.setServer(ServerType.OnLine);
	        	Setting.setClient(false);
	        	Setting.setClientHost("localhost");
        	}
        	if (aName.equals("serverclient")) {
	            Setting.setServer(ServerType.OnLine);
	        	Setting.setClient(true);
	        	Setting.setClientHost("localhost");
        	}
        	if (aName.equals("client")) {
	            Setting.setServer(ServerType.Off);
	        	Setting.setClient(true);
	        	Client client;
	        	client = new Client(25600,25600);
	    		client.start();
	    		InetAddress address = client.discoverHost(Network.portUDP,5000);
	    		Gdx.app.log("LGame","Connect to LAN server " + address);
	    		if (address == null) {
	    			Setting.setClientHost("localhost");
	    		} else Setting.setClientHost(address.getHostAddress());

	    		Gdx.app.log("LGame","Connect to LAN server " + Setting.getClientHost());
	        	client.stop();
        	}
    		MySyper2DGame.inst.setScreen(new GameScreen());
            return true;
        }
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Actor actor = hit(screenX, scrY-screenY, true);
        if(actor == null){
//            aName = "null";
            return false;
        }else{
//            aName = actor.getName();
        }
        return true;
	}
	
	@Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor  actor  = super.hit(x,y,touchable);
        return actor;
    }

	@Override
	public void dispose() {
		// TODO Автоматически созданная заглушка метода
		super.dispose();
		buttons.dispose();
		font.dispose();
	}
	
	
}

package com.leganas.game.screen.gameScreen.ui.inpit.mobile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.IntMap;
import com.leganas.game.Setting;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.screen.gameScreen.ui.inpit.UserInput;
import com.leganas.game.screen.gameScreen.ui.inpit.mobile.actor.MyTouchPad;
import com.leganas.game.screen.gameScreen.ui.inpit.mobile.actor.SmallButtonActor;


/**Визуальные элементы пользовательского интерфейса перемещения персонажа*/
public class MobileInputItems  extends Stage{
	
	/**Будем передавать контроллеру по средством слушателя события ввода*/
	UserInput listener;
    public void setListener(UserInput listener) {
		this.listener = listener;
	}
    
    IntMap<ClientMessage.ClientInputMsg> arrayInput = new IntMap<ClientMessage.ClientInputMsg>();
    
	int scrX, scrY;
    float scaling;
    int oldX =0, oldY = 0, newX = 0, newY = 0;
    
    TextureAtlas buttons;
    SmallButtonActor btnForward, btnBack, btnRight, btnLeft, btnRotateRight, btnRotateLeft, btnJump, btnFire;

    protected Label label; // Label - надпись
    protected BitmapFont font; // шрифт битмаповский для рисования в пространстве OpenGL
    protected StringBuilder stringBuilder; // билдер для сборки строк из бит мап шрифта
    
    
    protected MyTouchPad touchPad;
    /**Human User Interface
     * 2D сцена с кнопками пользовательского интерфейса ввода*/
	public MobileInputItems() {
		super();
		
        scrX = Gdx.graphics.getWidth();
        scrY = Gdx.graphics.getHeight();
        scaling = scrX/1280f;
        
        font = new BitmapFont(); // новый шрифт (по умолчанию там загружается таумс нью роман)
        buttons = new TextureAtlas(Gdx.files.internal("atlas/buttons.atlas"));
        btnForward = new SmallButtonActor(buttons.findRegion("forward"), 16f, 5f,scaling, "forward");
        btnBack = new SmallButtonActor(buttons.findRegion("back"), 16f, 1f, scaling, "back");
        btnRight = new SmallButtonActor(buttons.findRegion("right"), 18f, 3f,scaling, "right");
        btnLeft = new SmallButtonActor(buttons.findRegion("left"), 14f, 3f, scaling, "left");
        btnRotateRight = new SmallButtonActor(buttons.findRegion("rotate_right"), 1f, 2f, scaling, "rotate_left");
        btnRotateLeft = new SmallButtonActor(buttons.findRegion("rotate_left"), 3f, 2f, scaling, "rotate_right");
        btnJump = new SmallButtonActor(buttons.findRegion("jump"), 5f+2f, 2f, scaling, "jump");
        btnFire = new SmallButtonActor(buttons.findRegion("jump"), 7f+2f, 2f, scaling, "fire");
        
        addActor(btnForward);
        addActor(btnBack);
        addActor(btnRight);
        addActor(btnLeft);
        addActor(btnJump);
        addActor(btnFire);
        if (Setting.touchPadFlag == false) {
          addActor(btnRotateRight);
          addActor(btnRotateLeft);
        } else {
            touchPad = new MyTouchPad();
            addActor(touchPad.touchpad);
        }

        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE)); // воздаём label (объект типа строчка)
        addActor(label); // создаём новую 2х мерную сцену
        stringBuilder = new StringBuilder(); // новый сборочник строк 

	}
	
	public void setTextLabel(String text){
	}
	
	

	@Override
	public void draw() {
        // формируем строку
        act(Gdx.graphics.getDeltaTime());
		super.draw();
	}



	String aName;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
        screenY = scrY - screenY;
        Actor actor = hit(screenX, screenY, true);
        if(actor == null){
            aName = "null";
            ClientMessage.ClientInputMsg msg = new ClientMessage.ClientInputMsg(aName,true); 
            arrayInput.put(pointer, msg);
            listener.inputEvent(msg);
            return false;
        }else{
            aName = actor.getName();
            ClientMessage.ClientInputMsg msg = new ClientMessage.ClientInputMsg(aName,true); 
            arrayInput.put(pointer, msg);
            listener.inputEvent(msg);
            return true;
        }
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	super.touchUp(screenX, screenY, pointer, button);
        screenY = scrY - screenY;
        //Actor actor = hit(screenX, screenY, true);
        if (arrayInput.get(pointer) != null){
        	ClientMessage.ClientInputMsg msg = arrayInput.get(pointer);
        	if (msg.param != -2) msg.msg = "skip_move";
        	msg.status = false;
            listener.inputEvent(msg);
        }
        return true;
    }
          
    
    

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	super.touchDragged(screenX, screenY, pointer);
    	
    	/**Проверяем было ли движение по тачпаду*/
    	if (Setting.touchPadFlag) {
    		float touchPadX = touchPad.touchpad.getKnobPercentX();
    		float touchPadY = touchPad.touchpad.getKnobPercentY();
    	
    	 if (touchPadX !=0  || touchPadY != 0) {
//        	Gdx.app.log("LGame","KNOB " + " | X - "+touchPadX + " | Y - " + touchPadY);
            listener.inputEvent(new ClientMessage.ClientInputMsg("skip_move",true));
            if (touchPadY != 0) {
            	if (touchPadY > 0) aName = "forward"; else aName = "back";  
                ClientMessage.ClientInputMsg msg = new ClientMessage.ClientInputMsg(aName,true); 
                msg.param = Math.abs(touchPadY);
                arrayInput.put(pointer, msg);
                listener.inputEvent(msg);
            }
            if (touchPadX != 0) {
            	if (touchPadX > 0) aName = "rotate_right"; else aName = "rotate_left";  
                ClientMessage.ClientInputMsg msg = new ClientMessage.ClientInputMsg(aName,true);
                msg.param = Math.abs(touchPadX);  
                arrayInput.put(pointer, msg);
                listener.inputEvent(msg);
            }
    	 }
    	}
//    	Gdx.app.log("LGame","pointer : "+ pointer + " | X - "+screenX + " | Y - " + screenY);
    	//    	if (pointer > 0) return true;
        return false;
    }
    
    @Override
    public Actor hit(float x, float y, boolean touchable) {
        Actor  actor  = super.hit(x,y,touchable);
        return actor;
    }

	@Override
	public void dispose() {
		buttons.dispose();
	    font.dispose();
	    if (touchPad != null) touchPad.dispose();
	    Gdx.app.log("LGame"," Human User Interface HUI dispose");
	}
}

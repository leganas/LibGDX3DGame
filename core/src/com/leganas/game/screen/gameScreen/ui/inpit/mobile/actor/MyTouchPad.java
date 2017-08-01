package com.leganas.game.screen.gameScreen.ui.inpit.mobile.actor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class MyTouchPad implements Disposable{
	public Touchpad touchpad;
	private TouchpadStyle touchpadStyle;
	private Skin touchpadSkin;
	private Drawable touchBackground;
	private Drawable touchKnob;
	
	public MyTouchPad() {
		super();
		
	//Create a touchpad skin	
	touchpadSkin = new Skin();
	

	
	//Set background image
//	touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
	//Set knob image
/*	Pixmap.setBlending(Blending.None);
	Pixmap knob = new Pixmap(200, 200, Format.RGBA8888);
	knob.setColor(1, 1, 1, .6f);
	knob.fillCircle(100, 100, 50);*/
	
	
	touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
	//Create TouchPad Style
	touchpadStyle = new TouchpadStyle();
	//Create Drawable's from TouchPad skin
	
	Pixmap.setBlending(Blending.None);
	Pixmap background = new Pixmap(200, 200, Format.RGBA8888);
    background.setColor(1, 1, 1, .6f);
    background.fillCircle(100, 100, 100);
    touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(background)));
	
	
//	touchBackground = touchpadSkin.getDrawable("touchBackground");
	touchKnob = touchpadSkin.getDrawable("touchKnob");
	
	//Apply the Drawables to the TouchPad Style
//	touchpadStyle.background = touchBackground;
	touchpadStyle.knob = touchKnob;
	//Create new TouchPad with the created style
	touchpad = new Touchpad(10, touchpadStyle);
	touchpad.setName("touchpad");
	//setBounds(x,y,width,height)
	touchpad.setBounds(15, 15, 200, 200);
	}

	@Override
	public void dispose() {
		touchpadSkin.dispose();
	}

	
}

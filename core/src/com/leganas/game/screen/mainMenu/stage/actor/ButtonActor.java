package com.leganas.game.screen.mainMenu.stage.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class ButtonActor extends Actor{
    TextureRegion region;
    
    public ButtonActor(TextureRegion tex, float oX, float oY, float scaleX, float scaleY, String name){
        region = new TextureRegion(tex);
        setScale(scaleX, scaleY);
        this.setTouchable(Touchable.enabled);
        setX(oX);
        setY(oY);
        setWidth(region.getTexture().getWidth());
        setHeight(region.getTexture().getHeight());
        setName(name);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw (region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return x >= 0 && x < getWidth()	&& y >= 0 && y < getHeight() ? this : null;
	}
    
    
    
}
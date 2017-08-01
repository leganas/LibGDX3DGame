package com.leganas.game.screen.gameScreen.ui.inpit.mobile.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**ћаленька€ кнопка размером 64х64 пиксел€, (если загрузить в неЄ текстуру больших размеров то будет беда с регистрацией нажатий)*/
public class SmallButtonActor extends Actor{
    TextureRegion region;
    final float pointX, pointY, scaleXY;
    
    public SmallButtonActor(TextureRegion tex, float oX, float oY, float sca, String name){
        region = new TextureRegion(tex);
        pointX = oX*sca*64;
        pointY = oY*sca*40;
        scaleXY = sca;
        this.setTouchable(Touchable.enabled);
        setName(name);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(region, pointX, pointY, getOriginX(), getOriginY(),
                region.getRegionWidth(), region.getRegionHeight(), scaleXY, scaleXY, getRotation ());
    }
    
    @Override
    public Actor hit (float x, float y, boolean touchable) {
        return x >= pointX && x < pointX +100*scaleXY && y >= pointY && y < pointY + 100*scaleXY ? this : null;
    }
}
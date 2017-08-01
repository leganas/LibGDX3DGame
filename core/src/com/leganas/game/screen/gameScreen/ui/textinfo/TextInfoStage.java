package com.leganas.game.screen.gameScreen.ui.textinfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.leganas.game.Assets;

/**���������� �������� ����������������� ���������� ����������� ���������*/
public class TextInfoStage  extends Stage{
	
	int scrX, scrY;
    float scaling;
    int oldX =0, oldY = 0, newX = 0, newY = 0;
    
    protected Label label; // Label - �������
    protected BitmapFont font; // ����� ������������ ��� ��������� � ������������ OpenGL
    protected StringBuilder stringBuilder; // ������ ��� ������ ����� �� ��� ��� ������
    
    
    /**���������� ��������� ����������*/
	public TextInfoStage() {
		super();
		
        scrX = Gdx.graphics.getWidth();
        scrY = Gdx.graphics.getHeight();
        scaling = scrX/1280f;
        
        font = new BitmapFont(); // ����� ����� (�� ��������� ��� ����������� ����� ��� �����)
        
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE)); // ������ label (������ ���� �������)
        label.setX(0);
        label.setY(0);
        addActor(label); // ��������� ����� Actor � ���� �����
        stringBuilder = new StringBuilder(); // ����� ��������� ����� 

	}
	
	public void setTextLabel(String text){
	}
	
	

	@Override
	public void draw() {
        // ��������� ������
        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ")
        			 .append(Gdx.graphics.getFramesPerSecond())
        			 .append(Assets.textInfo);
        label.setText(stringBuilder);
        act(Gdx.graphics.getDeltaTime());
		super.draw();
	}



	String aName;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		super.touchDown(screenX, screenY, pointer, button);
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	super.touchUp(screenX, screenY, pointer, button);
        return false;
    }
          
    
    

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	super.touchDragged(screenX, screenY, pointer);
        return false;
    }
    

	@Override
	public void dispose() {
	    font.dispose();
	    Gdx.app.log("LGame","TextInfoStage dispose");
	}
}

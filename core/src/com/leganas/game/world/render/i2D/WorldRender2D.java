package com.leganas.game.world.render.i2D;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.render.WorldRender;

public class WorldRender2D extends WorldRender{

	final int WIDTH  = 480;
	final int HEIGHT = 320;
	
	/**Ортаграфическая камера*/
	private OrthographicCamera  cam;
    /** Класс для прорисовки моделек */
    ModelBatch modelBatch;
    /**Целочисленная переменная - в качестве счетчика отрисованных моделей*/
    int visibleModels; 
    /**Label - надпись*/
    protected Label label;
    protected BitmapFont font; // шрифт битмаповский для рисования в пространстве OpenGL

    

	public WorldRender2D() {
		super();
	}
	
	public WorldRender2D(GWorld world) {
		super();
		this.world = world;
	}
	
	public void Init(){
		modelBatch = new ModelBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);  
		cam.position.set(WIDTH / 2, HEIGHT / 2, 0);
//		gameWorldStage = new GameWorldStage(new ScreenViewport());
	}
	
	public GWorld getWorld() {
		return world;
	}

	public void setWorld(GWorld world) {
		this.world = world;
	}

	public void render(float delta){
	}

	@Override
	public void dispose() {
		if (modelBatch != null) modelBatch.dispose();
	}

	@Override
	public Camera getCamera() {
		// TODO Автоматически созданная заглушка метода
		return cam;
	}
}

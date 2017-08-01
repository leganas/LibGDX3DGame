package com.leganas.game.screen.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.screen.AbstractScreen;
import com.leganas.game.screen.loading.actor.LoadingBar;
import com.leganas.game.screen.mainMenu.MainMenuScreen;


/**
 * @author Mats Svensson
 */
public class LoadingScreen extends AbstractScreen {

    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;
    boolean finishinit = false;
    
    public LoadingScreen() {
        super();
    }

    @Override
    public void show() {
        finishinit = false;
		Gdx.app.log("LGame", "Loading screen show");

    	MySyper2DGame.inst.handler.showAdMob(true); // Включаем банер
        // Загружаем атлас с картинками для экрана загрузки
    	MySyper2DGame.inst.manager.load("data/loading.pack", TextureAtlas.class);
        // Ждём окончания загрузки
    	MySyper2DGame.inst.manager.finishLoading();

        // Создаем новую 2D сцену
        stage = new Stage();

        // Получаем атлас из памяти из атласа
        TextureAtlas atlas = MySyper2DGame.inst.manager.get("data/loading.pack", TextureAtlas.class);

        // Получаем отдельные картинки из атласа
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Получаем 2D анимацию из атласа
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        // loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Добавляем все элементы 2D сцены
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);
        new Thread("Loading") {
        	   public void run () {
        	        Gdx.app.log("LGame", "loading data");
        	        loading(); // Грузим наши данные
        	   }
        }.start();
    }
    
    void loading(){
        // Тут мы грузим всё что у нас есть
    	MySyper2DGame.inst.manager.load("music/start.mp3", Music.class);
    	MySyper2DGame.inst.manager.load("atlas/main_menu.atlas", TextureAtlas.class);
    	MySyper2DGame.inst.manager.load("models/robot3.g3db", Model.class);
    	MySyper2DGame.inst.manager.load("models/monkey.g3db", Model.class);
    	MySyper2DGame.inst.manager.load("models/teabox.g3db", Model.class);
    	MySyper2DGame.inst.manager.load("models/istrebitel_mini.g3db", Model.class);
    	MySyper2DGame.inst.manager.load("models/mount.g3db", Model.class);
        
    	MySyper2DGame.inst.manager.load("textures/ground.jpg", Texture.class);
        finishinit = true;
   }
    

    @Override
    public void resize(int width, int height) {
        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

        @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glViewport ( 0 , 0 , Gdx.graphics.getWidth (), Gdx.graphics.getHeight ()); // оределяем область видимости
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (MySyper2DGame.inst.manager.update() && finishinit) { // Load some, will return true if done loading
//            if (Gdx.input.isTouched()) { // If the screen is touched after the game is done loading, go to the main menu screen
        	  MySyper2DGame.inst.handler.showAdMob(false); // Выключаем банер
              Gdx.app.log("LGame", "finish loading data");
        	  MySyper2DGame.inst.setScreen(new MainMenuScreen());
        }

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, MySyper2DGame.inst.manager.getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
    	MySyper2DGame.inst.manager.unload("data/loading.pack");
    	stage.dispose();
    	Gdx.app.log("LGame", "Loading screen dispose");
    }

}

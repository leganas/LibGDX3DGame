package com.leganas.game.world.render.i3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.leganas.game.Assets;
import com.leganas.game.Setting;
import com.leganas.game.framework.graphics.effects.Particles;
import com.leganas.game.framework.graphics.engine3D.Model3DPhysics;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv;
import com.leganas.game.framework.graphics.engine3D.Player3DCamera;
import com.leganas.game.world.GameWorld3D;
import com.leganas.game.world.GWorld;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.gameObject.i3D.Entity3D;
import com.leganas.game.world.entity.gameObject.i3D.Player3D;
import com.leganas.game.world.render.WorldRender;

public class WorldRender3D extends WorldRender{
	/**Перспективная камера*/
    public Player3DCamera camera;

    /** Класс для прорисовки моделек */
    public ModelBatch modelBatch;
    /**Целочисленная переменная - в качестве счетчика отрисованных моделей*/
    public int visibleModels; 
    /**Label - надпись*/
    protected Label label;
    protected BitmapFont font; // шрифт битмаповский для рисования в пространстве OpenGL
    protected String animControllerEvent;
    /**Свет*/
    public Environment environment;
    /**Эффекты*/
    
    Vector3 position = new Vector3(); // Временная переменная вектора хранящего центр 3D-объекта (текущего или проверяемого (рабочая переменная (временная)))
    GameWorld3D world3d;

    Particles particles;
    
	public WorldRender3D() {
		super();
	}

	public WorldRender3D(GWorld world) {
		super(world);
		world3d = (GameWorld3D)world;
		
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.3f, 0.4f, 0.3f, 1f));
        
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        camera = new Player3DCamera((GameWorld3D)world);
        camera.cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.cam.near = 1f;
        camera.cam.far = 300f;
        camera.cam.update();
        
        particles = new Particles(this);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.5f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
        
        if (Assets.playerId  != -1) { 
        	Vector3 posP = new Vector3();
        	if (world.entityController.items.get(Assets.playerId) instanceof Player3D){
        		Player3D plr3D = ((Player3D)world.entityController.items.get(Assets.playerId));
        	
        	if (plr3D != null){
        		((ModelInstanceAdv)plr3D.model.instance).transform.getTranslation(posP);
            	camera.cam.position.set((float) (posP.x - camera.zoom*Math.sin(Math.toRadians(plr3D.direction+camera.getDirectionPlusX()))),
					    posP.y+13f, 
					    (float) (posP.z - camera.zoom*Math.cos(Math.toRadians(plr3D.direction+camera.getDirectionPlusX()))));
            	camera.cam.lookAt(posP.x,posP.y+13,posP.z);
            	camera.cam.update();
        	}
        	}
        }
	
		synchronized (world.entityController.items) {
        modelBatch.begin(camera.cam);
           if (world != null){
			   for (Entry<Entity> tree : new IntMap.Entries<Entity>(world.entityController.items))
				 if (tree.value != null) {
					Entity3D ent3D = (Entity3D)tree.value;
					synchronized (ent3D) {
						if (ent3D instanceof Player3D) {
							((Player3D) ent3D).updateFlag = true;
							((Player3D) ent3D).animController.update(delta * 5);
							((Player3D) ent3D).updateFlag = false;
						}
					}
					Model3DPhysics model = (Model3DPhysics)ent3D.model;
					if (IsVisible(camera.cam, model.instance)) {
						if (Setting.isMobile()){
							if (model.environmentEconom == true) {
								modelBatch.render(model.instance);
							} else modelBatch.render(model.instance, environment);
						}else modelBatch.render(model.instance, environment);
					}
				}
		   } 
        modelBatch.end();
        drawParticleEffects();
		}
	}
	
	
	private void drawParticleEffects() {
		Particles.inst.system.update();
		Particles.inst.system.begin();
		Particles.inst.system.draw();
		Particles.inst.system.end();
		modelBatch.render(Particles.inst.system);
	}

    protected boolean IsVisible(final Camera cam, final ModelInstanceAdv instance) {
        instance.transform.getTranslation(position); // получаем вектор до основания модели
        position.add (instance.center); // прибовляем ветор до его центра 
        // теперь в position хранится у нас цент нашей 3D модельки 
        // фото пояснение http://olelucoye.ru/image/Frustum/center_model.jpg
        return cam.frustum.sphereInFrustum(position, instance.radius); // возвращает труе если шар ограничивающей модельку поподает в область видимости камеры
    }


	@Override
	public void dispose() {
		super.dispose();
		modelBatch.dispose();
		if (font != null) font.dispose();
	}

	@Override
	public Camera getCamera() {
		return camera.cam;
	}
	
	

}

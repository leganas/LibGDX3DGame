package com.leganas.game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.Particle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leganas.game.framework.graphics.effects.Particles;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv;
import com.leganas.game.framework.graphics.engine3D.ModelInstanceAdv.Constructor;
import com.leganas.game.framework.graphics.engine3D.Physics;

public class Assets implements Disposable{
	// ?????? ??????
	public HashMap<String,Music> music;
	// ?????? ??????
	public HashMap<String,Sound> sound;
	Texture texture;
	//?????? ????????
	public Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();
    /**?????? 3D ???????*/
    public Array<Model> model3D = new Array<Model>();
    
    /**?????? (????????????? ???????) ??? ????????? ????? ??????? , ? ?????? String */ 
    public ArrayMap<String, ModelInstanceAdv.Constructor> constructors;
    
    /**??????? ????????*/
    Particles particles; 
    
    public static String textInfo = "";
    
    public static int playerId = -1;
    
    public static int worldrender = -1;

	/**????????? ?????? ??? ???? ??????????? ????*/
	public Assets() {
		super();
		music = new HashMap<String,Music>();
		sound = new HashMap<String,Sound>();
	}

	public void init(){
		System.gc();
		loadAll();
		Bullet.init();

		/**?????????? ?????????*/
		int scale = 5;
		// ??? ?????????? ??? ????????? ?????? ?????????? ?????? ???? (?? ??????? , ? ?????? ??????????? ??? ??????????? ?? ???????????? ?????????????)
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "ball";
        modelBuilder.part("ball", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).sphere(1f*scale, 1f*scale, 1f*scale, 20, 20);
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).box(1f*scale, 1f*scale, 1f*scale);
        modelBuilder.node().id = "cone";
        modelBuilder.part("cone", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE))).cone(1f*scale, 2f*scale, 1f*scale, 20);
        modelBuilder.node().id = "cylinder";
        modelBuilder.part("cylinder", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW))).cylinder(1f*scale, 2f*scale, 1f*scale, 20);
        modelBuilder.node().id = "capsule";
        modelBuilder.part("capsule", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).capsule(0.5f*scale, 2f*scale, 20);
        /*????????? ??????????? ?????? ?0*/
        model3D.add(modelBuilder.end()); 
        
        /**????????? ?????? ?1,2,3*/
        model3D.add(MySyper2DGame.inst.manager.get("models/monkey.g3db", Model.class));
        model3D.add(MySyper2DGame.inst.manager.get("models/teabox.g3db", Model.class));
        model3D.add(MySyper2DGame.inst.manager.get("models/istrebitel_mini.g3db", Model.class));
        
        /**?????????????? ???????????*/
        constructors = new ArrayMap<String, ModelInstanceAdv.Constructor>(String.class, ModelInstanceAdv.Constructor.class);
        constructors.put("monkey", new ModelInstanceAdv.Constructor(model3D.get(1), Physics.createConvexHullShape(model3D.get(1), true, 0), 1f));
        constructors.put("teabox", new ModelInstanceAdv.Constructor(model3D.get(2), Physics.createConvexHullShape(model3D.get(1), true, 0), 1f));
        constructors.put("istrebitel", new ModelInstanceAdv.Constructor(model3D.get(3), Physics.createConvexHullShape(model3D.get(3), true, 0), 10f));
        constructors.put("ball", new ModelInstanceAdv.Constructor(model3D.get(0), "ball", new btSphereShape(0.5f*scale), 1f));
        constructors.put("box", new ModelInstanceAdv.Constructor(model3D.get(0), "box", new btBoxShape(new Vector3(0.5f*scale, 0.5f*scale, 0.5f*scale)), 1f));
        constructors.put("cone", new ModelInstanceAdv.Constructor(model3D.get(0), "cone", new btConeShape(0.5f*scale, 2f*scale), 1f*scale));
        constructors.put("cylinder", new ModelInstanceAdv.Constructor(model3D.get(0), "cylinder", new btCylinderShape(new Vector3(0.5f*scale, 1f*scale, 0.5f*scale)), 1f));
        constructors.put("capsule", new ModelInstanceAdv.Constructor(model3D.get(0), "capsule", new btCapsuleShape( 0.5f*scale, 1f*scale), 1f));

		/**????????? ?????? ????? ?4*/
        TextureAttribute land_attr = TextureAttribute.createDiffuse(new Texture("img/land.jpg"));
        Material land = new Material(land_attr);
//        model.add(modelBuilder.createBox(200f, 0.1f, 200f, land, Usage.Position|Usage.Normal|Usage.TextureCoordinates));
        // ?????
        model3D.add(MySyper2DGame.inst.manager.get("models/mount.g3db", Model.class));

        // ????????? ??????
        model3D.add(MySyper2DGame.inst.manager.get("models/robot3.g3db", Model.class));

	}

    void loadAll(){
        music.put("fon",MySyper2DGame.inst.manager.get("music/start.mp3", Music.class));
		sound.put("step", Gdx.audio.newSound(Gdx.files.internal("sounds/step.mp3")));
		sound.put("jump", Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3")));
		sound.put("shot", Gdx.audio.newSound(Gdx.files.internal("sounds/shot.ogg")));
		sound.put("explosion1", Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.ogg")));
		sound.put("damage", Gdx.audio.newSound(Gdx.files.internal("sounds/Damage.ogg")));
	}
	
	
	@Override
	public void dispose() {
		// ?????????? ??? ??????
		for (Map.Entry<String, Music> tree : music.entrySet()) {tree.getValue().dispose();}
		music.clear();
		for (Map.Entry<String, Sound> tree : sound.entrySet()) {tree.getValue().dispose();}
		sound.clear();
		if (texture != null) texture.dispose();;
		/**?????? 3D ???????*/
		model3D.clear();
	    /**?????? (????????????? ???????) ??? ????????? ????? ??????? , ? ?????? String */ 
		for (Entry<String, Constructor> tree : constructors.entries()) tree.value.dispose();
		constructors.clear();
		Gdx.app.log("LGame", "Assets dispose");
	}
}

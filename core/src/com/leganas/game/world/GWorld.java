package com.leganas.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.framework.graphics.engine3D.Contacter;
import com.leganas.game.world.entity.EntityController;
import com.leganas.game.world.entity.EntityController.EntityControllerListener;
import com.leganas.game.world.render.WorldRender;

public class GWorld implements Disposable,EntityControllerListener {
	public interface WorldListener {
		public void worldMessage(int worldid, Object msg);
	}
	public static Contacter contacter;
	public int id;
	public String name;
	protected WorldRender render;
	public EntityController entityController;
	protected WorldListener listener;
	
	public GWorld() {
		super();
		if (contacter == null) contacter = new Contacter();
		entityController = new EntityController(id);
		entityController.setListener(this);
	}
	
	public void setListener(WorldListener listener) {
		this.listener = listener;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.entityController.worldid = id;
	}


	public void setWorldRender(WorldRender render){
		this.render = render;
	}

	@Override
	public void entityControllerMessage(Object msg) {
		// Тут мир получает сообщения от своего контроллера сущностей
		// Отправляем его слушателю (ServerGameController)
		listener.worldMessage(id, msg);
	}

	/**Обновляем состояние игрового мира*/
	public void update(float delta) {
		entityController.update(delta);
	}
	
	/**Рисует игровой мир*/
	public void render(float delta){
		// Рисуем игровой мир
		if (render != null) render.render(delta);
	}

	@Override
	public void dispose() {
		render.dispose();
		entityController.dispose();
		Gdx.app.log("LGame", "World : "+id+" | "+ name + " dispose");
	}

}

package com.leganas.game.framework.graphics.engine3D;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.framework.graphics.AbstractPhysics;
import com.leganas.game.world.entity.Entity;
import com.leganas.game.world.entity.gameObject.i3D.Entity3D;


/**Класс наделяет наш мир реальными физическими свойствами 
 * (+ реализацией обработки столкновений объектов в рамках реального физ. мира)
 * PS. но только мир решает для каких объектов применять симуляцию физики мира. а для каких нет 
*/
public class Physics extends AbstractPhysics implements Disposable {
	boolean collision_flag;  
	/**Конфигурация для диспетчера определения столкновений(при помощи неё он он инициализирует алгоритм определения столкновений)*/
	btCollisionConfiguration collisionConf;
    /**Диспетчер управления алгоритмами определения коллизий*/
    btDispatcher collisionDispatcher;
    /**интерфейс широкой фазы */
    btBroadphaseInterface broadphase;
    /**Объект для проверки столкновений используя широкую и ближнюю фазы (аналог btCollisionWorld только с учётом физ. параметров модели)*/
    public btDynamicsWorld dynWorld;
    /**Нужен для прикрепления объектов друг к другу (хз как применять)*/
    btConstraintSolver solver;
    
    DebugDrawer debugDrawer;
    
	public static Contacter contacter;

    
    /**Класс наделяет наш мир реальными физическими свойствами 
     * (+ реализацией обработки столкновений объектов в рамках реального физ. мира)
     * PS. но только мир решает для каких объектов применять симуляцию физики мира. а для каких нет 
     * @param gravity - гравитация*/
	public Physics(Vector3 gravity) {
        Bullet.init();
        collisionConf = new btDefaultCollisionConfiguration();
        collisionDispatcher = new btCollisionDispatcher(collisionConf);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();
        dynWorld = new btDiscreteDynamicsWorld(collisionDispatcher, broadphase, solver, collisionConf);
        dynWorld.setGravity(gravity);

//       debugDrawer = new DebugDrawer();
//		 debugDrawer.setDebugMode(DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
//		 dynWorld.setDebugDrawer(debugDrawer);
		
	}
	
	public static void init(){
        if (contacter == null) contacter = new Contacter();
	}
	
	public void debugDraw(PerspectiveCamera cam) {
		debugDrawer.begin(cam);
		dynWorld.debugDrawWorld();
//		
		/*for (DynamicEntity ent : DynamicEntity.list) {
			debugDrawDynamicEntity(ent);
		}*/
		debugDrawer.end();
	}

    /**Создает ограничивающую фигуру для динамичного объекта
     * @param model - 3D модель
     * @param optimize - флаг определяющий необходимость применения оптимизации (упрощения)
     * @param numMesh - по номеру какого меша модели будет создаваться ограничивающая фигура*/
    public static btConvexHullShape createConvexHullShape (final Model model, boolean optimize, int numMesh) {
    	final Mesh mesh = model.meshes.get(numMesh);
    	final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
        if (!optimize) return shape;
        // оптимизация формы
        final btShapeHull hull = new btShapeHull(shape);
        hull.buildHull(shape.getMargin());
        
        final btConvexHullShape result = new btConvexHullShape(hull);
        // удаление вспомогательных форм
        shape.dispose();
        hull.dispose();
        return result;
    }
    
    public static Array<btConvexHullShape> createConvexHullShapeAray (final Model model, boolean optimize) 
    {
    	Array<btConvexHullShape> result = new Array<btConvexHullShape>();
    	for (int i=0;i<model.nodes.size;i++) {result.add(createConvexHullShape(model, optimize,i));}
     	return result;
    }

	@Override
	public void dispose() {
		dynWorld.dispose();
		solver.dispose();
		broadphase.dispose();
		collisionDispatcher.dispose();
		collisionConf.dispose();
		if (debugDrawer != null) debugDrawer.dispose();
	}

	@Override
	public void addEntity(Entity ent) {
		if (ent instanceof Entity3D)
		{
		 synchronized (dynWorld) {
			Entity3D tempEnt = (Entity3D) ent;
			dynWorld.addRigidBody(((Model3DPhysics) tempEnt.model).instance.body);
		}
		}
	}

	@Override
	public void removeEntity(Entity ent) {
		if (ent instanceof Entity3D)
		{
	     synchronized (dynWorld) {
			Entity3D tempEnt = (Entity3D) ent;
			dynWorld.removeRigidBody(((Model3DPhysics) tempEnt.model).instance.body);
		 }
		}
	}
}

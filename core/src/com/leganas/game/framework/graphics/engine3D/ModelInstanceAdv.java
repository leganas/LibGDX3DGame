package com.leganas.game.framework.graphics.engine3D;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**Класс расширяющий базовый ModelInstance и 
 * описывающий как саму модель, и её положение, 
 * так и физические свойства, ограничивающие фигуры, вес и.т.д.
 * А также содержит набор конструкторов упрощающих создание модели как объекта физического тела*/
public class ModelInstanceAdv extends ModelInstance implements Disposable{
	public interface TransformListener {
		/**Метод вызывается при преобразовании модели*/
		public void transformEvent(Matrix4 transform);
	}
	/**Объект для определения столкновений в ручном режиме
	 * инициализируется по средством создания коробки вокруг модели*/
	public final btCollisionObject collisionObject;
	/**Смещение тела (хуй знает что такое по умолчанию типа нет смещения)*/
	public Vector3 bodyOffset = new Vector3();
	/**Описание модели как твердого тела, используем в место btCollisionObject*/
    public final btRigidBody body;
    /**Слушатель для слежения за трансформациями модели*/
    public final MotionStateAdv motionState; 
    /**центр модельки*/ 
    public final Vector3 center = new Vector3();
    /**размер модельки*/
    public final Vector3 dimens = new Vector3();
    /**Радиус ограничивающего модель шара*/
    public final float radius;
    /** ограничительный прямоугольник */
    private final BoundingBox bound = new  BoundingBox(); 
    /**Сохранять ссылку на эту ёбань пока буду , чтобы ошибки о удалении не лезли*/
    btRigidBody.btRigidBodyConstructionInfo bodyInfo;

    /**Назначение слушателя за преобразованиями модели*/
    public void setTransformlistener(TransformListener transformListener){
    	motionState.setTransformlistener(transformListener);
    }
    public ModelInstanceAdv(Model model,String node ,btRigidBody.btRigidBodyConstructionInfo bodyInfo) {
        super(model,node);
        calculateBoundingBox(bound); // считает и записывает ограничительный прямоугольник для текущей модели
        bound.getCenter(center); // вычисляем центр
        bound.getDimensions(dimens); // вычисляет размеры ограничивабщего прямоугольника
        radius = dimens.len()/2f; // радиус это половина размера т.к. мы в центре
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(new btBoxShape(dimens));
        body = new btRigidBody(bodyInfo);
        this.bodyInfo = bodyInfo;
        motionState = new MotionStateAdv();
        motionState.transform = transform;
        body.setMotionState(motionState);
    }
    
	public ModelInstanceAdv(Model model,btRigidBody.btRigidBodyConstructionInfo bodyInfo) {
        super(model);
        calculateBoundingBox(bound); // считает и записывает ограничительный прямоугольник для текущей модели
        bound.getCenter(center); // вычисляем центр
        bound.getDimensions(dimens); // вычисляет размеры ограничивабщего прямоугольника
        radius = dimens.len()/2f; // радиус это половина размера т.к. мы в центре
        collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(new btBoxShape(dimens));
//        bodyInfo.setCollisionShape(new btBoxShape(dimens));
        body = new btRigidBody(bodyInfo);
        this.bodyInfo = bodyInfo;
        motionState = new MotionStateAdv();
        motionState.transform = transform;
        body.setMotionState(motionState);
    }
    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
        bodyInfo.dispose();
        if (collisionObject != null) collisionObject.dispose();
    }
    
    public static class Constructor implements Disposable{
       	/**Модель 3D*/
        public final Model model;
        /**Имя*/
        public String node;
        /**ограничивающая фигура*/
        public btCollisionShape collShape;
        /**Комплекс параметров твердого тела*/
        public final btRigidBody.btRigidBodyConstructionInfo bodyInfo;
        /**Инерция (расчитывается если задана масса > 0)*/
        private static Vector3 inertia = new Vector3();
        /**Класс конструирования свойств моделей
         * @param model - Model3D
         * @param node - имя фигуры
         * @param collShape - ограничивающая фигура
         * @param mass - масса*/
        public Constructor(Model model, String node, btCollisionShape collShape, float mass){
            this.model = model;
            this.node = node;
            this.collShape = collShape;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
        }
        /**Класс конструирования свойств моделей
         * @param model - Model3D
         * @param node - имя фигуры
         * @param collShape - ограничивающая фигура
         * @param mass - масса*/
        public Constructor(Model model, btConvexHullShape collShape, float mass) {
            this.model = model;
            this.collShape = collShape;
            this.node = null;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
		}
        /**Класс конструирования свойств моделей
         * @param model - Model3D
         * @param node - имя фигуры
         * @param collShape - массив ограничивающих фигур 
         * @param mass - масса*/
        public Constructor(Model model, Array<btConvexHullShape> collShape, float mass) {
            this.model = model;
            for (btConvexHullShape tree : collShape) {
                this.collShape = tree;
                this.node = null;
                if(mass > 0f){
                    tree.calculateLocalInertia(mass, inertia);
                }else{inertia.set(0f, 0f, 0f);}
            }
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, this.collShape, inertia);
		}
        /**Класс конструирования свойств моделей
         * @param model - Model3D
         * @param node - имя фигуры
         * @param collShape - ограничивающая фигура
         * @param mass - масса*/
		public Constructor(Model model, btCollisionShape collShape, float mass) {
            this.model = model;
            this.collShape = collShape;
            this.node = null;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
		}
        /**Класс конструирования свойств моделей
         * @param model - Model3D
         * @param node - имя фигуры
         * @param collShape - ограничивающая фигура
         * @param mass - масса*/
		public Constructor(Model model) {
            this.model = model;
            this.bodyInfo = null;
            this.node = null;
		}
		/**Создать описание модели*/
        public ModelInstanceAdv create(){
        	ModelInstanceAdv result;  
        	if (this.node != null) {result = new ModelInstanceAdv(model,node, bodyInfo); } else {result = new ModelInstanceAdv(model, bodyInfo);}
//        	bodyInfo.dispose();
        	return result;
        }
        @Override
        public void dispose() {
            collShape.dispose();
            if (bodyInfo != null) bodyInfo.dispose();
        }
    }
    /**Механизм информирования о преобразовании объекта, который избавляет нас от необходимости перебирать все объекты. 
     * Для этого служит класс с методами обратного вызова btMotionState. */
    class MotionStateAdv extends btMotionState{
        Matrix4 transform;
        TransformListener transformlistener;
		public void setTransformlistener(TransformListener transformlistener) {
			this.transformlistener = transformlistener;
		}
		/**Вызывается системой эмуляции физ. мира при вызова метода  physics.dynWorld.stepSimulation
		 * служит для сохранения состояния мира, 
		 * ну или реакции на его изменение*/
		@Override
        public void getWorldTransform(Matrix4 WorldTrans){
            WorldTrans.set(transform); // мир изменился запишем это в transform            
//    		Gdx.app.log("LGame"," transform");
        }
		/**Вызывается системой эмуляции физ. мира после getWorldTransform
		 * служит для применения изменений к модели
		 * ну и для реакции на её изменение*/
        @Override
        public void setWorldTransform(Matrix4 WorldTrans){
            transform.set(WorldTrans); // применим изменения мира к нам
 //   		Gdx.app.log("LGame"," transform" + transform);
            if (transformlistener!= null) transformlistener.transformEvent(transform); // отправим нашу актуальную трансформацию слушателю
        }
    }
}

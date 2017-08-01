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

/**����� ����������� ������� ModelInstance � 
 * ����������� ��� ���� ������, � � ���������, 
 * ��� � ���������� ��������, �������������� ������, ��� �.�.�.
 * � ����� �������� ����� ������������� ���������� �������� ������ ��� ������� ����������� ����*/
public class ModelInstanceAdv extends ModelInstance implements Disposable{
	public interface TransformListener {
		/**����� ���������� ��� �������������� ������*/
		public void transformEvent(Matrix4 transform);
	}
	/**������ ��� ����������� ������������ � ������ ������
	 * ���������������� �� ��������� �������� ������� ������ ������*/
	public final btCollisionObject collisionObject;
	/**�������� ���� (��� ����� ��� ����� �� ��������� ���� ��� ��������)*/
	public Vector3 bodyOffset = new Vector3();
	/**�������� ������ ��� �������� ����, ���������� � ����� btCollisionObject*/
    public final btRigidBody body;
    /**��������� ��� �������� �� ��������������� ������*/
    public final MotionStateAdv motionState; 
    /**����� ��������*/ 
    public final Vector3 center = new Vector3();
    /**������ ��������*/
    public final Vector3 dimens = new Vector3();
    /**������ ��������������� ������ ����*/
    public final float radius;
    /** ��������������� ������������� */
    private final BoundingBox bound = new  BoundingBox(); 
    /**��������� ������ �� ��� ����� ���� ���� , ����� ������ � �������� �� �����*/
    btRigidBody.btRigidBodyConstructionInfo bodyInfo;

    /**���������� ��������� �� ���������������� ������*/
    public void setTransformlistener(TransformListener transformListener){
    	motionState.setTransformlistener(transformListener);
    }
    public ModelInstanceAdv(Model model,String node ,btRigidBody.btRigidBodyConstructionInfo bodyInfo) {
        super(model,node);
        calculateBoundingBox(bound); // ������� � ���������� ��������������� ������������� ��� ������� ������
        bound.getCenter(center); // ��������� �����
        bound.getDimensions(dimens); // ��������� ������� ��������������� ��������������
        radius = dimens.len()/2f; // ������ ��� �������� ������� �.�. �� � ������
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
        calculateBoundingBox(bound); // ������� � ���������� ��������������� ������������� ��� ������� ������
        bound.getCenter(center); // ��������� �����
        bound.getDimensions(dimens); // ��������� ������� ��������������� ��������������
        radius = dimens.len()/2f; // ������ ��� �������� ������� �.�. �� � ������
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
       	/**������ 3D*/
        public final Model model;
        /**���*/
        public String node;
        /**�������������� ������*/
        public btCollisionShape collShape;
        /**�������� ���������� �������� ����*/
        public final btRigidBody.btRigidBodyConstructionInfo bodyInfo;
        /**������� (������������� ���� ������ ����� > 0)*/
        private static Vector3 inertia = new Vector3();
        /**����� ��������������� ������� �������
         * @param model - Model3D
         * @param node - ��� ������
         * @param collShape - �������������� ������
         * @param mass - �����*/
        public Constructor(Model model, String node, btCollisionShape collShape, float mass){
            this.model = model;
            this.node = node;
            this.collShape = collShape;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
        }
        /**����� ��������������� ������� �������
         * @param model - Model3D
         * @param node - ��� ������
         * @param collShape - �������������� ������
         * @param mass - �����*/
        public Constructor(Model model, btConvexHullShape collShape, float mass) {
            this.model = model;
            this.collShape = collShape;
            this.node = null;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
		}
        /**����� ��������������� ������� �������
         * @param model - Model3D
         * @param node - ��� ������
         * @param collShape - ������ �������������� ����� 
         * @param mass - �����*/
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
        /**����� ��������������� ������� �������
         * @param model - Model3D
         * @param node - ��� ������
         * @param collShape - �������������� ������
         * @param mass - �����*/
		public Constructor(Model model, btCollisionShape collShape, float mass) {
            this.model = model;
            this.collShape = collShape;
            this.node = null;
            if(mass > 0f){
                collShape.calculateLocalInertia(mass, inertia);
            }else{inertia.set(0f, 0f, 0f);}
            this.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, collShape, inertia);
		}
        /**����� ��������������� ������� �������
         * @param model - Model3D
         * @param node - ��� ������
         * @param collShape - �������������� ������
         * @param mass - �����*/
		public Constructor(Model model) {
            this.model = model;
            this.bodyInfo = null;
            this.node = null;
		}
		/**������� �������� ������*/
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
    /**�������� �������������� � �������������� �������, ������� ��������� ��� �� ������������� ���������� ��� �������. 
     * ��� ����� ������ ����� � �������� ��������� ������ btMotionState. */
    class MotionStateAdv extends btMotionState{
        Matrix4 transform;
        TransformListener transformlistener;
		public void setTransformlistener(TransformListener transformlistener) {
			this.transformlistener = transformlistener;
		}
		/**���������� �������� �������� ���. ���� ��� ������ ������  physics.dynWorld.stepSimulation
		 * ������ ��� ���������� ��������� ����, 
		 * �� ��� ������� �� ��� ���������*/
		@Override
        public void getWorldTransform(Matrix4 WorldTrans){
            WorldTrans.set(transform); // ��� ��������� ������� ��� � transform            
//    		Gdx.app.log("LGame"," transform");
        }
		/**���������� �������� �������� ���. ���� ����� getWorldTransform
		 * ������ ��� ���������� ��������� � ������
		 * �� � ��� ������� �� � ���������*/
        @Override
        public void setWorldTransform(Matrix4 WorldTrans){
            transform.set(WorldTrans); // �������� ��������� ���� � ���
 //   		Gdx.app.log("LGame"," transform" + transform);
            if (transformlistener!= null) transformlistener.transformEvent(transform); // �������� ���� ���������� ������������� ���������
        }
    }
}

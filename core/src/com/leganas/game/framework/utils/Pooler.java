package com.leganas.game.framework.utils;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entries;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.world.GWorld;


public class Pooler {
	private static Vector3Pool v3Pool;
	private static Matrix4Pool mtxPool;
	


	public static void init() {
		v3Pool = new Vector3Pool();
		mtxPool = new Matrix4Pool();
	}



	public static class Vector3Pool extends CountingPool<Vector3> {
		@Override
		protected Vector3 newObject() {
			return new Vector3();
		}
	}
	public static Vector3 v3() {
		return v3Pool.obtain();
	}

	public static void free(Vector3 v3) {
		v3Pool.free(v3);
	}

	public static void free(Vector3 ...v3) {
		for (int i = 0; i < v3.length; i++) {
			v3Pool.free(v3[i]);
		}
	}



	public static class Matrix4Pool extends CountingPool<Matrix4> {
		@Override
		protected Matrix4 newObject() {
			return new Matrix4();
		}
	}

	public static Matrix4 mtx() {
		return mtxPool.obtain();
	}

	public static void free(Matrix4 mtx) {
		mtxPool.free(mtx);
	}

	public static void free(Matrix4 ...mtx) {
		for (int i = 0; i < mtx.length; i++) {
			mtxPool.free(mtx[i]);
		}
	}
	
}
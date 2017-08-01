package com.leganas.game.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json.Serializable;

/** Get the approximate size of an object through serialization */
public class ObjectSize {
	public static long getObjectSize(Serializable ser) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ser);
			oos.close();
		} catch (Exception e) {
			throw new GdxRuntimeException(e.toString());
		}
		return baos.size();
	}
}

package com.leganas.game.framework.graphics.effects;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.leganas.game.Setting;
import com.leganas.game.framework.utils.Pooler;


public class BulletHit {
	public static Array<BulletHit> list = new Array<BulletHit>();
	private Vector3 location;
	private ParticleEffect pfx;

	private static Matrix4 mtx = new Matrix4();

	public BulletHit(Vector3 loc) {
		location = Pooler.v3().set(loc);
		if (Setting.isClient()) {
			pfx = Particles.inst.obtainBulletHit();
			//mtx.setToScaling(0.5f, 0.5f, 0.5f);
			mtx.setToTranslation(loc);
			pfx.setTransform(mtx);
			Particles.inst.system.add(pfx);
			RegularEmitter emitter = (RegularEmitter) pfx.getControllers().first().emitter;
			emitter.setEmissionMode(RegularEmitter.EmissionMode.EnabledUntilCycleEnd);
			pfx.start();
		}
		list.add(this);
		if (Setting.isServer()) {
			applyEffects();
		}
	}

	private void applyEffects() {
/*		float minDist = 1f;
		for (DynamicEntity ent : DynamicEntity.list) {
			float distSqr = ent.getPosition().dst2(location);
			if (distSqr <= minDist * minDist) {
				float strength = 10.0f;
				tmp.set(ent.getPosition()).sub(location).nor().scl(strength);
				tmp.y = 0f;
				ent.adjustVelocity(tmp);
				// TODO don't damage players yet, no respawn or death code exists
				if (!ent.isPlayer()) {
					ent.applyDamage(20f);
				}
			}
		}*/
	}

	private static Vector3 tmp = new Vector3();

	private void destroy() {
		Pooler.free(location);
		if (Setting.isClient()) {
			Particles.inst.freeBulletHit(pfx);
		}
		list.removeValue(this, true);
	}

	public static void updateAll(float timeStep) {
		for (BulletHit bulletHit : list) {
			bulletHit.update();
		}
		//System.out.println("bhit list: " + list.size);
	}

	public void update() {
		if (Setting.isClient()) {
			RegularEmitter emitter = (RegularEmitter) pfx.getControllers().first().emitter;
			if (emitter.isComplete()) {
				destroy();
			}
		}
	}
}

package com.leganas.game.framework.graphics.effects;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.utils.Pool;
import com.leganas.game.MySyper2DGame;
import com.leganas.game.world.render.WorldRender;


/**Класс для работы с эффектами взрывов и огня*/
public class Particles {
	public static Particles inst;
	public ParticleSystem system;
	private PFXPool bulletHitPool;
	private PFXPool blueExplosionPool;

	/**Класс для работы с эффектами взрывов и огня*/
	public Particles(WorldRender render) {
		inst = this;
		system = ParticleSystem.get();
		PointSpriteParticleBatch psBatch = new PointSpriteParticleBatch();
		psBatch.setCamera(render.getCamera());
		system.add(psBatch);

		loadParticleEffects(system);

		ParticleEffect bulletHit = MySyper2DGame.inst.manager.get("particle/bullet-hit.pfx");
		ParticleEffect blueExplosion = MySyper2DGame.inst.manager.get("particle/blue-explosion.pfx");
		bulletHitPool = new PFXPool(bulletHit);
		blueExplosionPool = new PFXPool(blueExplosion);
	}
	
	public static void loadParticleEffects(ParticleSystem particleSystem) {
		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
		ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
		MySyper2DGame.inst.manager.setLoader(ParticleEffect.class, loader);
		MySyper2DGame.inst.manager.load("particle/bullet-hit.pfx", ParticleEffect.class, loadParam);
		MySyper2DGame.inst.manager.load("particle/blue-explosion.pfx", ParticleEffect.class, loadParam);
		MySyper2DGame.inst.manager.finishLoading();
	}

	public ParticleEffect obtainBulletHit() {
		//System.out.println("free bullet-hit count: " + bulletHitPool.getFree());
		return bulletHitPool.obtain();
	}

	public void freeBulletHit(ParticleEffect pfx) {
		system.remove(pfx);
		bulletHitPool.free(pfx);
	}

	public ParticleEffect obtainBlueExplosion() {
		//System.out.println("free bullet-hit count: " + blueExplosionPool.getFree());
		return blueExplosionPool.obtain();
	}

	public void freeBlueExplosion(ParticleEffect pfx) {
		system.remove(pfx);
		blueExplosionPool.free(pfx);
	}


	// TODO initial capacity in constructor
	private static class PFXPool extends Pool<ParticleEffect> {
		private ParticleEffect sourceEffect;

		public PFXPool(ParticleEffect sourceEffect) {
			this.sourceEffect = sourceEffect;
		}

		@Override
		public void free(ParticleEffect pfx) {
			pfx.reset();
			super.free(pfx);
		}

		@Override
		protected ParticleEffect newObject() {
			ParticleEffect pfx = sourceEffect.copy();
			pfx.init();
			return pfx;
		}
	}
}

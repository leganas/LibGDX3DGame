package com.leganas.game.framework.graphics;



public abstract class AbstractModel<T>{
	public T instance;
    protected boolean transformFlag;
	public enum model3DStatus {
		init,
		ready,
		outOfRange,
	}
	
	public model3DStatus status = model3DStatus.init; // статус графической модельки сущности
    
	public interface ModelListener {
		public void modelMessage(int modelID, Object msg);
	}
	
	public ModelListener listener;
	

	public void setListener(ModelListener listener) {
		this.listener = listener;
	}

	public T getInstance() {
		return instance;
	}

	public void setInstance(T instance) {
		this.instance = instance;
	}
  
    public boolean isTransformFlag() {
		return transformFlag;
	}

	public void setTransformFlag(boolean transformFlag) {
		this.transformFlag = transformFlag;
	}
}

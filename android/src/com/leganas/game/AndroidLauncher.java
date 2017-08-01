package com.leganas.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;*/
import com.leganas.game.framework.interfaces.CallbackMain;

public class AndroidLauncher extends AndroidApplication  implements CallbackMain{
//	protected AdView adView;
	
	protected Handler handler = new Handler()
	  {
	    @Override
	    public void handleMessage(Message msg) {
/*	      if(msg.what ==0)
	        adView.setVisibility(View.GONE);
	      if(msg.what==1){
	        adView.setVisibility(View.VISIBLE);
	        AdRequest adRequest = new AdRequest();
	        adView.loadAd(adRequest);
	      }*/
	    }
	  }; 
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Firebase.setAndroidContext(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(new SomeCoolGame(), config);
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;

		RelativeLayout main_layout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		View gameView = initializeForView(new MySyper2DGame(this), config);
	    main_layout.addView(gameView);
	    
/*	    adView = new AdView(this, AdSize.BANNER, "ca-app-pub-7916609733931280/5123195450");
	    AdRequest adRequest = new AdRequest();
	    adRequest.addTestDevice("A4A477807530639FCAF42A7B78EF6B8A");
	    adView.setVisibility(View.VISIBLE);
	    adView.loadAd(adRequest);*/
	    
/*	    RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
	    	RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	    main_layout.addView(adView, adParams);*/
//	    adView.refreshDrawableState();
	    
	    setContentView(main_layout);
	}

	@Override
	public void showAdMob(boolean show) {
		// Показываем рекламу
		handler.sendEmptyMessage(show ? 1 : 0);
		Gdx.app.log("LGame", "Admod desktop show - " + show);
	}
}
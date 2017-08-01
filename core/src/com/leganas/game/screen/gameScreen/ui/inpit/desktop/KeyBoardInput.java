package com.leganas.game.screen.gameScreen.ui.inpit.desktop;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage;
import com.leganas.game.screen.gameScreen.ui.inpit.UserInput;

public class KeyBoardInput implements InputProcessor{
	/**Будем передавать контроллеру по средством слушателя события ввода*/
	UserInput listener;
    public void setListener(UserInput listener) {
		this.listener = listener;
	}

	@Override
	public boolean keyDown(int keycode) {
	   	 switch(keycode){
    	 case Keys.UP:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("forward",true));
              break;
          case Keys.DOWN:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("back",true));
              break;
          case Keys.RIGHT:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("rotate_right",true));
              break;
          case Keys.LEFT:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("rotate_left",true));
              break;
          case Keys.SPACE:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("jump",true));
             break;
          case Keys.ENTER:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("fire",true));
           break;
          case Keys.A:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("left",true));
           break;
          case Keys.D:
     		 listener.inputEvent(new ClientMessage.ClientInputMsg("right",true));
           break;
          }
    	 return true;
	}

	@Override
	public boolean keyUp(int keycode) {
	   	 switch(keycode){
   	     case Keys.UP:
   		     listener.inputEvent(new ClientMessage.ClientInputMsg("forward",false));
             break;
         case Keys.DOWN:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("back",false));
             break;
         case Keys.RIGHT:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("rotate_right",false));
             break;
         case Keys.LEFT:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("rotate_left",false));
             break;
         case Keys.SPACE:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("jump",false));
             break;
         case Keys.ENTER:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("fire",false));
    		 break;
         case Keys.A:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("left",false));
    		 break;
         case Keys.D:
    		 listener.inputEvent(new ClientMessage.ClientInputMsg("right",false));
    		 break;
         }
   	 return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

}

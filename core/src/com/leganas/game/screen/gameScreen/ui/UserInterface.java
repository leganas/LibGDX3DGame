package com.leganas.game.screen.gameScreen.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.leganas.game.framework.interfaces.Event;
import com.leganas.game.network.packeges.clientTOserver.ClientMessage.ClientInputMsg;
import com.leganas.game.screen.gameScreen.ui.inpit.UserInput;
import com.leganas.game.screen.gameScreen.ui.inpit.desktop.KeyBoardInput;
import com.leganas.game.screen.gameScreen.ui.inpit.mobile.MobileInputItems;
import com.leganas.game.screen.gameScreen.ui.textinfo.TextInfoStage;

/**����� �������������� ��� ��������� �� ������� ��������� �������� ���������� ����������������� �����*/
public class UserInterface implements UserInput, Disposable{
	UserInterfaceListener listener;
	
	public void setListener(UserInterfaceListener listener) {
		this.listener = listener;
	}

	public interface UserInterfaceListener {
		public void userInterfaceEvent(Event msg);
	}
	
	/**���������� �������� ����������������� ���������� ����������� ���������*/
	MobileInputItems mobileInputItems;
	/**���� � ���������� */
	KeyBoardInput keyBoardInput; 
	/**��������� ��������� ����������*/
	TextInfoStage textInfoStage;
	
	/**����� �������������� ��� ��������� �� ������� ��������� �������� ���������� ����������������� �����*/
	public UserInterface() {
//		if (Setting.isMobile()) {
			mobileInputItems = new MobileInputItems();
			mobileInputItems.setListener(this);
//	}

			keyBoardInput = new KeyBoardInput();
			keyBoardInput.setListener(this);
			
			textInfoStage = new TextInfoStage();
	}

	public void draw(){
		mobileInputItems.draw();
		textInfoStage.draw();
	}
	
	/**���������� ��� ��������� inputProcessor ��� ������������� 
	 * ����������������� ����� �� ������� ���������*/
	public Array<InputProcessor> getInputProcessors(){
		Array<InputProcessor> processors = new Array<InputProcessor>();;
		if (mobileInputItems != null) processors.add(mobileInputItems);
		processors.add(keyBoardInput);
		return processors;
	}

	@Override
	public void inputEvent(ClientInputMsg msg) {
		if (listener != null) listener.userInterfaceEvent(msg);
	}

	@Override
	public void dispose() {
		// TODO ������������� ��������� �������� ������
		mobileInputItems.dispose();
	}

}

package com.leganas.game;

import com.badlogic.gdx.Application;

public class Setting {
    public enum ServerType {
    	Local,
    	OnLine,
    	Off
    }
    public enum ClientState {
    	Off,
    	Init,
    	Pause,
    	Ready    	
    }
    public static boolean touchPadFlag = false; 
	private static ServerType Server = ServerType.OnLine;
	private static boolean Client = true;
	private static String clientName = "Player";
	private static String clientHost = "localhost";
	private static ClientState clientState = ClientState.Off;
	public static Application.ApplicationType platform;

	public static boolean isLocalServer() {
		return Server == ServerType.Local;
	}
	public static boolean isOnlineServer() {
		return Server == ServerType.OnLine;
	}
	public static boolean isServer() {
		if (Server == ServerType.Off) return false;
		return isLocalServer() || isOnlineServer();
	}
	
	public static boolean isClient() {
		return Client;
	}
	
	public static boolean isClientReady(){
		if (isClient() && clientState == ClientState.Ready) return true; else return false;
	}
	
	
	public static ClientState getClientState() {
		return clientState;
	}
	public static void setClientState(ClientState clientState) {
		Setting.clientState = clientState;
	}
	public static String getClientHost() {
		return clientHost;
	}
	public static void setClientHost(String cHost) {
		clientHost = cHost;
	}
	public static void setServer(ServerType server) {
		Server = server;
	}
	public static void setClient(boolean client) {
		Client = client;
	}
	
	
	public static String getClientName() {
		return clientName;
	}
	public static void setClientName(String clientName) {
		Setting.clientName = clientName;
	}
	public static boolean isMobile() {
		return isAndroid() || isIOS();
	}

	public static boolean isAndroid() {
		return platform == Application.ApplicationType.Android;
	}

	public static boolean isIOS() {
		return platform == Application.ApplicationType.iOS;
	}

	public static boolean isDesktop() {
		return platform == Application.ApplicationType.Desktop;
	}

	
}

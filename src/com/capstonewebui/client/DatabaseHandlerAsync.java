package com.capstonewebui.client;

import com.capstonewebui.shared.LocationObject;
import com.capstonewebui.shared.WorldObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatabaseHandlerAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getLocations(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getLocation(String locationName, AsyncCallback<LocationObject> callback)
			throws IllegalArgumentException;
	
	void storeLocation(LocationObject object, AsyncCallback<String> callback) 
			throws IllegalArgumentException;
	
	void storeWorld(WorldObject world,AsyncCallback<String> callback) 
			throws IllegalArgumentException;
	
	void getWorlds(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getWorld(String worldName, AsyncCallback<String> callback)
			throws IllegalArgumentException;
			
	void getWorldInfo(String worldName, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void deleteLocation(String locationName, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void deleteWorld(String worldName, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}

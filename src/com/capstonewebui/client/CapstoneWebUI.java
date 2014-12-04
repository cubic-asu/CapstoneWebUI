package com.capstonewebui.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CapstoneWebUI implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	// private final GreetingServiceAsync greetingService = GWT
	// .create(GreetingService.class);

	static final DatabaseHandlerAsync databaseService = GWT
			.create(DatabaseHandler.class);
	/**
	 * This is the entry point method.
	 */

	static MenuPanel menuPanel;
	static LoginPanel logInPanel;
	static LocationCreationForm locationCreationPanel;
	static ManageWorldsPanel worldManagerPanel;
	static WorldCreationForm worldCreationForm;

	public void onModuleLoad() {
		loadMapApi();
	}

	private void addToRoot() {
		// Login Panel
		logInPanel = new LoginPanel();

		// Menu Option
		menuPanel = new MenuPanel();

		locationCreationPanel = new LocationCreationForm();
		worldManagerPanel = new ManageWorldsPanel();
		worldCreationForm = new WorldCreationForm();

		RootPanel.get("contentPanel").add(worldCreationForm);

		RootPanel.get("contentPanel").add(logInPanel);
		RootPanel.get("contentPanel").add(menuPanel);
		// logInPanel.setVisible(false);
		RootPanel.get("contentPanel").add(locationCreationPanel);

		// locationCreationPanel.setVisible(true);
		// worldBuilderPanel.setVisible(true);
		RootPanel.get("contentPanel").add(worldManagerPanel);

	}

	private void loadMapApi() {
		boolean sensor = true;

		// load all the libs for use in the maps
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadLibrary>();
		loadLibraries.add(LoadLibrary.ADSENSE);
		loadLibraries.add(LoadLibrary.DRAWING);
		loadLibraries.add(LoadLibrary.GEOMETRY);
		loadLibraries.add(LoadLibrary.PANORAMIO);
		loadLibraries.add(LoadLibrary.PLACES);
		loadLibraries.add(LoadLibrary.WEATHER);
		loadLibraries.add(LoadLibrary.VISUALIZATION);

		Runnable onLoad = new Runnable() {
			@Override
			public void run() {
				addToRoot();
			}
		};

		LoadApi.go(onLoad, loadLibraries, sensor);
	}

}
package com.capstonewebui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;

public class ManageWorldsPanel extends AbsolutePanel {

	private Grid mGrid;
	private ListBox worldsList;

	public ManageWorldsPanel() {
		mGrid = new Grid(3, 3);
		mGrid.setStyleName("centered");
		this.setSize("800px", "800px");
		this.setVisible(false);

		worldsList = new ListBox();
		worldsList.setSize("300px", "192px");
		mGrid.setWidget(0, 0, worldsList);

		addControls();

		this.add(mGrid);

	}

	public void addWorldsToList(String worldString) {
		worldsList.clear();
		if (worldString.compareTo("") != 0) // compare
		{
			String[] world = worldString.split(",");
			for (int i = 0; i < world.length; i++) {
				worldsList.addItem(world[i]);
			}
			worldsList.setVisibleItemCount(10);
		}
	}

	private void clearWorldList() {
		worldsList.clear();
	}

	private void addControls() {

		Button editWorld = new Button("Edit");
		Button deleteWorld = new Button("Delete");
		Button initializeWorld = new Button("Initialize");
		Button back = new Button("<-Back");

		editWorld.setStyleName("worldManagerButton");
		deleteWorld.setStyleName("worldManagerButton");
		initializeWorld.setStyleName("worldManagerButton");
		back.setStyleName("worldManagerButton");

		AbsolutePanel buttonContainer = new AbsolutePanel();
		buttonContainer.setWidth("300px");

		buttonContainer.add(initializeWorld);
		buttonContainer.add(deleteWorld);
		buttonContainer.add(editWorld);
		buttonContainer.add(back);
		mGrid.setWidget(1, 0, buttonContainer);

		// adding the handlers to each button
		deleteWorld.addClickHandler(new DeleteListHandler());
		editWorld.addClickHandler(new EditListHandler());
		initializeWorld.addClickHandler(new InitializeWorldHandler());
		back.addClickHandler(new BackButtonHandler());
	}

	private void getWorlds(String[] worlds) {
		for (int i = 0; i < worlds.length; i++) {
			worldsList.addItem(worlds[i]);
		}
	}

	class BackButtonHandler implements ClickHandler, KeyUpHandler {
		public void onClick(ClickEvent event) {
			{
				CapstoneWebUI.worldManagerPanel.setVisible(false);
				CapstoneWebUI.menuPanel.setVisible(true);
			}
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

			}
		}
	}

	class DeleteListHandler implements ClickHandler, KeyUpHandler {

		public void onClick(ClickEvent event) {

			{
				if (worldsList.getItemCount() != 0) {
					CapstoneWebUI.databaseService.deleteWorld(worldsList
							.getItemText(worldsList.getSelectedIndex()),
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									System.out.println(caught);
								}

								public void onSuccess(String result) {
									deleteWorld();
								}
							});
				}
			}

		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

			}
		}

		private void deleteWorld() {
			worldsList.removeItem(worldsList.getSelectedIndex());
		}
	}

	class EditListHandler implements ClickHandler, KeyUpHandler {

		public void onClick(ClickEvent event) {

			if (worldsList.getSelectedIndex() >= 0) {

				{
					CapstoneWebUI.databaseService.getWorldInfo(worldsList
							.getItemText(worldsList.getSelectedIndex()),
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									System.out.println(caught);
								}

								public void onSuccess(String result) {
									System.out
											.println("The info for the selected world is: "
													+ result);
									CapstoneWebUI.worldCreationForm
											.loadWorldInformation(result);
								}

							});
				}

				CapstoneWebUI.databaseService.getWorld(
						worldsList.getItemText(worldsList.getSelectedIndex()),
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(String result) {
								CapstoneWebUI.worldCreationForm
										.clearAllMarkers();
								CapstoneWebUI.worldCreationForm
										.loadLocations(result);
								CapstoneWebUI.worldManagerPanel
										.setVisible(false);
								CapstoneWebUI.worldCreationForm
										.setVisible(true);
								CapstoneWebUI.worldCreationForm.resizeMap();
							}
						});
			}
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			}
		}

		private void editWorld() {
			System.out.println(worldsList.getValue(worldsList
					.getSelectedIndex()));
		}
	}

	class InitializeWorldHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (worldsList.getSelectedIndex() >= 0) {
				System.out.println("running world "
						+ worldsList.getValue(worldsList.getSelectedIndex()));
				// Brian's code goes here
			}

		}

	}

}

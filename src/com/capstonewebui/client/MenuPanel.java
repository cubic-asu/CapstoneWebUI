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

public class MenuPanel extends AbsolutePanel {

	public MenuPanel() {
		Grid menuGrid = new Grid(5, 1);
		menuGrid.setStyleName("centered");
		Button buildWorldButton = new Button("Create World");

		Button manageWorldsButton = new Button("Manage Worlds");
		Button interactButton = new Button("Interact with Players");
		Button tutorialButton = new Button("Tutorial");

		buildWorldButton.setStyleName("menuButton");
		manageWorldsButton.setStyleName("menuButton");
		interactButton.setStyleName("menuButton");
		tutorialButton.setStyleName("menuButton");

		menuGrid.setWidget(0, 0, buildWorldButton);
		menuGrid.setWidget(1, 0, manageWorldsButton);
		menuGrid.setWidget(2, 0, interactButton);
		menuGrid.setWidget(3, 0, tutorialButton);

		NewWorldListener handler = new NewWorldListener();
		buildWorldButton.addClickHandler(handler);
		manageWorldsButton.addClickHandler(new MenuManagerHandler());
		tutorialButton.addClickHandler(new TutorialHandler());

		this.add(menuGrid);
		this.setVisible(false);
	}

	class TutorialHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

		}

	}

	class NewWorldListener implements ClickHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			// sendNameToServer();
			generateNewWorld();
		}

		/**
		 * Fired when the user types in the nameField.
		 */

		private void generateNewWorld() {
			CapstoneWebUI.menuPanel.setVisible(false);
			CapstoneWebUI.worldCreationForm.setVisible(true);
			CapstoneWebUI.worldCreationForm.generateNewWorld();
			// CapstoneWebUI.worldCreationForm.resizeMap();
		}

	}

	class MenuManagerHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			// sendNameToServer();
			loadWorldsManagerPanel();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				// sendNameToServer();
			}
		}

		private void loadWorldsManagerPanel() {
			CapstoneWebUI.menuPanel.setVisible(false);
			CapstoneWebUI.worldManagerPanel.setVisible(true);
			CapstoneWebUI.databaseService
					.getWorlds(new AsyncCallback<String>() {
						public void onFailure(Throwable caught) {
							System.out.println("failed");
							System.out.println(caught);
						}

						public void onSuccess(String result) {
							System.out.println(result);
							CapstoneWebUI.worldManagerPanel
									.addWorldsToList(result);
						}

					});
			CapstoneWebUI.menuPanel.setVisible(false);
			CapstoneWebUI.worldManagerPanel.setVisible(true);
		}

	}
}
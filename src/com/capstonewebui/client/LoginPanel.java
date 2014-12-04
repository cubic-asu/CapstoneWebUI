package com.capstonewebui.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class LoginPanel extends AbsolutePanel {

	private TextBox userNameTextBox;
	
	public LoginPanel() {
		userNameTextBox = new TextBox();
		PasswordTextBox passwordTextBox = new PasswordTextBox();
		Grid grid = new Grid(3, 2);
		
		
			//adding widgets to login Panel
		grid.setWidget(0, 0, new Label("UserName"));
		grid.setWidget(0, 1, userNameTextBox);
		grid.setWidget(1, 1, passwordTextBox);
		grid.setWidget(1, 0, new Label("password"));
		
		
		AbsolutePanel buttonContainer = new AbsolutePanel();
		Button logInButton = new Button("Log In");
		Button forgotPasswordButton = new Button("Forgot Password");
		
		buttonContainer.setWidth("200px");
		buttonContainer.add(logInButton);
		buttonContainer.add(forgotPasswordButton);
		//grid.setWidget(2, 0, forgotPasswordButton);
		grid.setWidget(2, 1, buttonContainer);
		grid.setStyleName("centered");
		
		
		//inscript styling
		this.setSize("800px", "800px");
		passwordTextBox.setWidth("200px");
		userNameTextBox.setWidth("200px");
		
		this.add(grid);
		
		MyHandler handler = new MyHandler();
		logInButton.addClickHandler(handler);
	}
	
	class MyHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			sendNameToServer();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				//sendNameToServer();
			}
		}

		/**
		 * Send the name from the nameField to the server and wait for a response.
		 */
		
		
		private void sendNameToServer() {
			// First, we validate the input.
			CapstoneWebUI.logInPanel.setVisible(false);
			CapstoneWebUI.menuPanel.setVisible(true);
			}

		}

}

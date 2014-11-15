package com.capstonewebui.client;

import com.capstonewebui.shared.LocationObject;
import com.capstonewebui.shared.WorldObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;

public class WorldCreationForm extends AbsolutePanel {
	
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private HorizontalPanel namePanel = new HorizontalPanel();
	private HorizontalPanel descriptionPanel = new HorizontalPanel();
	private VerticalPanel leftFormPanel = new VerticalPanel();
	private VerticalPanel rightMapPanel = new VerticalPanel();
	private HorizontalPanel discardSavePanel = new HorizontalPanel();
	private FlexTable locationsFlexTable = new FlexTable();
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private HorizontalPanel addLocationPanel = new HorizontalPanel();

	
	private ArrayList<LocationObject> locationsArray;
	private TextBox nameTextBox = new TextBox();
	private TextArea descriptionTextBox = new TextArea();
	private Button addLocationButton = new Button("Add a Location");
	private Button discardButton = new Button("Discard");
	private Button saveButton = new Button("Save");
	private Button editDeleteButton;
	private Label nameLabel = new Label("Name:");
	private Label descriptionLabel = new Label("Description:");
	private ArrayList<String> locations = new ArrayList<String>();
	private MapWidget map;
	
	public WorldCreationForm() {
		
		locationsArray = new ArrayList<LocationObject>();
		this.setVisible(false);
		this.add(assemblePanels());
		
		this.addStyleName("worldCreationPanel");
		mainPanel.addStyleName("mainPanel");
		leftFormPanel.addStyleName("leftFormPanel");
		rightMapPanel.addStyleName("rightFormPanel");
		namePanel.addStyleName("namePanel");
		descriptionPanel.addStyleName("descriptionPanel");
		descriptionTextBox.addStyleName("descriptionTextArea");
		nameTextBox.addStyleName("nameTextBox");
		locationsFlexTable.addStyleName("locationsFlexTable");
		addLocationButton.addStyleName("addLocationButton");
		addLocationPanel.addStyleName("addLocationPanel");
		discardButton.addStyleName("discardButton");
		discardSavePanel.addStyleName("discardSavePanel");
		saveButton.addStyleName("saveButton");
		locationsFlexTable.getColumnFormatter().setWidth(0, "26px");
		locationsFlexTable.getColumnFormatter().setWidth(1, "125px");
		locationsFlexTable.getColumnFormatter().setWidth(2, "81px");
		locationsFlexTable.getRowFormatter().addStyleName(0, "locationListHeader");

		saveButton.addClickHandler(new SaveButtonHandler());
		discardButton.addClickHandler(new DiscardButtonHandler());
		addLocationButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				CapstoneWebUI.worldCreationForm.setVisible(false);
				CapstoneWebUI.locationCreationPanel.populateAvailableLocationLists(locationsArray);
				CapstoneWebUI.locationCreationPanel.setVisible(true);
			}
		});
		
		

	}
	
	
	private HorizontalPanel assemblePanels(){
		assembleFlexTable();
		
		namePanel.add(nameLabel);
		namePanel.add(nameTextBox);
		
		discardSavePanel.add(discardButton);
		discardSavePanel.add(saveButton);
		
		descriptionPanel.add(descriptionLabel);
		descriptionPanel.add(descriptionTextBox);
		
		addLocationPanel.add(addLocationButton);
		
		leftFormPanel.add(namePanel);
		leftFormPanel.add(descriptionPanel);
		leftFormPanel.add(addLocationPanel);
		leftFormPanel.add(locationsFlexTable);
		mainPanel.add(leftFormPanel);
		
		//rightMapPanel.add(Google map)
		
		Maps.loadMapsApi("", "2", false, new Runnable() {
		      public void run() {
		        mapPanel.add(buildMap());
		        CapstoneWebUI.locationCreationPanel.buildMapUI();
		        map.setVisible(false);
		        map.setVisible(true);
		        //buildMap().checkResizeAndCenter();
		      }
		});
		
		
		rightMapPanel.add(mapPanel);
		rightMapPanel.add(discardSavePanel);
		mainPanel.add(rightMapPanel);
		
		return mainPanel;
	}
	
	private void clearPanel()
	{
		locationsArray = null;
		nameTextBox.setText("");
		descriptionTextBox.setText("");
		
		for(int i = 0; i < locationsFlexTable.getRowCount() -1; i++)
		{
			locationsFlexTable.removeRow(1);
		}
	}
	
	private void assembleFlexTable(){
		locationsFlexTable.setText(0, 0, "#");
		locationsFlexTable.setText(0, 1, "Location");
		locationsFlexTable.setText(0, 2, "Options");
		locationsFlexTable.setText(0, 3, "");
	}
	
	public void addLocation(final LocationObject location)
	{
		locationsArray.add(location);
		final int row = locationsFlexTable.getRowCount();
		locationsFlexTable.setText(row, 0, location.getLocationName());
		locationsFlexTable.setText(row, 1, location.getLongitude() + "," + location.getLatitude());
		locationsFlexTable.setText(row, 2, "Edit/Delete");
		Button deleteButton = new Button("Delete");
		Button editButton = new Button("Edit");
		
		deleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeLocation(location.getLocationName());
			}
		});
		
		editButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				editLocation(location.getLocationName());
				
			}
		});
		

		LatLng locationLatLng = LatLng.newInstance(Double.parseDouble(location.getLongitude()),Double.parseDouble(location.getLongitude()));
		Marker newMarker = new Marker(locationLatLng);
		
		map.addOverlay(newMarker);
		
		//caption
    	map.getInfoWindow().open(locationLatLng,
    			new InfoWindowContent(location.getLocationName()));
		locationsFlexTable.setWidget(row, 2, editButton);
		locationsFlexTable.setWidget(row, 3, deleteButton);
		
		
	}
	
	private void removeLocation(String a)
	{
		
		int rowNumber = -1;
		
		
		for(int i = 1; i < locationsFlexTable.getRowCount(); i++)
		{
			if(a.compareTo(locationsFlexTable.getText(i, 0)) == 0)
			{
				rowNumber = i;
			}
		}
		
		
		if(rowNumber != -1 && rowNumber < locationsFlexTable.getRowCount())
		{
			locationsFlexTable.removeRow(rowNumber);
			locationsArray.remove(rowNumber-1);
		}
	}
	
	
	private void editLocation(String a)
	{
		int rowNumber = -1;
		for(int i = 1; i < locationsFlexTable.getRowCount(); i++)
		{
			if(a.compareTo(locationsFlexTable.getText(i, 0)) == 0)
			{
				rowNumber = i;
			}
		}
		
		if(rowNumber != -1 && rowNumber < locationsFlexTable.getRowCount())
		{
			CapstoneWebUI.locationCreationPanel.update(locationsArray.get(rowNumber-1), locationsArray);
			CapstoneWebUI.worldCreationForm.setVisible(false);
			CapstoneWebUI.locationCreationPanel.setVisible(true);
		}
		
	}
	
	private MapWidget buildMap() {
	    // Open a map centered on Cawker City, KS USA

	    map = new MapWidget();
	    map.setSize("380px", "380px");
	    // Add some controls for the zoom level
	    map.addControl(new LargeMapControl());
	    return map;
	  }
	
	class SaveButtonHandler implements ClickHandler
	{

		@Override
		public void onClick(ClickEvent event) {
			WorldObject mWorld = new WorldObject();
			mWorld.setWorldName(nameTextBox.getText());
			mWorld.setWorldDescription(descriptionTextBox.getText());
			
			System.out.println(locationsArray.size());

			
			for(int i = 0; i < locationsArray.size(); i++)
			{
				locationsArray.get(i).setWorld(nameTextBox.getText());
				CapstoneWebUI.databaseService.storeLocation(locationsArray.get(i), 
					new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						System.out.println(caught);
					}
					public void onSuccess(String result) {
						System.out.println("saved!");
					}
				});
			}
			
			
			CapstoneWebUI.databaseService.storeWorld(mWorld,new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					System.out.println(caught);
				}
				public void onSuccess(String result) {
					System.out.println("saved!");
				}
			});

		}
		
	}
	
	class DiscardButtonHandler implements ClickHandler
	{

		@Override
		public void onClick(ClickEvent event) {
			
			CapstoneWebUI.worldCreationForm.setVisible(false);
			CapstoneWebUI.menuPanel.setVisible(true);
			clearPanel();
		}
		
	}
	
	public void loadWorldInformation()
	{
		
	}
	
	
	//The input is a list of locations such that it is seperated by ','.
	//example: "house,work,gym"
	public void loadLocations(String locations) 
	{
		clearPanel();
		locationsArray = new ArrayList<LocationObject>();
		String[] locationsNameArray = locations.split(",");
		
		for(byte i = 0; i <locationsNameArray.length; i++)
		{
			final String location = locationsNameArray[i];
			CapstoneWebUI.databaseService.getLocation(locationsNameArray[i],new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					System.out.println(caught);
				}
				public void onSuccess(String result) {
					final LocationObject newLocation = new LocationObject();
					newLocation.setLocationName(location);
					String[] resultValues = result.split("\n");
					String[] longitude = resultValues[6].split(" = ");
					String[] latitude = resultValues[5].split(" = ");
					newLocation.setLatitude(latitude[1].toString());
					newLocation.setLongitude(longitude[1].toString());
					addLocation(newLocation);
					locationsArray.add(newLocation);
				}
			});
			//System.out.println(newLocation.getLatitude());
			
		}
	}
}

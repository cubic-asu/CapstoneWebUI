package com.capstonewebui.client;

import java.util.ArrayList;

import com.capstonewebui.shared.LocationObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import com.google.gwt.maps.client.overlays.CircleOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.*;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.Circle;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LocationCreationForm extends AbsolutePanel {

	private MapWidget map;
	private MapOptions mapOpt;
	private Marker mark = Marker.newInstance(null);
	Circle circle = Circle.newInstance(null);
	private TextBox longitudeTB;
	private TextBox latitudeTB;
	private TextArea descriptionTB;
	private TextBox nameTB;
	private FlexTable locationBuilderGrid = new FlexTable();
	private HorizontalPanel mainPanel = new HorizontalPanel();
	private VerticalPanel mapPanel = new VerticalPanel();
	private CheckBox lockedCB;
	private TextBox discoveryRadiusTB;
	private FlexTable dependentLocationsFlexTable;

	public LocationCreationForm() {
		this.setVisible(false);
		this.setSize("800px", "800px");
		this.add(assemblePanels());
	}

	class discoveryRadiusHandler implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (isNumber(discoveryRadiusTB.getText())) {
				if (!discoveryRadiusTB.getText().isEmpty()) {
					circle.setRadius(Double.parseDouble(discoveryRadiusTB
							.getText()));
				}
			} else {
				discoveryRadiusTB.setText("10");
				circle.setRadius(Double.parseDouble(discoveryRadiusTB.getText()));
			}

		}

	}

	private HorizontalPanel assemblePanels() {

		addLabelToPanel();
		addSecondColumnContent();
		addAvailableLocationsHeader();
		setUpMapPanel();
		addNavigationButtons();

		descriptionTB.addStyleName("locationTextArea");
		locationBuilderGrid.setStyleName("locationBuilderGrid");
		locationBuilderGrid.setSize("400px", "400px");
		mainPanel.add(locationBuilderGrid);
		mainPanel.add(mapPanel);

		return mainPanel;

	}

	public void resizeMap() {
		map.triggerResize();
	}

	public void resetOverlays() {
		if (mark != null) {
			mark.close();
		}
		if (circle != null) {
			circle.setMap(null);
		}
	}

	public void buildMapUI() {
		HorizontalPanel mapControlPanel = new HorizontalPanel();

		mapControlPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		LatLng arizonaStateUniversity = LatLng.newInstance(33.453760,
				-112.072978);
		mapOpt = MapOptions.newInstance();
		mapOpt.setCenter(arizonaStateUniversity);
		mapOpt.setZoom(4);
		map = new MapWidget(mapOpt);
		map.setSize("380px", "380px");

		map.addClickHandler(new selectMapLocation());

	}

	private void setUpMapPanel() {
		mapPanel = new VerticalPanel();
		mapPanel.setStyleName("locationMapPanel");
		mapPanel.setSize("400px", "400px");
		buildMapUI();
		mapPanel.add(map);
	}

	private void addLabelToPanel() {
		locationBuilderGrid.setText(0, 0, "Name: ");
		locationBuilderGrid.setText(1, 0, "Description: ");
		locationBuilderGrid.setText(2, 0, "Longitude: ");
		locationBuilderGrid.setText(3, 0, "Latitude: ");
		locationBuilderGrid.setText(4, 0, "Discovery Radius: ");
		locationBuilderGrid.setText(6, 0, "Locked: ");
		locationBuilderGrid.setText(7, 0, "");
		locationBuilderGrid.setText(8, 0, "Dependencies: ");
	}

	// second column is a mix of textBoxes, checkboxes, and list
	private void addSecondColumnContent() {

		TextBox locationIDTB = new TextBox();
		locationIDTB.setEnabled(false);

		nameTB = new TextBox();
		locationBuilderGrid.setWidget(0, 1, nameTB);

		descriptionTB = new TextArea();
		locationBuilderGrid.setWidget(1, 1, descriptionTB);

		longitudeTB = new TextBox();

		FlowPanel longitudeContainer = new FlowPanel();
		longitudeContainer.add(longitudeTB);
		locationBuilderGrid.setWidget(2, 1, longitudeContainer);

		latitudeTB = new TextBox();
		locationBuilderGrid.setWidget(3, 1, latitudeTB);

		discoveryRadiusTB = new TextBox();
		locationBuilderGrid.setWidget(4, 1, discoveryRadiusTB);

		lockedCB = new CheckBox();
		locationBuilderGrid.setWidget(6, 1, lockedCB);

		discoveryRadiusTB.addKeyUpHandler(new discoveryRadiusHandler());
	}

	// this consists "id", "longitude/latitude", and "edit/add" buttons
	private void addAvailableLocationsHeader() {
		dependentLocationsFlexTable = new FlexTable();
		locationBuilderGrid.getFlexCellFormatter().setColSpan(9, 0, 2);
		locationBuilderGrid.setWidget(9, 0, dependentLocationsFlexTable);

		dependentLocationsFlexTable.getColumnFormatter().setWidth(0, "113px");
		dependentLocationsFlexTable.getColumnFormatter().setWidth(1, "125px");
		dependentLocationsFlexTable.getColumnFormatter().setWidth(2, "81px");
		dependentLocationsFlexTable.getRowFormatter().addStyleName(0,
				"locationListHeader");
		dependentLocationsFlexTable.setText(0, 0, "Locations");
		dependentLocationsFlexTable.setText(0, 1, "Unlock");
		dependentLocationsFlexTable.setText(0, 2, "Retire");
	}

	public void disableNameField() {
		nameTB.setEnabled(false);
	}

	public void enableNameField() {
		nameTB.setEnabled(true);
	}

	private void clearPanel() {
		nameTB.setText("");
		descriptionTB.setText("");
		longitudeTB.setText("");
		latitudeTB.setText("");
		lockedCB.setValue(false);

		dependentLocationsFlexTable.removeAllRows();

		dependentLocationsFlexTable.getColumnFormatter().setWidth(0, "26px");
		dependentLocationsFlexTable.getColumnFormatter().setWidth(1, "125px");
		dependentLocationsFlexTable.getColumnFormatter().setWidth(2, "81px");
		dependentLocationsFlexTable.getRowFormatter().addStyleName(0,
				"locationListHeader");
		dependentLocationsFlexTable.setText(0, 0, "Locations");
		dependentLocationsFlexTable.setText(0, 1, "Unlock");
		dependentLocationsFlexTable.setText(0, 2, "Retire");

	}

	private void addNavigationButtons() {
		Button saveButton = new Button("Save");
		Button backButton = new Button("Back");

		HorizontalPanel navigationButtonsContainer = new HorizontalPanel();
		navigationButtonsContainer.add(backButton);
		navigationButtonsContainer.add(saveButton);

		// adds listeners
		backButton.addClickHandler(new CancelLocationHandler());
		saveButton.addClickHandler(new SaveLocationHandler());

		backButton.addStyleName("backButton");
		saveButton.addStyleName("saveButton");

		navigationButtonsContainer.setStyleName("discardSavePanel");
		mapPanel.add(navigationButtonsContainer);
	}

	public void update(LocationObject location,
			ArrayList<LocationObject> allLocations) {
		clearFields();
		longitudeTB.setText(location.getLongitude());
		latitudeTB.setText(location.getLatitude());
		descriptionTB.setText(location.getLocationDescription());
		nameTB.setText(location.getLocationName());
		discoveryRadiusTB.setText(location.getDisoveryRadius());
		// update map with existing marker
		LatLng locationLatLng = LatLng.newInstance(
				Double.parseDouble(location.getLatitude()),
				Double.parseDouble(location.getLongitude()));
		resetOverlays();
		mark.setPosition(locationLatLng);
		mark.setMap(map);
		circle.setCenter(locationLatLng);
		try {
			circle.setRadius(Double.parseDouble(location.getDisoveryRadius()));
		} catch (Exception ex) {
			circle.setRadius(35d);
		}
		circle.setMap(map);
		// don't zoom in too close to marker
		LatLngBounds bounds = LatLngBounds.newInstance(locationLatLng,
				locationLatLng);
		LatLng tempLatLng1 = LatLng.newInstance(bounds.getNorthEast()
				.getLatitude() + 0.01,
				bounds.getNorthEast().getLongitude() + 0.01);
		LatLng tempLatLng2 = LatLng.newInstance(bounds.getNorthEast()
				.getLatitude() - 0.01,
				bounds.getNorthEast().getLongitude() - 0.01);
		bounds.extend(tempLatLng1);
		bounds.extend(tempLatLng2);
		map.fitBounds(bounds);

		updateAvailableLocations(allLocations);
	}

	private boolean isNumber(String str) {
		return str.matches("[0-9]*");
	}

	class selectMapLocation implements ClickMapHandler {
		@Override
		public void onEvent(ClickMapEvent event) {
			LatLng selectedPoint = event.getMouseEvent().getLatLng();

			if (mark != null) {
				mark.setPosition(selectedPoint);
				mark.setMap(map);
				CircleOptions circleOptions = CircleOptions.newInstance();
				circle.setOptions(circleOptions);
				circle.setCenter(selectedPoint);
				circle.setRadius(35d);
				circle.setMap(map);
				discoveryRadiusTB.setText("35");
			} else {
				CircleOptions circleOptions = CircleOptions.newInstance();
				circle.setOptions(circleOptions);
				circle.setCenter(selectedPoint);
				circle.setRadius(35d);
				circle.setMap(map);
				discoveryRadiusTB.setText("35");

				MarkerOptions options = MarkerOptions.newInstance();
				mark.setOptions(options);
				mark.setPosition(selectedPoint);
				mark.setMap(map);
			}

			String selectedPointString = selectedPoint.toString();
			selectedPointString = selectedPointString.replace(")", "");
			selectedPointString = selectedPointString.replace("(", "");
			String selectedPointStringArray[] = selectedPointString.split(",");

			longitudeTB.setText(selectedPointStringArray[1]);
			latitudeTB.setText(selectedPointStringArray[0]);
		}
	}

	private void buildDepedency(LocationObject location) {
		ArrayList<String> locationsToRetireArray = new ArrayList<String>();
		ArrayList<String> locationsToUnlockArray = new ArrayList<String>();
		byte rowCount = (byte) dependentLocationsFlexTable.getRowCount();

		for (int i = 1; i < rowCount; i++) {

			CheckBox cb = (CheckBox) dependentLocationsFlexTable
					.getWidget(i, 1);
			if (cb.getValue() == true) {
				locationsToUnlockArray.add(dependentLocationsFlexTable.getText(
						i, 0));
			}

			cb = (CheckBox) dependentLocationsFlexTable.getWidget(i, 2);
			if (cb.getValue() == true) {
				locationsToRetireArray.add(dependentLocationsFlexTable.getText(
						i, 0));
			}

		}

		location.setLocationToUnlock(locationsToUnlockArray);
		location.setLocationToRetire(locationsToRetireArray);
	}

	private void clearFields() {
		nameTB.setText("");
		descriptionTB.setText("");
		longitudeTB.setText("");
		latitudeTB.setText("");
		discoveryRadiusTB.setText("");
		lockedCB.setValue(false);

		for (int i = 1; i < dependentLocationsFlexTable.getRowCount(); i++) {
			dependentLocationsFlexTable.removeAllRows();
			addAvailableLocationsHeader();
		}
	}

	private void updateAvailableLocations(ArrayList<LocationObject> locations) {
		int locationSize = locations.size();
		byte vacantRow = 1;
		for (int i = 1; i <= locationSize; i++) {

			if (locations.get(i - 1).getLocationName()
					.compareTo(nameTB.getText()) != 0) {
				dependentLocationsFlexTable.setText(vacantRow, 0, locations
						.get(i - 1).getLocationName());
				CheckBox unlock = new CheckBox();
				CheckBox retire = new CheckBox();
				dependentLocationsFlexTable.setWidget(vacantRow, 1, unlock);
				dependentLocationsFlexTable.setWidget(vacantRow, 2, retire);
				vacantRow = (byte) (vacantRow + 1);
			}
		}
	}

	public void generateNewLocation(ArrayList<LocationObject> locations) {
		clearPanel();
		updateAvailableLocations(locations);
	}

	class SaveLocationHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			CapstoneWebUI.locationCreationPanel.setVisible(false);
			CapstoneWebUI.worldCreationForm.setVisible(true);
			CapstoneWebUI.worldCreationForm.resizeMap();
			enableNameField();

			if (!nameTB.getText().isEmpty()
					&& !descriptionTB.getText().isEmpty()
					&& !longitudeTB.getText().isEmpty()) {
				LocationObject location = new LocationObject();
				location.setLocationName(nameTB.getText());
				location.setLocationDescription(descriptionTB.getText());
				location.setLongitude(longitudeTB.getText());
				location.setLatitude(latitudeTB.getText());
				location.setLocked(lockedCB.getValue());
				location.setDisoveryRadius(discoveryRadiusTB.getText());
				buildDepedency(location);
				CapstoneWebUI.worldCreationForm.addLocation(location);
			}

		}
	}

	class CancelLocationHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			CapstoneWebUI.locationCreationPanel.setVisible(false);
			CapstoneWebUI.worldCreationForm.setVisible(true);
			CapstoneWebUI.worldCreationForm.resizeMap();
		}

	}

}

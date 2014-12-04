package com.capstonewebui.server;

import java.util.ArrayList;
import java.util.Map;

import com.capstonewebui.client.DatabaseHandler;
import com.capstonewebui.shared.LocationObject;
import com.capstonewebui.shared.WorldObject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class DatabaseHandlerImpl extends RemoteServiceServlet implements
		DatabaseHandler {

	private static final long serialVersionUID = -6830493241011313788L;

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		return "hello Albert";
	}

	private Entity createWorldEntity(WorldObject world) {
		String world_id = world.getWorldName().replace(" ", "_");
		Entity w = new Entity("world", "name=" + world.getWorldName());
		w.setProperty("name", world.getWorldName());
		w.setProperty("description", world.getWorldDescription());
		w.setProperty("user_id", world.getUserID());
		w.setProperty("world_id", world_id);
		return w;
	}

	private Entity createLocationEntity(LocationObject location) {
		Entity w = new Entity("location", "name=" + location.getLocationName());
		w.setProperty("name", location.getLocationName());
		w.setProperty("loc_id", location.getLocationName().replace(" ", "_"));
		w.setProperty("description", location.getLocationDescription());
		w.setProperty("latitude", location.getLatitude());
		w.setProperty("longitude", location.getLongitude());
		w.setProperty("unlock_threshold", location.getDisoveryRadius());
		w.setProperty("locked", location.isLocked());
		w.setProperty("visited", "false");
		w.setProperty("locations_to_unlock", location.getLocationToUnlock());
		w.setProperty("locations_to_lock", location.getLocationToRetire());
		w.setProperty("world_name", location.getWorld());
		return w;
	}

	@Override
	public String storeLocation(LocationObject object)
			throws IllegalArgumentException {
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Entity w = createLocationEntity(object);
		dss.put(w);

		return null;
	}

	@Override
	public String storeWorld(WorldObject world) throws IllegalArgumentException {
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Entity w = createWorldEntity(world);
		dss.put(w);
		return null;
	}

	@Override
	public String getLocations() throws IllegalArgumentException {
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("location")
				.addSort("Name", SortDirection.ASCENDING);

		PreparedQuery pq = dss.prepare(q);

		for (Entity result : pq.asIterable()) {
			String locations = (String) result.getProperty("Name");
			System.out.println(locations);
		}

		System.out.println("done");
		return null;
	}

	@Override
	public String getWorlds() throws IllegalArgumentException {

		String worlds = "";
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("world").addSort("name", SortDirection.ASCENDING);

		PreparedQuery pq = dss.prepare(q);

		for (Entity result : pq.asIterable()) {

			String world = (String) result.getProperty("name");
			if (worlds.compareTo("") == 0)
				worlds = world;
			else
				worlds = worlds + "," + world;
		}
		return worlds;
	}

	@Override
	public String getWorld(String worldName) throws IllegalArgumentException {

		String locations = "";
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Filter worldFilter = new FilterPredicate("world_name",
				FilterOperator.EQUAL, worldName);
		Query q = new Query("location")
				.addSort("name", SortDirection.ASCENDING);
		q.setFilter(worldFilter);
		PreparedQuery pq = dss.prepare(q);
		for (Entity result : pq.asIterable()) {

			if (locations.compareTo("") == 0)
				locations = (String) result.getProperty("name");
			else
				locations = locations + ","
						+ (String) result.getProperty("name");
		}

		return locations;
	}

	/*
	 * @Override public String getLocation(String locationName) throws
	 * IllegalArgumentException { String locations = "";
	 * System.out.println("location name to get from server is: " +
	 * locationName); DatastoreService dss =
	 * DatastoreServiceFactory.getDatastoreService(); Filter worldFilter = new
	 * FilterPredicate("name",FilterOperator.EQUAL, locationName); Query q = new
	 * Query("location").addSort("name", SortDirection.ASCENDING);
	 * q.setFilter(worldFilter); PreparedQuery pq = dss.prepare(q);
	 * 
	 * return pq.asSingleEntity().toString(); }
	 */

	@Override
	public LocationObject getLocation(String locationName)
			throws IllegalArgumentException {
		LocationObject locationO = new LocationObject();
		System.out.println("location name to get from server is: "
				+ locationName);
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Filter worldFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				locationName);
		Query q = new Query("location")
				.addSort("name", SortDirection.ASCENDING);
		q.setFilter(worldFilter);
		PreparedQuery pq = dss.prepare(q);

		Entity tempEntity = pq.asSingleEntity();
		locationO.setLocationDescription((String) tempEntity
				.getProperty("description"));
		locationO.setLongitude((String) tempEntity.getProperty("longitude"));
		locationO.setLatitude((String) tempEntity.getProperty("latitude"));

		/*
		 * String locationsToUnlockString = (String)
		 * tempEntity.getProperty("locations_to_unlock");
		 * locationsToUnlockString = locationsToUnlockString.replace("[", "");
		 * locationsToUnlockString = locationsToUnlockString.replace("]", "");
		 * 
		 * locationsTo
		 */
		locationO.setDisoveryRadius((String) tempEntity.getProperty("unlock_threshold"));
		locationO.setLocationToUnlock((ArrayList<String>) tempEntity
				.getProperty("locations_to_unlock"));
		locationO.setLocationToRetire((ArrayList<String>) tempEntity
				.getProperty("locations_to_lock"));
		locationO.setLocationName((String) tempEntity.getProperty("name"));

		return locationO;
	}

	@Override
	public String getWorldInfo(String worldName)
			throws IllegalArgumentException { // returns the name and the
												// descriptions

		String worldInfo = "";
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Filter worldFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				worldName);
		Query q = new Query("world").addSort("name", SortDirection.ASCENDING);
		q.setFilter(worldFilter);
		PreparedQuery pq = dss.prepare(q);

		for (Entity result : pq.asIterable()) {
			worldInfo = (String) result.getProperty("name") + ","
					+ (String) result.getProperty("description");

		}

		return worldInfo;
	}

	@Override
	public String deleteLocation(String locationName) // given a locationName,
														// this function will
														// query the database
														// and delete the
														// selected item
			throws IllegalArgumentException {

		String worldInfo = "";
		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Filter worldFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				locationName);
		Query q = new Query("location")
				.addSort("name", SortDirection.ASCENDING);
		q.setFilter(worldFilter);
		PreparedQuery pq = dss.prepare(q);

		for (Entity result : pq.asIterable()) {
			// result.getKey();
			dss.delete(result.getKey());
		}

		return null;
	}

	@Override
	public String deleteWorld(String worldName) throws IllegalArgumentException {

		DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
		Filter worldFilter = new FilterPredicate("world_name",
				FilterOperator.EQUAL, worldName);
		Query q = new Query("location")
				.addSort("name", SortDirection.ASCENDING);
		q.setFilter(worldFilter);
		PreparedQuery pq = dss.prepare(q);
		for (Entity result : pq.asIterable()) {
			dss.delete(result.getKey());
		}

		Filter nameFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				worldName);
		Query worldQuery = new Query("world").addSort("name",
				SortDirection.ASCENDING);
		q.setFilter(nameFilter);
		PreparedQuery preparedWorldQuery = dss.prepare(worldQuery);

		for (Entity result : preparedWorldQuery.asIterable()) {
			// result.getKey();
			dss.delete(result.getKey());
		}

		return "world deleted";
	}
}

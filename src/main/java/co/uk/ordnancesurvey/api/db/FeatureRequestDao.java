package co.uk.ordnancesurvey.api.db;

public interface FeatureRequestDao {

	/**
	 * Request feature items
	 * 
	 * Handles simple paging
	 * 
	 * Repsonse built fully in the SQL statement. No post processing.
	 * 
	 * @throws Exception if any error occurs.
	 */
	String getFeatures(String featureType, int limit, int offset) throws Exception;
	
	
	/**
	 * Request feature items using BBOX core functionality in WFS3
	 * 
	 * @throws Exception if any error occurs.
	 */
	String getFeatureSpatial(String featureType, int limit, int offset, String bbox) throws Exception;

}

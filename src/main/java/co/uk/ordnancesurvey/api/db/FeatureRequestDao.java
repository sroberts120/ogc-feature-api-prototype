package co.uk.ordnancesurvey.api.db;

public interface FeatureRequestDao {

	/**
	 * Request feature items
	 * 
	 * Handles simple paging, bbox and limits
	 * 
	 * Repsonse built fully in the SQL statement. No post processing.
	 * 
	 * @throws Exception if any error occurs.
	 */
	String getFeatures(String featureType, int limit, int offset,String bbox) throws Exception;
	
	/**
	 * Request feature by id
	 * 	 * 
	 * Repsonse built fully in the SQL statement. No post processing.
	 * 
	 * @throws Exception if any error occurs.
	 */
	String getFeature(String featureType, String id) throws Exception;


}

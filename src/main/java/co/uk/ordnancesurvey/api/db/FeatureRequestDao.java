package co.uk.ordnancesurvey.api.db;

public interface FeatureRequestDao {

	/**
	 * Add users wfs request into storage
	 * 
	 * @param userRequest the request to be added.
	 * @return true if request is successfully added, false if customer failed to add.
	 * @throws Exception if any error occurs.
	 */
	String getFeatures(String featureType, int limit, int offset) throws Exception;

}

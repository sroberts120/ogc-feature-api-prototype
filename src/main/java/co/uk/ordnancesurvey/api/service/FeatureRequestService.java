package co.uk.ordnancesurvey.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.uk.ordnancesurvey.api.db.FeatureRequestDao;
import co.uk.ordnancesurvey.api.resources.DateTime;

@Component
public class FeatureRequestService {

	/** Request Database Acess Object */
	@Autowired
	private FeatureRequestDao featureRequestDao;

	/**
	 * TODO Add validation on incoming values
	 * 
	 * @param collection
	 * @param limit
	 * @param offset
	 * @param bbox
	 * @return
	 * @throws Exception
	 */
	public String getFeatures(String collection, String limit, String offset, String bbox, String datetime) throws Exception {
		DateTime datetimeObject = null;
		if(datetime!=null){
		boolean range = datetime.contains("/");
		if(range == true){
			String[] splitRange = datetime.split("/");
			String startInterval = splitRange[0];
			String endIntevral = splitRange[1];
		    datetimeObject = new DateTime(startInterval, endIntevral, null, datetime);
		} else {
		    datetimeObject = new DateTime(null, null, datetime,datetime);

		}
		}
		
		if (limit == null & offset == null) {
			return featureRequestDao.getFeatures(collection, 100, 0, bbox, datetimeObject);
		} else if (limit == null && offset != null) {
			return featureRequestDao.getFeatures(collection, 100, Integer.parseInt(offset), bbox, datetimeObject);
		} else if (limit != null && offset == null) {
			return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), 0, bbox, datetimeObject);
		} else {
			return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), Integer.parseInt(offset), bbox, datetimeObject);
		}
	}

	/**
	 * TODO Add validation on incoming values
	 * 
	 * @param collection
	 * @param limit
	 * @param offset
	 * @param bbox
	 * @return
	 * @throws Exception
	 */
	public String getFeature(String collection, String id) throws Exception {
		return featureRequestDao.getFeature(collection, id);

	}
}

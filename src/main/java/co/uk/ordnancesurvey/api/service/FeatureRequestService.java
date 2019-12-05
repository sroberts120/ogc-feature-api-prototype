package co.uk.ordnancesurvey.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import co.uk.ordnancesurvey.api.db.FeatureRequestDao;

@Component
public class FeatureRequestService {
	
	/** Request Database Acess Object */
	@Autowired
	private FeatureRequestDao featureRequestDao;

	@Value("${ogc.service.url}")
	private String serviceURL;
	
	public String getFeatures(String collection, String limit, String offset, String bbox) throws Exception{
		if (limit == null & offset == null) {
			return featureRequestDao.getFeatures(collection, 100, 0, bbox);
		} else if (limit != null && offset == null) {
			return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), 0, bbox);
		} else {
			return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), Integer.parseInt(offset), bbox);
		}
	}
}

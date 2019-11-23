package co.uk.ordnancesurvey.api.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Database User Request DAO.
 * 
 * This is the postgres implementation of the UserRequestDao.java
 */
@Repository
public class PostgresFeatureRequestDao implements FeatureRequestDao {

	/** Logger */
	private static Logger LOG = LoggerFactory.getLogger(PostgresFeatureRequestDao.class);

	@Qualifier("wfs")
	@Autowired
	private DataSource dataSource;

	@Value("${ogc.service.url}")
	private String serviceURL;

	@Override
	public String getFeatures(String featureType, int limit, int offset, String bbox) throws Exception {
		
		PostgresFeaturesQuery postgresFeaturesQuery = new PostgresFeaturesQuery.Builder()
				.setLimit(limit)
				.setOffset(offset)
				.setFeatureType(featureType)
				.setServiceURL(serviceURL)
				.setBbox(bbox)
				.build();
		
		try (Connection connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();){
			 ResultSet rs = stmt.executeQuery(postgresFeaturesQuery.getFeatureQuery());
			 rs.next();
			return rs.getString(1);
		} catch (SQLException ex) {
			LOG.info("WARNING : Failed to execute following SQL : " + ex.getMessage());
		}
		return null;
	}
}

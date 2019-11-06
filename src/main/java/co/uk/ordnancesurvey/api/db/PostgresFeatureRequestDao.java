package co.uk.ordnancesurvey.api.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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



	@Override
	public String getFeatures(String featureType) throws Exception {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT jsonb_build_object('type', 'FeatureCollection','crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))), 'features', jsonb_agg(features.feature), 'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),'numberReturned', 100,'links', json_build_array(json_object('{href, rel}', '{1,2}'), json_object('{href, rel}', '{1,2}'))) FROM ( SELECT jsonb_build_object('type', 'Feature','id', feature_id,'geometry', ST_AsGeoJSON(geometry)::jsonb,'properties', to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering') AS feature FROM (SELECT * FROM topo_all_in_one.buildings limit 100) inputs) features;")) {
			System.out.println(statement.toString());
			 ResultSet results = statement.executeQuery();
			 results.next();
			return results.getString(1);
		} catch (SQLException ex) {
			LOG.info("WARNING : Failed to execute following SQL : " + ex.getMessage());
		}
		return null;
	}
}

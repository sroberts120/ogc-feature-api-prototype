package co.uk.ordnancesurvey.api.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
	public String getFeatures(String featureType, int limit, int offset) throws Exception {
		try (Connection connection = dataSource.getConnection();
			Statement stmt = connection.createStatement();){
		   
				
			String nextLink;
			String previousLink;
			if(offset == 0){
				previousLink = "'{href, rel}','{prev, "+serviceURL+"/collections/"+featureType+"/items?f=application/json&limit="+limit+"&offset=0}'";
			} else {
				previousLink = "'{href, rel}','{prev, "+serviceURL+"/collections/"+featureType+"/items?f=application/json&limit="+limit+"&offset="+(offset-limit)+"}'";
			}
			
			nextLink = "'{href, rel}','{next, "+serviceURL+"/collections/"+featureType+"/items?f=application/json&limit="+limit+"&offset="+(offset+limit)+"}'";
			String jsobParamarmeterizedBuilderQuery = "SELECT jsonb_build_object('type','FeatureCollection','crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),'features', jsonb_agg(features.feature),'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),'numberReturned', %s,'links', json_build_array(json_object( %s ), json_object( %s ))) FROM (SELECT jsonb_build_object('type',       'Feature','id', feature_id,'geometry',   ST_AsGeoJSON(geometry)::jsonb,'properties', to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering') AS feature FROM (SELECT * FROM topo_all_in_one.buildings ORDER BY page_ordering LIMIT %s OFFSET %s) inputs) features;";
			String query = String.format(jsobParamarmeterizedBuilderQuery, Integer.toString(limit),
					previousLink, nextLink,
					Integer.toString(limit), Integer.toString(offset));

			System.out.println(query);
			
			//ResultSet results = statement.executeQuery();
			 ResultSet rs = stmt.executeQuery(query);
			 rs.next();
			return rs.getString(1);
		} catch (SQLException ex) {
			LOG.info("WARNING : Failed to execute following SQL : " + ex.getMessage());
		}
		return null;
	}
}

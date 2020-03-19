package uk.co.ordnancesurvey.db;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import co.uk.ordnancesurvey.api.db.PostgresFeatureRequestDao;

/**
 * Test class for the Feature Request Database Access Object
 */
public class PostgresFeatureRequestDaoTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/** Inject the PostgresFeatureRequestDao class to be tested */
	@InjectMocks
	PostgresFeatureRequestDao dao = new PostgresFeatureRequestDao();
	
	@Mock
	private DataSource dataSource;

	@Mock
	private Connection connection;

	@Mock
	private Statement statement;

	@Mock
	private ResultSet rs;

	/** Example sql statement created by builder class */
	private final String query = "SELECT jsonb_build_object('type','FeatureCollection','crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),'features', jsonb_agg(features.feature),'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),'numberReturned', 10,'links',json_build_array(json_object( '{href, rel}','{prev, null/collections/buildings/items?f=application/json&limit=10&offset=0}' ),json_object( '{href, rel}','{next, null/collections/buildings/items?f=application/json&limit=10&offset=10}' ))) FROM (SELECT jsonb_build_object('type','Feature','id',feature_id,'geometry',ST_AsGeoJSON(geometry)::jsonb,'properties',to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering') AS feature FROM (SELECT * FROM topo_all_in_one.buildings  ORDER BY page_ordering LIMIT 10 OFFSET 0) inputs) features;";

	
	@Test @Ignore
	public void testSimpleGetFeatures() throws Exception {
		//GIVEN a postgres feature access object
		
		//WHEN
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.createStatement()).thenReturn(statement);
		Mockito.when(statement.executeQuery(query)).thenReturn(rs);
		Mockito.when(rs.getString(1)).thenReturn("RESULTS");

		//THEN
		String response = dao.getFeatures("buildings", 10, 0, null,null);
		assertEquals("RESULTS", response);

	}
	
	@SuppressWarnings("unchecked")
	@Test @Ignore
	public void testFailedGetFeatures() throws Exception {
		//GIVEN a postgres feature access object
		
		//WHEN
		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.createStatement()).thenReturn(statement);
		Mockito.when(statement.executeQuery(query)).thenThrow(SQLException.class);

		//THEN
		String response = dao.getFeatures("buildings", 10, 0, null,null);
		assertEquals(null, response);
	}
}

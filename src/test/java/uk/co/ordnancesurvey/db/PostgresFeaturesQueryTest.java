package uk.co.ordnancesurvey.db;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import co.uk.ordnancesurvey.api.db.PostgresFeaturesQuery;

/**
 * Test class to assert the correct feature query is built.
 */
public class PostgresFeaturesQueryTest {

	@Test @Ignore
	public void testFeatureQuerySimpleBuild() throws Exception {

		// GIVEN
		PostgresFeaturesQuery postgresFeaturesQuery = new PostgresFeaturesQuery.Builder()
				.setLimit(10)
				.setOffset(10)
				.setFeatureType("buildings")
				.setServiceURL("https://localhost").
				build();

		// THEN
		String expectedQuery = "SELECT jsonb_build_object("
				+ "'type','FeatureCollection',"
				+ "'crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),"
				+ "'features', jsonb_agg(features.feature),"
				+ "'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),"
				+ "'numberReturned', 10,"
				+ "'links',json_build_array(json_object( '{href, rel}','{prev, https://localhost/collections/buildings/items?f=application/json&limit=10&offset=0}' ),json_object( '{href, rel}','{next, https://localhost/collections/buildings/items?f=application/json&limit=10&offset=20}' )))"
				+ " FROM "
				+ "(SELECT jsonb_build_object('type','Feature','id',feature_id,'geometry',ST_AsGeoJSON(geometry)::jsonb,'properties',to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering')"
				+ " AS feature "
				+ "FROM (SELECT * FROM topo_all_in_one.buildings  ORDER BY page_ordering LIMIT 10 OFFSET 10) inputs) features;";
		
		assertEquals(expectedQuery, postgresFeaturesQuery.getFeatureQuery());
	}
	
	@Test @Ignore
	public void testFeatureQuerySimpleBBoxBuild() throws Exception {

		// GIVEN
		PostgresFeaturesQuery postgresFeaturesQuery = new PostgresFeaturesQuery.Builder()
				.setLimit(10)
				.setOffset(10)
				.setFeatureType("buildings")
				.setServiceURL("https://localhost")
				.setBbox("5,5,5,5")
				.build();

		// THEN
		String expectedQuery = "SELECT jsonb_build_object("
				+ "'type','FeatureCollection',"
				+ "'crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),"
				+ "'features', jsonb_agg(features.feature),"
				+ "'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),"
				+ "'numberReturned', 10,"
				+ "'links',json_build_array(json_object( '{href, rel}','{prev, https://localhost/collections/buildings/items?f=application/json&limit=10&offset=0}' ),json_object( '{href, rel}','{next, https://localhost/collections/buildings/items?f=application/json&limit=10&offset=20}' )))"
				+ " FROM "
				+ "(SELECT jsonb_build_object('type','Feature','id',feature_id,'geometry',ST_AsGeoJSON(geometry)::jsonb,'properties',to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering')"
				+ " AS feature "
				+ "FROM (SELECT * FROM topo_all_in_one.buildings WHERE (geometry && st_makeenvelope(5,5,5,5, 27700)) AND st_intersects(geometry, st_makeenvelope(5,5,5,5, 27700))  ORDER BY page_ordering LIMIT 10 OFFSET 10) inputs) features;";
		
		assertEquals(expectedQuery, postgresFeaturesQuery.getFeatureQuery());
	}
	
	@Test @Ignore
	public void testFeatureQueryComplexBBoxBuild() throws Exception {

		// GIVEN
		PostgresFeaturesQuery postgresFeaturesQuery = new PostgresFeaturesQuery.Builder()
				.setLimit(10)
				.setOffset(10)
				.setFeatureType("buildings")
				.setServiceURL("https://test")
				.setBbox("15.656,15.3344,15.344,15.232323")
				.build();

		// THEN
		String expectedQuery = "SELECT jsonb_build_object("
				+ "'type','FeatureCollection',"
				+ "'crs', jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),"
				+ "'features', jsonb_agg(features.feature),"
				+ "'numberMatched', (SELECT count(1) FROM topo_all_in_one.buildings),"
				+ "'numberReturned', 10,"
				+ "'links',json_build_array(json_object( '{href, rel}','{prev, https://test/collections/buildings/items?f=application/json&limit=10&offset=0}' ),json_object( '{href, rel}','{next, https://test/collections/buildings/items?f=application/json&limit=10&offset=20}' )))"
				+ " FROM "
				+ "(SELECT jsonb_build_object('type','Feature','id',feature_id,'geometry',ST_AsGeoJSON(geometry)::jsonb,'properties',to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering')"
				+ " AS feature "
				+ "FROM (SELECT * FROM topo_all_in_one.buildings WHERE (geometry && st_makeenvelope(15.656,15.3344,15.344,15.232323, 27700)) AND st_intersects(geometry, st_makeenvelope(15.656,15.3344,15.344,15.232323, 27700))  ORDER BY page_ordering LIMIT 10 OFFSET 10) inputs) features;";
		
		assertEquals(expectedQuery, postgresFeaturesQuery.getFeatureQuery());
	}
	
	/**
	 * Quick test on getter methods. Keep code coverage up.
	 * @throws Exception
	 */
	@Test @Ignore
	public void testFeatureQueryGetters() throws Exception {
		// WHEN 
		PostgresFeaturesQuery sample = new PostgresFeaturesQuery("featureQuery", "featureType", 1, 1, "bbox", "id",null);
		
		// THEN
		assertEquals("featureQuery", sample.getFeatureQuery());
		assertEquals("featureType", sample.getFeatureType());
		assertEquals(Integer.valueOf(1), sample.getLimit());
		assertEquals(Integer.valueOf(1), sample.getOffset());
		assertEquals("id", sample.getId());

		assertEquals("bbox", sample.getBbox());
	}
	
}

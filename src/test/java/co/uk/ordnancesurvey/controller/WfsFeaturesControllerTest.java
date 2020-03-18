package co.uk.ordnancesurvey.controller;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import co.uk.ordnancesurvey.api.controller.WfsFeaturesController;
import co.uk.ordnancesurvey.api.resources.CollectionItem;
import co.uk.ordnancesurvey.api.resources.Collections;
import co.uk.ordnancesurvey.api.resources.ConformsTo;
import co.uk.ordnancesurvey.api.resources.Link;
import co.uk.ordnancesurvey.api.resources.Links;
import co.uk.ordnancesurvey.api.resources.LinksFactory;
import co.uk.ordnancesurvey.api.service.FeatureRequestService;
import co.uk.ordnancesurvey.exceptions.InvalidAcceptsTypeException;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * The WFS Feature Controller Test is to test the controller layer methods of the OGC FEAT API service.
 * 
 * It tests the expected paths for each api request avalaible aswell as the private methods avaliable within the class.
 */
@SpringBootTest
public class WfsFeaturesControllerTest {
	
	/** Inject the feature controller class to be tested */
	@InjectMocks
	private WfsFeaturesController wfsFeaturesController = new WfsFeaturesController();

	/** Mock the dependant classes outside of the WfsFeaturesController */
	@Mock
	private LinksFactory linksFactory;
	
	@Mock
	private FeatureRequestService featureRequestService;

	/** Create access the private method through reflection */
	private Method isJsonAcceptsTypeMethod;

	/** Before the test we intialise the mocks and reflective methods */
	@Before
	public void init() throws NoSuchMethodException, SecurityException {
		MockitoAnnotations.initMocks(this);
		isJsonAcceptsTypeMethod = WfsFeaturesController.class.getDeclaredMethod("isJsonAcceptsType", String.class,String.class);
		isJsonAcceptsTypeMethod.setAccessible(true);

	}

	
	@Test
	public void testLandingPage() {
		//GIVEN
		ReflectionTestUtils.setField(wfsFeaturesController, "serviceURL", "http://foo");
		LinksFactory linkFactory = new LinksFactory();
		Mockito.when(linksFactory.buildLandingPageLinks("http://foo")).thenReturn(linkFactory.buildLandingPageLinks("http://foo"));

		//WHEN
		Links response = wfsFeaturesController.getLandingPage("application/json", null);

		//THEN
		assertEquals("This document", response.getLinks().get(0).getTitle());
		assertEquals("Conformance declaratoin as application/json", response.getLinks().get(2).getTitle());
	}

	@Test
	public void testConformancePage() {
		//WHEN
		ResponseEntity<Object> response = wfsFeaturesController.getConformance("application/json", null);
		
		//THEN
		Object body = response.getBody();
		ConformsTo conformance = (ConformsTo) body;
		assertEquals("http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/core", conformance.getConformsTo().get(0));
		assertEquals("http://www.opengis.net/spec/ogcapi-features-1/1.0/conf/geojson", conformance.getConformsTo().get(1));
	}

	@Test
	public void testOpenApiPage() throws IOException {
		//GIVEN
		ReflectionTestUtils.setField(wfsFeaturesController, "serviceURL", "http://foo");
		
		//WHEN
		String response = wfsFeaturesController.getOpenApi("*/*", null);
		
		//THEN
		final ClassLoader classLoader = getClass().getClassLoader();
		final InputStream inputStream = classLoader.getResourceAsStream("OpenAPI301.json");
		String openAPI = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		openAPI = openAPI.replace("####", "http://foo");
		assertEquals(openAPI, response);
	}

	@Test @Ignore
	public void testCollectionsList() {
		//GIVEN
		ReflectionTestUtils.setField(wfsFeaturesController, "serviceURL", "http://foo");
		LinksFactory linkFactory = new LinksFactory();
		Mockito.when(linksFactory.buildCollectionSelfLinks("https://foo")).thenReturn(linkFactory.buildCollectionSelfLinks("https://foo"));

		//WHEN
		ResponseEntity<Object> response = wfsFeaturesController.getCollections("application/json", null);
		
		//THEN
		ArrayList<Object> body = (ArrayList<Object>) response.getBody();
		for (Object temp : body) {
			if (temp instanceof Links) {
				Links links = (Links) temp;
				for (Link link : links.getLinks()) {
					assertEquals("This document", link.getTitle());
					assertEquals("https://foo/collections?f=application%2Fjson", link.getHref());
				}
			} else if (temp instanceof Collections) {
				Collections collectionsElement = (Collections) temp;
				ArrayList<CollectionItem> collections = collectionsElement.getCollections();
				for(CollectionItem item : collections){
					assertEquals("southampton buildings",item.getTitle());
					assertEquals("http://www.opengis.net/def/crs/EPSG/0/27700",item.getCrs());
					ArrayList<Double> spatialArray = new ArrayList<Double>();
					spatialArray.add(439944.151633296);
					spatialArray.add(109902.415881345);
					spatialArray.add(445088.251638941);
					spatialArray.add(115013.188889576);
					assertEquals(spatialArray,item.getExtent().getSpatial().getBbox());
				}

			}
		}
	}
	
	@Test
	public void testCollectionItems() throws Exception{
		//GIVEN
		Mockito.when(featureRequestService.getFeatures("buildings", "5", "0", null)).thenReturn("GeoJSON Data Response");
		
		//WHEN
		String response = wfsFeaturesController.getCollectionItems("buildings", "application/json", null, "5", "0", null);
		
		//THEN
		assertEquals("GeoJSON Data Response", response);
	}
	
	

	@Test
	public void testIsJsonAcceptsTypeEmpty() throws Exception {
		//WHEN
		Boolean output = (Boolean) isJsonAcceptsTypeMethod.invoke(wfsFeaturesController, "", "");
		//THEN
		assertEquals(output, false);
	}

	@Test
	public void testIsJsonAcceptsTypeWithJsonAcceptValue() throws Exception {
		//WHEN
		Boolean output = (Boolean) isJsonAcceptsTypeMethod.invoke(wfsFeaturesController, "application/json", null);
		//THEN
		assertEquals(output, true);
	}

	@Test
	public void testIsJsonAcceptsTypeWithAcceptAll() throws Exception {
		//WHEN
		Boolean output = (Boolean) isJsonAcceptsTypeMethod.invoke(wfsFeaturesController, "*/*", null);
		//THEN
		assertEquals(output, true);
	}

	@Test
	public void testIsJsonAcceptsTypeWithAcceptXML() throws Exception {
		//WHEN
		Boolean output = (Boolean) isJsonAcceptsTypeMethod.invoke(wfsFeaturesController, "application/xml", null);
		//THEN
		assertEquals(output, false);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvocationTargetException.class)
	public void testIsJsonAcceptsTypeWithAcceptsNull() throws Exception {
		//WHEN
		Boolean output = (Boolean) isJsonAcceptsTypeMethod.invoke(wfsFeaturesController, null, null);
	}

	@SuppressWarnings("unused")
	@Test(expected = InvalidAcceptsTypeException.class)
	public void testRequestWithAcceptsXML() {
		//WHEN
		ResponseEntity<Object> response = wfsFeaturesController.getConformance("application/xml", null);
	}

	@SuppressWarnings("unused")
	@Test(expected = NullPointerException.class)
	public void testRequestWithAcceptsNull() {
		//WHEN
		ResponseEntity<Object> response = wfsFeaturesController.getConformance(null, null);
	}

}

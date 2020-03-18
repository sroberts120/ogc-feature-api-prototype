package co.uk.ordnancesurvey.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import co.uk.ordnancesurvey.api.db.FeatureRequestDao;
import co.uk.ordnancesurvey.api.service.FeatureRequestService;

@SpringBootTest
public class FeatureRequestServiceTest {

	/** Inject the feature controller class to be tested */
	@InjectMocks
	private FeatureRequestService featureRequestService = new FeatureRequestService();

	@Mock
	private FeatureRequestDao featureRequestDao;

	/** Before the test we intialise the mocks and reflective methods */
	@Before
	public void init() throws NoSuchMethodException, SecurityException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFeatureServiceWithNoLimitOrOffset() throws Exception {
		// GIVEN
		Mockito.when(featureRequestDao.getFeatures("collection", 100, 0, "bbox")).thenReturn("NO LIMIT OR OFFSET");

		// WHEN
		String response = featureRequestService.getFeatures("collection", null, null, "bbox");

		// THEN
		assertEquals("NO LIMIT OR OFFSET", response);
	}
	
	@Test
	public void testFeatureServiceWithLimitNoOffset() throws Exception {
		// GIVEN
		Mockito.when(featureRequestDao.getFeatures("collection", 55, 0, "bbox")).thenReturn("55 LIMIT NO OFFSET");

		// WHEN
		String response = featureRequestService.getFeatures("collection", "55", null, "bbox");

		// THEN
		assertEquals("55 LIMIT NO OFFSET", response);
	}
	
	@Test
	public void testFeatureServiceWithLimitAndOffset() throws Exception {
		// GIVEN
		Mockito.when(featureRequestDao.getFeatures("collection", 55, 55, "bbox")).thenReturn("55 LIMIT 55 OFFSET");

		// WHEN
		String response = featureRequestService.getFeatures("collection", "55", "55", "bbox");

		// THEN
		assertEquals("55 LIMIT 55 OFFSET", response);
	}
	
	@Test
	public void testFeatureServiceNoLimitAndOffset() throws Exception {
		// GIVEN
		Mockito.when(featureRequestDao.getFeatures("collection", 100, 55, "bbox")).thenReturn("100 LIMIT 55 OFFSET");

		// WHEN
		String response = featureRequestService.getFeatures("collection", null, "55", "bbox");

		// THEN
		assertEquals("100 LIMIT 55 OFFSET", response);
	}

}

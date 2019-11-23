package co.uk.ordnancesurvey.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.uk.ordnancesurvey.api.db.FeatureRequestDao;
import co.uk.ordnancesurvey.api.resources.CollectionItem;
import co.uk.ordnancesurvey.api.resources.Collections;
import co.uk.ordnancesurvey.api.resources.ConformsTo;
import co.uk.ordnancesurvey.api.resources.Extent;
import co.uk.ordnancesurvey.api.resources.Links;
import co.uk.ordnancesurvey.api.resources.LinksFactory;
import co.uk.ordnancesurvey.api.resources.Spatial;
import co.uk.ordnancesurvey.exceptions.InvalidAcceptsTypeException;

/**
 * WFS FES impementation REST controller.
 * 
 * Including : 
 * Landing page (entry page giving overview of service)
 * Open API Definition (open api 3.0.0 spec)
 * Conformance (conformance with OGC Spec)
 * Collections (access to avaliable data)
 *
 * The services themselves are all JSON based for now. No other response type is
 * accepted currently.
 */
@RestController
public class WfsFeaturesController {

	/** Request Database Acess Object */
	@Autowired
	private FeatureRequestDao featureRequestDao;

	@Value("${ogc.service.url}")
	private String serviceURL;
	
	@Autowired
	private LinksFactory linksFactory;

	/**
	 * Landing page.
	 * 
	 * This page gives a set of links and descriptions to all the avalaible
	 * endpoints on the server
	 * 
	 * @param accept The accept type of the request
	 * @param f The response type parameter that may be defined
	 * @return Links the set of links to avaliable services
	 */
	@RequestMapping(value = "/", produces = "application/json")
	public Links landingPage(@RequestHeader("Accept") final String accept, @RequestParam(required = false) final String f) {
		if (isJsonAcceptsType(accept, f)) {
			return linksFactory.buildLandingPageLinks();
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * API specification.
	 * 
	 * This service returns the OpenAPI 3.0.0 standard response to service
	 * description. (This response can be pulled into swagger etc)
	 * 
	 * This will describe what requests can be made using the service.
	 * 
	 * @return OpenAPI json response
	 * @throws IOException
	 */
	@RequestMapping(value = "/api", produces = "application/json")
	public ResponseEntity<Object> api(@RequestHeader("Accept") final String accept, @RequestParam(required = false) final String f) throws IOException {
		if (isJsonAcceptsType(accept, f)) {
			final ClassLoader classLoader = getClass().getClassLoader();
			final InputStream inputStream = classLoader.getResourceAsStream("OpenAPI301.json");
			String openAPI = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			openAPI = openAPI.replace("####", serviceURL);
			return new ResponseEntity<Object>(openAPI, HttpStatus.OK);
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * Conformance
	 * 
	 * Displays the conformance classes this service follows. In our instance its only core & json
	 * 
	 * @return confomance list
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/conformance", produces = "application/json")
	public ResponseEntity<Object> conformance(@RequestHeader("Accept") final String accept, @RequestParam(required = false) final String f) {
		if (isJsonAcceptsType(accept, f)) {
		final ArrayList<String> conformsTo = new ArrayList<>();
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/core");
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/geojson");
		final ConformsTo comformance = new ConformsTo(conformsTo);
		return new ResponseEntity<Object>(comformance, HttpStatus.OK);
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * Collections
	 * 
	 * Builds a view on avalaible data collections. Reponse returned in
	 * application/json
	 * 
	 * In this instance we only have building available
	 * 
	 * @return avaliable data collections
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/collections", produces = "application/json")
	public ResponseEntity<Object> collection(@RequestHeader("Accept") String accept, @RequestParam(required = false) String f) {
		if (isJsonAcceptsType(accept, f)) {
		final Extent extent = new Extent(new Spatial());
		final CollectionItem buildingsItem = new CollectionItem("buildings_southampton_city", "southampton buildings","Buildings within the central Southampton city area", extent, linksFactory.buildCollectionsBuildingLinks());
		final ArrayList<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
		collectionItems.add(buildingsItem);
		final Collections collections = new Collections(collectionItems);
		
		final ArrayList<Object> collectionResponse = new ArrayList<>();
		collectionResponse.add(linksFactory.buildCollectionSelfLinks());
		collectionResponse.add(collections);
		return new ResponseEntity<Object>(collectionResponse, HttpStatus.OK);
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * Collection
	 * 
	 * This shows the relevant metadata about a given collection.
	 * 
	 * Including links to its items
	 * 
	 * @param collection The collection selected
	 * @param accept header
	 * @param f in line header type
	 * @return collection metadata in application/json
	 */
	@RequestMapping(value = "/collections/{collection}", produces = "application/json")
	public CollectionItem collectionItem(@PathVariable("collection") String collection, @RequestHeader("Accept") String accept, @RequestParam(required = false) String f) {
		if (isJsonAcceptsType(accept, f)) {
			Extent extent = new Extent(new Spatial());
			CollectionItem collectionItem = new CollectionItem("buildings_southampton_city", "southampton buildings", "Buildings within the central Southampton city area", extent, linksFactory.buildCollectionsBuildingLinks());
			return collectionItem;
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * Items
	 * 
	 * Items returns data against given filters
	 * 
	 * @param collection
	 * @param accept
	 * @param f
	 * @param limit
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/collections/{collection}/items", produces = "application/json")
	public String collectionItems(
			@PathVariable("collection") String collection,
			@RequestHeader("Accept") String accept,
			@RequestParam(required = false) String f,
			@RequestParam(required = false) String limit,
			@RequestParam(required = false) String offset,
			@RequestParam(required = false) String bbox)
			throws Exception {
		
		if (isJsonAcceptsType(accept, f)) {
				if (limit == null & offset == null) {
					return featureRequestDao.getFeatures(collection, 100, 0, bbox);
				} else if (limit != null && offset == null) {
					return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), 0, bbox);
				} else {
					return featureRequestDao.getFeatures(collection, Integer.parseInt(limit), Integer.parseInt(offset), bbox);
				}
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * Method to decide if the response request type is acceptable. This service
	 * will accept json and default * requests Al responses will be json or
	 * geojson
	 * 
	 * @param accepts
	 *            this is the accept types header
	 * @param fParameter
	 *            this is the inline accept types header parameter
	 * @return boolean
	 */
	private boolean isJsonAcceptsType(final String accepts, final String fParameter) {
		// Check unspecified accepts type. This is accepted and json returned
		if ((fParameter == null) && (accepts.equals("*/*")||accepts.contains("*/*"))) {
			return true;
			// Check 'f' parameter
		} else if (fParameter != null && fParameter.equals("application/json")) {
			return true;
			// Check accept type header
		} else if (fParameter == null && accepts.equals("application/json")) {
			return true;
		}
		return false;
	}

}

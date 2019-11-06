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
import co.uk.ordnancesurvey.api.resources.Link;
import co.uk.ordnancesurvey.api.resources.Links;
import co.uk.ordnancesurvey.exceptions.InvalidAcceptsTypeException;

/**
 * WFS FES impementation REST controller.
 * 
 * This call defines the resources avaliable within the service.
 * 
 * Including : Landing page (entry page giving overview of service) Open API
 * Definition (open api 3.0.0 spec) Conformance (conformance with OGC Spec)
 * Collections (access to avaliable data)
 *
 * The services themselves are all JSON based for now. No other response type is
 * accepted currently.
 * 
 * TODO Implementation of error handling to spec
 */
@RestController
public class WfsFeaturesController {

	/** Request Database Acess Object */
	@Autowired
	private FeatureRequestDao featureRequestDao;

	@Value("${ogc.service.url}")
	private String serviceURL;

	/**
	 * Landing page.
	 * 
	 * This page gives a set of links and descriptions to all the avalaible
	 * endpoints on the server
	 * 
	 * @param accept
	 *            The accept type of the request
	 * @param f
	 *            The response type parameter that may be defined
	 * @return Links the set of links to avaliable services
	 */
	@RequestMapping("/")
	public Links landingPage(@RequestHeader("Accept") String accept, @RequestParam(required = false) String f) {
		// TODO implement correct error handling according to spec
		if (f == null || f.equals("application/json")) {
			Link landingPage = new Link("self", "application/json", "This document",
					serviceURL + "/?f=application%2Fjson");
			Link api = new Link("service", "application/json", "API definition for this endpoint as application/json",
					serviceURL + "/api?f=application%2Fjson");
			Link conformance = new Link("conformance", "application/json",
					"Conformance declaratoin as application/json", serviceURL + "/conformance?f=application%2Fjson");
			Link data = new Link("data", "application/json", "Collections Metadata as application/json",
					serviceURL + "/collections?f=application%2Fjson");

			ArrayList<Link> linkArray = new ArrayList<>();
			linkArray.add(landingPage);
			linkArray.add(api);
			linkArray.add(conformance);
			linkArray.add(data);
			Links linksSet = new Links(linkArray);
			return linksSet;
		} else {
			throw new InvalidAcceptsTypeException(f);
		}
	}

	/**
	 * API specification.
	 * 
	 * This service returns the OpenAPI 3.0.0 standard resposne to service
	 * description. (This response can be pulled into sweagger etc)
	 * 
	 * This will describe what requests can be made using the service.
	 * 
	 * @return OpenAPI json response
	 * @throws IOException
	 */
	@RequestMapping(value = "/api", produces = "application/json")
	public ResponseEntity<Object> api() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("OpenAPI301.json");
		String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		result = result.replace("####", serviceURL);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	/**
	 * Conformance
	 * 
	 * I need to understand better.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/conformance", produces = "application/json")
	public ResponseEntity<Object> conformance() throws IOException, ParseException {
		ArrayList<String> conformsTo = new ArrayList<>();
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/core");
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/oas30");
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/geojson");
		ConformsTo comformance = new ConformsTo(conformsTo);
		return new ResponseEntity<Object>(comformance, HttpStatus.OK);

	}

	@RequestMapping(value = "/collections", produces = "application/json")
	public ResponseEntity<Object> collection() throws IOException, ParseException {
		Link landingPage = new Link("self", "application/json", "This document",
				serviceURL + "/collections?f=application%2Fjson");
		ArrayList<Link> linkArray = new ArrayList<>();
		linkArray.add(landingPage);
		Links linksSet = new Links(linkArray);

		ArrayList<Object> collectionResponse = new ArrayList<>();
		collectionResponse.add(linksSet);

		ArrayList<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
		ArrayList<Double> extents = new ArrayList<Double>();

		extents.add(439944.151633296);
		extents.add(109902.415881345);
		extents.add(445088.251638941);
		extents.add(115013.188889576);

		ArrayList<Link> collectionLink = new ArrayList<Link>();
		collectionLink.add(new Link("item", "application/json", "Buildings items as application/json",
				serviceURL + "/collections/buildings/items?f=application/json"));

		CollectionItem collectionItem = new CollectionItem("buildings_southampton_city",
				"Buildings within the Southampton (UK) area", extents, collectionLink);
		collectionItems.add(collectionItem);
		Collections collections = new Collections(collectionItems);
		collectionResponse.add(collections);
		return new ResponseEntity<Object>(collectionResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/collections/{example}/items", produces = "application/json")
	public String collectionItems(@PathVariable("example") String example,@RequestHeader("Accept") String accept, @RequestParam(required = false) String f) throws Exception {
		if (f == null || f.equals("application/json")) {
		return featureRequestDao.getFeatures(example);
		}else {
			throw new InvalidAcceptsTypeException(f);

		}

	}

}

package co.uk.ordnancesurvey.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.uk.ordnancesurvey.api.resources.ConformsTo;
import co.uk.ordnancesurvey.api.resources.Link;
import co.uk.ordnancesurvey.api.resources.Links;
import co.uk.ordnancesurvey.exceptions.InvalidAcceptsTypeException;

@RestController
public class WfsFeaturesController {

	@RequestMapping("/")
	public Links landingPage(@RequestHeader("Accept") String accept, @RequestParam(required = false) String f) {
		// SORT ERROR HANDLING
		if (f == null || f.equals("application/json")) {
			Link landingPage = new Link("self", "application/json", "This document",
					"http://localhost:8080/?f=application%2Fjson");
			Link api = new Link("service", "application/json", "API definition for this endpoint as application/json",
					"http://localhost:8080/api?f=application%2Fjson");
			Link conformance = new Link("conformance", "application/json",
					"Conformance declaratoin as application/json",
					"http://localhost:8080/conformance?f=application%2Fjson");
			Link data = new Link("data", "application/json", "Collections Metadata as application/json",
					"http://localhost:8080/collections?f=application%2Fjson");

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

	@RequestMapping(value = "/api", produces = "application/json")
	public ResponseEntity<Object> api() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("OpenAPI301.json");
		String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/conformance", produces = "application/json")
	public ResponseEntity<Object> conformance() throws IOException, ParseException {
		ArrayList<String> conformsTo = new ArrayList<>();
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/core");
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/oas30");
		conformsTo.add("http://www.opengis.net/spec/wfs-1/3.0/req/geojson");
		ConformsTo comformance = new ConformsTo(conformsTo);
		return new ResponseEntity<Object>(comformance, HttpStatus.OK);

	}

}

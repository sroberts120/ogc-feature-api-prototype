package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinksFactory {
	
	@Value("${ogc.service.url}")
	private String serviceURL;

	public Links buildLandingPageLinks(){
		Link landingPage = new Link("self", "application/json", "This document", serviceURL + "/?f=application%2Fjson");
		Link api = new Link("service-desc", "application/json", "The canonical representation of the OpenAPI 3.0 document that describes the API offered at this endpoint as application/json", serviceURL + "/api?f=application%2Fjson");
		Link conformance = new Link("conformance", "application/json", "Conformance declaratoin as application/json", serviceURL + "/conformance?f=application%2Fjson");
		Link data = new Link("data", "application/json", "Collections Metadata as application/json", serviceURL + "/collections?f=application%2Fjson");
		final ArrayList<Link> links = new ArrayList<>();
		links.add(landingPage);
		links.add(api);
		links.add(conformance);
		links.add(data);
		return new Links(links);
	}
	
	public Links buildCollectionSelfLinks(){
		Link collectionSelf = new Link("self", "application/json", "This document", serviceURL + "/collections?f=application%2Fjson");
		ArrayList<Link> linkArray = new ArrayList<>();
		linkArray.add(collectionSelf);
		return new Links(linkArray);
	}
	
	public Links buildCollectionsBuildingLinks(){
		ArrayList<Link> collectionLink = new ArrayList<Link>();
		collectionLink.add(new Link("item", "application/json", "Buildings items as application/json", serviceURL + "/collections/buildings/items?f=application/json"));
		collectionLink.add(new Link("self", "application/json", "this document", serviceURL + "/collections/buildings?f=application/json"));
		return new Links(collectionLink);
	}
}

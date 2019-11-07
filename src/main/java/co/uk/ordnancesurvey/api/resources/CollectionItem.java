package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class CollectionItem {

	public CollectionItem(String id, String title,String description, Extent extent, ArrayList<Link> links) {
		super();
		this.id = id;
		this.extent = extent;
		this.title = title;
		this.links = links;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public Extent getExtent() {
		return extent;
	}
	public ArrayList<Link> getLinks() {
		return links;
	}

	private final String id;
	private final String title;
	private final String description;
	private final Extent extent;
	private final ArrayList<Link> links;
	private final String crs = "http://www.opengis.net/def/crs/EPSG/0/27700";
	public String getCrs() {
		return crs;
	}

	

}

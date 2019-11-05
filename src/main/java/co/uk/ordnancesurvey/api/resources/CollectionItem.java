package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class CollectionItem {

	public CollectionItem(String id, String title, ArrayList<Double> extents, ArrayList<Link> links) {
		super();
		this.id = id;
		this.extents = extents;
		this.title = title;
		this.links = links;
	}

	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<Double> getExtents() {
		return extents;
	}
	public ArrayList<Link> getLinks() {
		return links;
	}

	private final String id;
	private final String title;
	private final ArrayList<Double> extents;
	private final ArrayList<Link> links;

	

}

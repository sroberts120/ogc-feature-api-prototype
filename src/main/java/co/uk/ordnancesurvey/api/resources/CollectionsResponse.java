package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class CollectionsResponse {
	
	//private final Collections collections;

	/** Set of links applicable to this collection */
	//private final Links links;
	
	private final ArrayList<Link> links;
	
	private final ArrayList<CollectionItem> collections;

	
	public ArrayList<CollectionItem> getCollections() {
		return collections;
	}

	public ArrayList<Link> getLinks() {
		return links;
	}

	public CollectionsResponse(final ArrayList<CollectionItem> collections,final ArrayList<Link> links) {
		super();
		this.links = links;
		this.collections = collections;
	}


}

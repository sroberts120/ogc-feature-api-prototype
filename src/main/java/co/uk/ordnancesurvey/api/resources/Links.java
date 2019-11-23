package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

/**
 * Links object wraps up indvidual links for a resource together.
 */
public class Links {

	private final ArrayList<Link> links;

	public ArrayList<Link> getLinks() {
		return links;
	}

	public Links(ArrayList<Link> links) {
		this.links = links;
	}

}

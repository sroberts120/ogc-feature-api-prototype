package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

/**
 * POJO representing a collection item. 
 * This is the definition of a feature set being returned
 */
public class CollectionItem {

	/** Collection item ID e.g buildings */
	private final String id;

	/** Collection title */
	private final String title;

	/** Collection item description */
	private final String description;

	/**
	 * The extent object defining spatial extent of geometries within collection
	 */
	private final Extent extent;

	/** Set of links applicable to this collection */
	private final Links links;

	/** Coordinate reference system. OS used BNG */
	private final String crs = "http://www.opengis.net/def/crs/EPSG/0/27700";

	/**
	 * Collecition Item constructor
	 * 
	 * @param id
	 * @param title
	 * @param description
	 * @param extent
	 * @param links
	 */
	public CollectionItem(final String id, final String title, final String description, final Extent extent,
			final Links links) {
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
		return links.getLinks();
	}

	public String getCrs() {
		return crs;
	}

}

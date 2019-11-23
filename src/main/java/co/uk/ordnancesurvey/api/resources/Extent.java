package co.uk.ordnancesurvey.api.resources;

/**
 * Extent object
 * 
 * Used to store the spatial extent of data within a collection.
 * 
 * This should hold the CRS and bottom left - > top right positions of the max
 * bounding box.
 */
public class Extent {

	/** Spatial information container */
	private final Spatial spatial;

	/**
	 * Constructor
	 * 
	 * @param spatial
	 */
	public Extent(final Spatial spatial) {
		this.spatial = spatial;
	}

	public Spatial getSpatial() {
		return spatial;
	}
}

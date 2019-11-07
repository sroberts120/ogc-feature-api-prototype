package co.uk.ordnancesurvey.api.resources;

public class Extent {

	private final Spatial spatial;

	public Spatial getSpatial() {
		return spatial;
	}

	public Extent(Spatial spatial) {
		this.spatial = spatial;
	}

}

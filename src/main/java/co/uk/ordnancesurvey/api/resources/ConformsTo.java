package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

/**
 * ConformsTo object used to hold array of conformances.
 * 
 * These are the standards that this WFS will follow against the WFS3 specification
 */
public class ConformsTo {

	/** Array of conformances e.g JSON, CORE (see wfs3 spec) */
	private final ArrayList<String> conformsTo;

	public ArrayList<String> getConformsTo() {
		return conformsTo;
	}

	public ConformsTo(ArrayList<String> conformsTo) {
		this.conformsTo = conformsTo;
	}

}


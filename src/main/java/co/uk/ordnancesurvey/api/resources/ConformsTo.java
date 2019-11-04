package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class ConformsTo {

	private final ArrayList<String> conformsTo;

	public ArrayList<String> getConformsTo() {
		return conformsTo;
	}

	public ConformsTo(ArrayList<String> conformsTo) {
		this.conformsTo = conformsTo;
	}

}

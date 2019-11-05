package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class Collections {
	private final ArrayList<CollectionItem> collections;

	public ArrayList<CollectionItem> getCollections() {
		return collections;
	}

	public Collections(ArrayList<CollectionItem> collections) {
		this.collections = collections;
	}
}

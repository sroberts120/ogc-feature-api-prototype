package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Collection object used to hold array of collection items.
 * 
 * I have built everything out as POJOs so that we can make the most of java's
 * abilty to turn these into JSON objects on response
 *
 */
public class Collections {
	
	/** Collection items array */
	private final ArrayList<CollectionItem> collections;

	/**
	 * Constructor
	 * @param collectionItems
	 */
	public Collections(ArrayList<CollectionItem> collectionItems) {
		this.collections = collectionItems;
	}

	public ArrayList<CollectionItem> getCollections() {
		return collections;
	}

}

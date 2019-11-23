package co.uk.ordnancesurvey.api.resources;

/**
 * Link is a heavily used concept within WFS3 specification.
 * 
 * It is used to represent self links, next links, links to other resources
 * 
 * The link class enables navigation through the service
 */
public class Link {

	/**
	 * Link constructor
	 * 
	 * @param rel
	 * @param type
	 * @param title
	 * @param href
	 */
	public Link(String rel, String type, String title, String href) {
		super();
		this.rel = rel;
		this.type = type;
		this.title = title;
		this.href = href;
	}

	/** response type. In our case application/json */
	private final String type;

	/** link title */
	private final String title;

	/** href used for navigation */
	private final String href;

	/** type of relationship */
	private final String rel;

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

}

package co.uk.ordnancesurvey.api.resources;

public class Link {
	private final String rel;

	public Link(String rel, String type, String title, String href) {
		super();
		this.rel = rel;
		this.type = type;
		this.title = title;
		this.href = href;
	}

	private final String type;
	private final String title;
	private final String href;

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

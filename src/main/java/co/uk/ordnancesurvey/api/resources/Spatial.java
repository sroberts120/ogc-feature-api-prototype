package co.uk.ordnancesurvey.api.resources;

import java.util.ArrayList;

public class Spatial {

	
	public ArrayList<Double> getBbox() {
		return bbox;
	}

	public void setBbox(ArrayList<Double> bbox) {
		this.bbox = bbox;
	}

	public String getCrs() {
		return crs;
	}

	private final String crs = "http://www.opengis.net/def/crs/EPSG/0/27700";
	
	
	ArrayList<Double> bbox = new ArrayList<Double>();

	public Spatial(){
		bbox.add(439944.151633296);
		bbox.add(109902.415881345);
		bbox.add(445088.251638941);
		bbox.add(115013.188889576);
	}
	
}

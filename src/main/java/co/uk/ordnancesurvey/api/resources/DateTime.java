package co.uk.ordnancesurvey.api.resources;

public class DateTime {
String startInterval;
String endInterval;

public String getStartInterval() {
	return startInterval;
}

public void setStartInterval(String startInterval) {
	this.startInterval = startInterval;
}

public String getEndInterval() {
	return endInterval;
}

public void setEndInterval(String endInterval) {
	this.endInterval = endInterval;
}

public DateTime(String startInterval, String endInterval){
	this.endInterval = endInterval;
	this.startInterval = startInterval;
}


}

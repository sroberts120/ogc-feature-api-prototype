package co.uk.ordnancesurvey.api.resources;

public class DateTime {
String startInterval;
String endInterval;
String specificDateTime;
String orginalValue;

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

public String getspecificDateTime() {
	return specificDateTime;
}
public String getOrginalValue() {
	return orginalValue;
}
public DateTime(String startInterval, String endInterval, String specificDateTime, String orginalValue){
	this.endInterval = endInterval;
	this.startInterval = startInterval;
	this.specificDateTime = specificDateTime;
	this.orginalValue = orginalValue;
}


}

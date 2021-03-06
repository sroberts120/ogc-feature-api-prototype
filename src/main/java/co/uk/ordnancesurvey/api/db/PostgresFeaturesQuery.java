package co.uk.ordnancesurvey.api.db;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import co.uk.ordnancesurvey.api.resources.DateTime;

public class PostgresFeaturesQuery {

	private final String featureQuery;
	private final String featureType;
	private final Integer limit;
	private final Integer offset;
	private final String bbox;
	private final String id;
	private final DateTime datetime;

	private final static String parameterisedSelectStatement = "SELECT "
			+ "jsonb_build_object(" 
			+ "'type',"
			+ "'FeatureCollection'," 
			+ "'crs',"
			+ " jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),"
			+ "'features'," + " jsonb_agg(features.feature)," 
			+ "'numberMatched'," + " (SELECT count(1) FROM topo_all_in_one.buildings)," 
			+ "'numberReturned'," + " %s,"
			+ "'links'," + "json_build_array(json_object( %s ),json_object( %s )))";

	private final static String parameterisedFromStatement = "FROM " + "(SELECT jsonb_build_object(" + "'type',"
			+ "'Feature'," + "'id'," + "feature_id," + "'geometry'," + "ST_AsGeoJSON(geometry)::jsonb,"
			+ "'properties'," + "to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering')" + " AS feature"
			+ "  FROM ("
			+ " (SELECT * "
			+ " FROM (SELECT ROW_NUMBER() OVER (PARTITION BY feature_id ORDER BY version_timestamp DESC) AS r, t.* FROM topo_all_in_one.buildings t  where %s %s) x WHERE x.r <= 1 )"
			+ "  ORDER BY page_ordering LIMIT %s OFFSET %s) inputs) features;";

	private final static String parameterisedBboxWhereFilter = "AND" + " (geometry && st_makeenvelope(%s, 27700))"
			+ " AND " + "st_intersects(geometry, st_makeenvelope(%s, 27700)) ";
	
	private final static String parameterisedIdWhereFilter = "AND" + " (feature_id = '%s')";
	
	private final static String latestTimeParameter = "(version_timestamp <= now()::timestamp)";

	public String getFeatureQuery() {
		return featureQuery;
	}

	public String getFeatureType() {
		return featureType;
	}

	public Integer getLimit() {
		return limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public String getBbox() {
		return bbox;
	}
	
	public String getId(){
		return id;
	}
	
	public DateTime getDateTime(){
		return datetime;
	}

	public PostgresFeaturesQuery(String featureQuery, String featureType, Integer limit, Integer offset, String bbox, String id, DateTime datetime) {
		this.featureQuery = featureQuery;
		this.featureType = featureType;
		this.limit = limit;
		this.offset = offset;
		this.bbox = bbox;
		this.id = id;
		this.datetime = datetime;
	}

	public static class Builder {
		private String serviceURL;
		private String featureType;
		private Integer limit;
		private Integer offset;
		private String bbox;
		private String id;
		private DateTime datetime;

		public Builder setServiceURL(String serviceURL) {
			this.serviceURL = serviceURL;
			return this;
		}

		public Builder setFeatureType(String featureType) {
			this.featureType = featureType;
			return this;
		}

		public Builder setLimit(Integer limit) {
			this.limit = limit;
			return this;
		}

		public Builder setOffset(int offset) {
			this.offset = offset;
			return this;
		}

		public Builder setBbox(String bbox) {
			this.bbox = bbox;
			return this;
		}
		
		public Builder setId(String id) {
			this.id = id;
			return this;
		}
		
		public Builder setDateTime(DateTime datetime){
			this.datetime = datetime;
			return this;
		}

		public PostgresFeaturesQuery build() throws UnsupportedEncodingException {
			
			String fromStatement;
			String timeQuery;
			String startTime;
			String endTime;
			String timeQueryClause;
			String timeclause;
			if(datetime != null && datetime.getspecificDateTime() == null){
				if(datetime.getStartInterval().equals("..")){
					startTime = "2001-02-12T00:00:00Z";
				} else {
					startTime = datetime.getStartInterval();
				}
				
				if(datetime.getEndInterval().equals("..")){
					endTime = "now()";
				} else {
					endTime = datetime.getEndInterval();
				}
				
				timeQueryClause = "(version_timestamp BETWEEN '"+startTime+"'::timestamp AND '"+endTime+"'::timestamp)";
				
			} else if (datetime != null && !datetime.getspecificDateTime().isEmpty()){
				timeQueryClause = "(version_timestamp = '"+datetime.getspecificDateTime()+"'::timestamp)";
			} else {
			
				timeQueryClause = latestTimeParameter;
			}
			if (bbox != null) {
				String bboxFilter = String.format(parameterisedBboxWhereFilter, bbox, bbox);
				fromStatement = String.format(parameterisedFromStatement,timeQueryClause ,bboxFilter, limit, offset);
			} else if (id != null){
				String idFilter = String.format(parameterisedIdWhereFilter,id);
				fromStatement = String.format(parameterisedFromStatement, timeQueryClause,idFilter, limit, offset);
			} else {
			
				fromStatement = String.format(parameterisedFromStatement, timeQueryClause,"", limit, offset);
			}
			
			
			
			String previousLink;
			if (offset == 0) {
				previousLink = "'{href, rel}','{prev, " + serviceURL + "/collections/" + featureType
						+ "/items?f=application/json&limit=" + limit + "&offset=0%s}'";
			} else {
				previousLink = "'{href, rel}','{prev, " + serviceURL + "/collections/" + featureType
						+ "/items?f=application/json&limit=" + limit + "&offset=" + (offset - limit) + "%s}'";
			}
			String nextLink = "'{href, rel}','{next, " + serviceURL + "/collections/" + featureType
					+ "/items?f=application/json&limit=" + limit + "&offset=" + (offset + limit) + "%s}'";
			
			StringBuilder filterAddtion = new StringBuilder();
			if(bbox != null){
				filterAddtion.append("&bbox="+encodeValue(bbox));
			}
			if(datetime!=null){
				filterAddtion.append("&datetime="+ encodeValue(datetime.getOrginalValue()));
			}
			String nextLinkExtended = String.format(nextLink, filterAddtion.toString());
			String previousLinkExtended =String.format(previousLink, filterAddtion.toString());
			String selectStatement = String.format(parameterisedSelectStatement, limit, previousLinkExtended, nextLinkExtended);
			
			return new PostgresFeaturesQuery((selectStatement + " " + fromStatement), featureType, limit, offset, bbox, id,datetime);
		}
		private String encodeValue(String value) throws UnsupportedEncodingException {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
			}
		
		
		public PostgresFeaturesQuery buildSingleFeat() {
			 
			 String self = "'{rel,href}','{self, " + serviceURL + "/collections/" + featureType + "/items/ID?f=application/json}'";
			
			String collection = "'{rel,href}','{collection, " + serviceURL + "/collections/" + featureType + "/items?f=application/json}'";

			String selectStatement = String.format(parameterisedSelectStatement, 1, self, collection);
			String fromStatement;
			if (id != null){
				String idFilter = String.format(parameterisedIdWhereFilter, id);
				fromStatement = String.format(parameterisedFromStatement, latestTimeParameter,idFilter, limit, offset);
			} else {
				fromStatement = String.format(parameterisedFromStatement, latestTimeParameter,"", limit, offset);
			}
			return new PostgresFeaturesQuery((selectStatement + " " + fromStatement), featureType, limit, offset, bbox, id,datetime);
		}
	}
	

}

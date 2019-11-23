package co.uk.ordnancesurvey.api.db;

public class PostgresFeaturesQuery {

	private final String featureQuery;
	private final String featureType;
	private final Integer limit;
	private final Integer  offset;
	private final String bbox;
	
	private final static String parameterisedSelectStatement = "SELECT "
			+ "jsonb_build_object("
			+ "'type',"
			+ "'FeatureCollection',"
			+ "'crs',"
			+ " jsonb_build_object('type', 'EPSG', 'properties', jsonb_build_object('code',27700, 'coordinate_order',array_to_json('{1,0}'::int[]))),"
			+ "'features',"
			+ " jsonb_agg(features.feature),"
			+ "'numberMatched',"
			+ " (SELECT count(1) FROM topo_all_in_one.buildings),"
			+ "'numberReturned',"
			+ " %s,"
			+ "'links',"
			+ "json_build_array(json_object( %s ),json_object( %s )))";
	
	private final static String parameterisedFromStatement = "FROM "
			+ "(SELECT jsonb_build_object("
			+ "'type',"
			+ "'Feature',"
			+ "'id',"
			+ "feature_id,"
			+ "'geometry',"
			+ "ST_AsGeoJSON(geometry)::jsonb,"
			+ "'properties',"
			+ "to_jsonb(inputs) - 'feature_id' - 'geometry' - 'page_ordering')"
			+ " AS feature"
			+ " FROM "
			+ "(SELECT * FROM topo_all_in_one.buildings %s ORDER BY page_ordering LIMIT %s OFFSET %s) inputs) features;";
	
	private final static String parameterisedBboxWhereFilter = "WHERE"
			+ " (geometry && st_makeenvelope(%s, 27700))"
			+ " AND "
			+ "st_intersects(geometry, st_makeenvelope(%s, 27700)) ";


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

	public PostgresFeaturesQuery(String featureQuery, String featureType, Integer limit, Integer offset, String bbox) {
		this.featureQuery = featureQuery;
		this.featureType = featureType;
		this.limit = limit;
		this.offset = offset;
		this.bbox = bbox;
	}

	public static class Builder {
		private String serviceURL;
		private String featureType;
		private Integer limit;
		private Integer offset;
		private String bbox;
		
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

		public PostgresFeaturesQuery build() {	
			String previousLink;
			if (offset == 0) {
				 previousLink = "'{href, rel}','{prev, " + serviceURL + "/collections/" + featureType + "/items?f=application/json&limit=" + limit + "&offset=0}'";
			} else {
				 previousLink = "'{href, rel}','{prev, " + serviceURL + "/collections/" + featureType + "/items?f=application/json&limit=" + limit + "&offset=" + (offset - limit) + "}'";
			}
			String nextLink = "'{href, rel}','{next, " + serviceURL + "/collections/" + featureType + "/items?f=application/json&limit=" + limit + "&offset=" + (offset + limit) + "}'";
			
			String selectStatement = String.format(parameterisedSelectStatement, limit,previousLink,nextLink);
			String fromStatement;
			if(bbox!= null){
			String bboxFilter = String.format(parameterisedBboxWhereFilter, bbox,bbox);
			fromStatement = String.format(parameterisedFromStatement,bboxFilter,limit, offset);	
			} else {
			fromStatement = String.format(parameterisedFromStatement,"",limit, offset);
			}
			return new PostgresFeaturesQuery((selectStatement + " " + fromStatement), featureType, limit, offset, bbox);
		}
	}

}

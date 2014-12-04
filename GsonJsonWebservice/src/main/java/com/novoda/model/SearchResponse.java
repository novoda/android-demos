package com.novoda.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResponse {
	
	public List<Result> results;
	
	@SerializedName("max_id")
	public long maxId;
	
	@SerializedName("since_id")
	public int sinceId;
	
	@SerializedName("refresh_url")
	public String refreshUrl;
	
	@SerializedName("next_page")
	public String nextPage;
	
	@SerializedName("results_per_page")
	public int resultsPerPage;
	
	public int page;
	
	@SerializedName("completed_in")
	public double completedIn;
	
	@SerializedName("since_id_str")
	public String sinceIdStr;
	
	@SerializedName("max_id_str")
	public String maxIdStr;
	
	public String query;
	
}

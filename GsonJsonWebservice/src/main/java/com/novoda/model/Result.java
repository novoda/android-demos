package com.novoda.model;

import com.google.gson.annotations.SerializedName;

public class Result {
	
	@SerializedName("from_user_id_str")
	public String fromUserIdStr;
	
	@SerializedName("profile_image_url")
	public String profileImageUrl;
	
	@SerializedName("created_at")
	public String createdAt;
	
	@SerializedName("from_user")
	public String fromUser;
	
	@SerializedName("id_str")
	public String idStr;
	
	public Metadata metadata;
	
	@SerializedName("to_user_id")
	public String toUserId;
	
	public String text;
	
	public long id;
	
	@SerializedName("from_user_id")
	public String from_user_id;

	@SerializedName("iso_language_code")
	public String isoLanguageCode;

	@SerializedName("to_user_id_str")
	public String toUserIdStr;

	public String source;
	
}

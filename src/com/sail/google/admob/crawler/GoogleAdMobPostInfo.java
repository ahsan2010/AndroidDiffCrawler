package com.sail.google.admob.crawler;

import java.io.Serializable;

public class GoogleAdMobPostInfo implements Serializable{

	private String postTitle;
	private String postCreationDate;
	private String postBody;
	private String postCreatorName;
	private String postUrl;
	/**
	 * Constcutro
	 * @param postTitle
	 * @param postBody
	 * @param postCreationDate
	 * @param postCreatorName
	 */
	public GoogleAdMobPostInfo(String postTitle, String postBody, String postCreationDate, String postCreatorName){
		this.postTitle = postTitle;
		this.postBody = postBody;
		this.postCreationDate = postCreationDate;
		this.postCreatorName = postCreatorName;
	}	
	
	
	
	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostCreationDate() {
		return postCreationDate;
	}
	public void setPostCreationDate(String postCreationDate) {
		this.postCreationDate = postCreationDate;
	}
	public String getPostBody() {
		return postBody;
	}
	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}
	public String getPostCreatorName() {
		return postCreatorName;
	}
	public void setPostCreatorName(String postCreatorName) {
		this.postCreatorName = postCreatorName;
	}
	
	
}

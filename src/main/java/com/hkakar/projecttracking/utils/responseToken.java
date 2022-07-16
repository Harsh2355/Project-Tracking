package com.hkakar.projecttracking.utils;

public class responseToken {
	private String tokenType;
	
	private String accessToken;
	
	public responseToken() {}

	public responseToken(String tokenType, String accessToken) {
		super();
		this.tokenType = tokenType;
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}

package com.csci5408project.validation;

public class IdentifyQuery {
	
	public String identifyQuery(String query) {
		if(query.substring(0, 6).equalsIgnoreCase("select")) {
			return "SELECT";
		}else if(query.substring(0, 6).equalsIgnoreCase("insert")) {
			return "INSERT";
		}else if(query.substring(0, 6).equalsIgnoreCase("update")) {
			return "UPDATE";
		}else if(query.substring(0, 6).equalsIgnoreCase("delete")) {
			return "DELETE";
		}
		return null;
	}

}

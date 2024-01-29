package dev.ime.solar_fleet.tool;


import org.bson.types.ObjectId;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Checker {

	
	public boolean checkObjectId(ObjectId objectId) {
		
		return objectId != null && ObjectId.isValid(objectId.toString()); 
	       
	}
	
	public boolean checkPage(String page) {
		
		return page != null && page.matches("^[1-9]\\d*$");
		
	}
	
	public boolean checkStringForValidObjectId(String id) {
		
		return id != null && id.matches(RegexPattern.OBJECT_ID);
		
	}
}

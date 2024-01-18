package dev.ime.solar_fleet.tool;


import org.bson.types.ObjectId;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Checker {

	
	public boolean checkObjectId(ObjectId objectId) {
		
		return objectId != null && ObjectId.isValid(objectId.toString()); 
	       
	}
	
	
}

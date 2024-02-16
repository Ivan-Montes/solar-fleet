package dev.ime.solar_fleet.tool;


import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Checker {
	
	public boolean checkPage(String page) {
		
		return page != null && page.matches("^[1-9]\\d*$");
		
	}
	
	public boolean checkStringForValidObjectId(String id) {
		
		return id != null && id.matches(RegexPattern.OBJECT_ID);
		
	}
}

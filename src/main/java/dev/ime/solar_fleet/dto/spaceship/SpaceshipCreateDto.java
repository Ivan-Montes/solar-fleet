package dev.ime.solar_fleet.dto.spaceship;

import org.bson.types.ObjectId;


public record SpaceshipCreateDto(String name, ObjectId shipClassId) {
	 
}
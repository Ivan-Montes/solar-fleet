package dev.ime.solar_fleet.dto.spaceship;

import org.bson.types.ObjectId;

public record SpaceshipDto(ObjectId id, String name, ObjectId shipClassId) {

}

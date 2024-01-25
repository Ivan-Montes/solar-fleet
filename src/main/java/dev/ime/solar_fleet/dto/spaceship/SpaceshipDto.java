package dev.ime.solar_fleet.dto.spaceship;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.tool.RegexPattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SpaceshipDto(@NotNull(message="{NotNull.Entity.id}")ObjectId id, 
							@Pattern( regexp = RegexPattern.NAME_FULL, message="{Pattern.Entity.name}")String name, 
							@NotNull(message="{NotNull.Entity.id}")ObjectId shipClassId) {

}

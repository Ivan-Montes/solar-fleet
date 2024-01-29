package dev.ime.solar_fleet.dto.spaceship;



import dev.ime.solar_fleet.tool.RegexPattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


public record SpaceshipCreateDto(@Pattern( regexp = RegexPattern.NAME_FULL, message="{Pattern.Entity.name}")String name, 
								@NotEmpty(message="{NotEmpty.Entity.field]") @Pattern( regexp = RegexPattern.OBJECT_ID, message="{Pattern.Entity.objectId}")String shipClassId) {
	 
}
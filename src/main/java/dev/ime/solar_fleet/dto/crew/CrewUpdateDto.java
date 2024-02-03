package dev.ime.solar_fleet.dto.crew;

import dev.ime.solar_fleet.tool.RegexPattern;
import jakarta.validation.constraints.Pattern;

public record CrewUpdateDto(@Pattern( regexp = RegexPattern.NAME_FULL, message="{Pattern.Entity.name}")String name,
		@Pattern( regexp = RegexPattern.LASTNAME_FULL, message="{Pattern.Entity.lastname}")String lastname,
		String spaceshipId,
		String positionId) {

}

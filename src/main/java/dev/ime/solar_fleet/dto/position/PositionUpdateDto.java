package dev.ime.solar_fleet.dto.position;

import dev.ime.solar_fleet.tool.RegexPattern;
import jakarta.validation.constraints.Pattern;

public record PositionUpdateDto(@Pattern( regexp = RegexPattern.NAME_FULL, message="{Pattern.Entity.name}")String name) {

}

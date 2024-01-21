package dev.ime.solar_fleet.dto.shipclass;

import dev.ime.solar_fleet.tool.RegexPattern;
import jakarta.validation.constraints.Pattern;

public record ShipClassCreateDto(@Pattern( regexp = RegexPattern.NAME_FULL, message="{Pattern.Entity.name}")String name) {

}

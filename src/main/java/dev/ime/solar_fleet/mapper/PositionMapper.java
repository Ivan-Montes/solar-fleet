package dev.ime.solar_fleet.mapper;

import java.util.List;

import dev.ime.solar_fleet.dto.position.PositionCreateDto;
import dev.ime.solar_fleet.dto.position.PositionDto;
import dev.ime.solar_fleet.dto.position.PositionUpdateDto;
import dev.ime.solar_fleet.entity.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PositionMapper {

	public PositionMapper() {
		super();
	}

	public PositionDto toPositionDto(Position position) {
		
		return new PositionDto(position.getId(), position.getName());
	}
	
	public List<PositionDto> toListPositionDto(List<Position>list){
		
		return list.stream().map(this::toPositionDto).toList();
	}
	
	public Position toPositionFromCreateDto(PositionCreateDto dto) {
		Position position = new Position();
		position.setName(dto.name());
		return position;
	}
	
	public Position toPositionFromUpdateDto(PositionUpdateDto dto) {
		Position position = new Position();
		position.setName(dto.name());
		return position;
	}
}

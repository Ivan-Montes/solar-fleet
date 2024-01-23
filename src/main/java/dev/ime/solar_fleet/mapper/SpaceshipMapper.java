package dev.ime.solar_fleet.mapper;

import java.util.List;

import dev.ime.solar_fleet.dto.spaceship.SpaceshipCreateDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipUpdateDto;
import dev.ime.solar_fleet.entity.Spaceship;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SpaceshipMapper {

	public SpaceshipMapper() {
		super();
	}

	public Spaceship toSpaceshipFromCreate(SpaceshipCreateDto dto) {
		
		Spaceship spaceship = new Spaceship();
		spaceship.setName(dto.name());
		spaceship.setShipClassId(dto.shipClassId());
		return spaceship;
	}
	

	public Spaceship toSpaceshipFromUpdate(SpaceshipUpdateDto dto) {
		
		Spaceship spaceship = new Spaceship();
		spaceship.setName(dto.name());
		spaceship.setShipClassId(dto.shipClassId());
		return spaceship;
	}
	
	public SpaceshipDto toSpaceshipDto(Spaceship spaceship) {
		
		return new SpaceshipDto(spaceship.getId(), spaceship.getName(), spaceship.getShipClassId());
	}
	
	public List<SpaceshipDto>toListSpaceshipDto(List<Spaceship> list){
		
		return list.stream().map(this::toSpaceshipDto).toList();
	}
	
}

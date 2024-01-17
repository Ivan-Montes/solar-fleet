package dev.ime.solar_fleet.mapper;

import java.util.List;

import dev.ime.solar_fleet.dto.shipclass.ShipClassDto;
import dev.ime.solar_fleet.entity.ShipClass;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipClassMapper {

	public ShipClassMapper() {
		super();
	}

	public ShipClassDto toShipClassDto(ShipClass shipClass) {
		
		return new ShipClassDto(shipClass.getId(), shipClass.getName());
		
	}
	
	public List<ShipClassDto> toListShipClassDto(List<ShipClass>list){
		
		return list.stream().map(this::toShipClassDto).toList();
	}
	
	public ShipClass toShipClass(ShipClassDto shipClassDto) {
		
		ShipClass shipClass = new ShipClass();
		shipClass.setId(shipClassDto.id());
		shipClass.setName(shipClassDto.name());
		return shipClass;
	}
	
}
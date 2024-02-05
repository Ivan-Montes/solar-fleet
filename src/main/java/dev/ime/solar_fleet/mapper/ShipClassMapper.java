package dev.ime.solar_fleet.mapper;

import java.util.List;

import dev.ime.solar_fleet.dto.shipclass.ShipClassCreateDto;
import dev.ime.solar_fleet.dto.shipclass.ShipClassDto;
import dev.ime.solar_fleet.dto.shipclass.ShipClassUpdateDto;
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
	
	public ShipClass toShipClassFromShipClassCreateDto(ShipClassCreateDto shipClassCreateDto) {
		
		ShipClass shipClass = new ShipClass();
		shipClass.setName(shipClassCreateDto.name());
		return shipClass;
		
	}

	public ShipClass toShipClassFromShipClassUpdateDto(ShipClassUpdateDto shipClassUpdateDto) {
		ShipClass shipClass = new ShipClass();
		shipClass.setName(shipClassUpdateDto.name());
		return shipClass;
	}
	
}

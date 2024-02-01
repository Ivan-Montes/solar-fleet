package dev.ime.solar_fleet.mapper;

import java.util.List;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.dto.crew.CrewCreateDto;
import dev.ime.solar_fleet.dto.crew.CrewDto;
import dev.ime.solar_fleet.dto.crew.CrewUpdateDto;
import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.tool.Checker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CrewMapper {

	private final Checker checker;
	
	@Inject
	public CrewMapper(Checker checker) {
		this.checker = checker;
	}

	public Crew toCrewFromCreate(CrewCreateDto dto) {
		
		Crew crew = new Crew();
		crew.setName(dto.name());
		crew.setLastname(dto.lastname());
		crew.setSpaceshipId( checker.checkStringForValidObjectId( dto.spaceshipId() )? new ObjectId(dto.spaceshipId()) : null );
		crew.setPositionId( checker.checkStringForValidObjectId( dto.positionId( ))? new ObjectId(dto.positionId()) : null );
		return crew;
	}
	

	public Crew toCrewFromUpdate(CrewUpdateDto dto) {
		
		Crew crew = new Crew();
		crew.setName(dto.name());
		crew.setLastname(dto.lastname());
		crew.setSpaceshipId( checker.checkStringForValidObjectId( dto.spaceshipId() )? new ObjectId(dto.spaceshipId()) : null );
		crew.setPositionId( checker.checkStringForValidObjectId( dto.positionId( ))? new ObjectId(dto.positionId()) : null );
		return crew;
	}
	
	public CrewDto toCrewDto(Crew crew) {
		
		return new CrewDto(crew.getId(), crew.getName(), crew.getLastname(), crew.getSpaceshipId(), crew.getPositionId());
	}
	
	public List<CrewDto>toListCrewDto(List<Crew> list){
		
		return list.stream().map(this::toCrewDto).toList();
	}
	
}

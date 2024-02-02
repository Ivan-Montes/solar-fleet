package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.CrewRepository;
import dev.ime.solar_fleet.repository.PositionRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CrewServiceImpl implements GenericService<Crew> {

	private final CrewRepository crewRepository;
	private final PositionRepository positionRepository;
	private final SpaceshipRepository spaceshipRepository;
	
	@Inject
	public CrewServiceImpl(CrewRepository crewRepository, PositionRepository positionRepository,
			SpaceshipRepository spaceshipRepository) {
		super();
		this.crewRepository = crewRepository;
		this.positionRepository = positionRepository;
		this.spaceshipRepository = spaceshipRepository;
	}

	@Override
	public List<Crew> getAll() {
		return crewRepository.listAll();

	}

	@Override
	public List<Crew> getAllPaged(int page) {
		return crewRepository.findAll().page(page - 1, 5).list();

	}

	@Override
	public Optional<Crew> getById(ObjectId id) {
		return Optional.ofNullable(crewRepository.findById(id));

	}

	@Override
	public Optional<Crew> create(Crew entity) {
		
		if ( authorizeChange(entity) ) {

			crewRepository.persist(entity);		
			return Optional.ofNullable(entity);
		}
		
		return Optional.empty();
	}
	
	private boolean authorizeChange(Crew entity) {		
		
		ObjectId positionId = entity.getPositionId();
		ObjectId spaceshipId = entity.getSpaceshipId();
		
		if ( spaceshipId == null && positionId == null ) {
			return true;
		}
		
		if ( spaceshipId != null ) {
			Optional<Spaceship>optSpace = Optional.ofNullable( spaceshipRepository.findById(entity.getSpaceshipId()) );	
			if( optSpace.isEmpty())return false;
		}
		
		if ( positionId != null ) {
			Optional<Position>optPos = Optional.ofNullable( positionRepository.findById(entity.getPositionId()) );			
			if ( optPos.isEmpty() )return false;
		}
		
		return true;
	}

	@Override
	public Optional<Crew> update(ObjectId id, Crew entity) {
		
		Optional<Crew> opt = Optional.ofNullable(crewRepository.findById(id));
		
		if ( opt.isEmpty() ) return opt;
		
		if ( authorizeChange(entity) ) {		 
			 Crew crew = opt.get();
			 crew.setName(entity.getName());
			 crew.setLastname(entity.getLastname());
			 crew.setPositionId(entity.getPositionId());
			 crew.setSpaceshipId(entity.getSpaceshipId());
			 crewRepository.persistOrUpdate(crew);
			 return Optional.ofNullable(crew);
		 }	 
		 
		return Optional.empty();
	}

	@Override
	public int delete(ObjectId id) {		
		return crewRepository.deleteById(id) ? 0 : 1;
	}

}

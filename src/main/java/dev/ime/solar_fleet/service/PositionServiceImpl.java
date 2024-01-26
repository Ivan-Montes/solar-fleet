package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.repository.PositionRepository;
import dev.ime.solar_fleet.repository.CrewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PositionServiceImpl implements GenericService<Position> {
	
	private final PositionRepository positionRepository;
	private final CrewRepository crewRepository;

	@Inject
	public PositionServiceImpl(PositionRepository positionRepository,CrewRepository crewRepository) {
		this.positionRepository = positionRepository;
		this.crewRepository = crewRepository;
	}

	@Override
	public List<Position> getAll() {
		return positionRepository.listAll();
	}

	
	@Override
	public List<Position> getAllPaged(int page) {
		return positionRepository.findAll().page(page - 1, 5).list();
	}

	@Override
	public Optional<Position> getById(ObjectId id) {
		return Optional.ofNullable(positionRepository.findById(id));
	}

	@Override
	public Optional<Position> create(Position entity) {
		positionRepository.persist(entity);		
		return Optional.ofNullable(entity);
	}

	@Override
	public Optional<Position> update(ObjectId id, Position entity) {
		
		Optional<Position> opt = Optional.ofNullable(positionRepository.findById(id));
		 
		 if ( opt.isPresent() ) {			 
			 Position position = opt.get();
			 position.setName(entity.getName());
			 positionRepository.persistOrUpdate(position);
			 opt = Optional.ofNullable(position);
		 }	 
		 
		return opt;
	}

	@Override
	public int delete(ObjectId id) {
		
		if ( crewRepository.list("positionId", id).isEmpty() ) {
	        return positionRepository.deleteById(id) ? 0 : 1;
	    }
		
		return 2;
	}

}

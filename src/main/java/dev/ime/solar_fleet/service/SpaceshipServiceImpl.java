package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.CrewRepository;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpaceshipServiceImpl implements GenericService<Spaceship>{
	
	private final SpaceshipRepository spaceshipRepository;
	private final ShipClassRepository shipClassRepository;
	private final CrewRepository crewRepository;
	
	@Inject
	public SpaceshipServiceImpl(SpaceshipRepository spaceshipRepository, ShipClassRepository shipClassRepository, CrewRepository crewRepository
			) {
		this.spaceshipRepository = spaceshipRepository;
		this.shipClassRepository = shipClassRepository;
		this.crewRepository = crewRepository;
	}

	@Override
	public List<Spaceship> getAll() {
		return spaceshipRepository.listAll();
	}

	@Override
	public List<Spaceship> getAllPaged(int page) {
		return spaceshipRepository.findAll().page(page - 1, 5).list();
	}
	
	@Override
	public Optional<Spaceship> getById(ObjectId id) {
		return Optional.ofNullable(spaceshipRepository.findById(id));
	}

	@Override
	public Optional<Spaceship> create(Spaceship entity) {
		
		Optional<ShipClass> opt = Optional.ofNullable( shipClassRepository.findById(entity.getShipClassId()) );
		
		if ( opt.isPresent() ) {
			spaceshipRepository.persist(entity);
			return Optional.ofNullable(entity);		
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Spaceship> update(ObjectId id, Spaceship entity) {		
		
		Optional<ShipClass> optShipClass = Optional.ofNullable( shipClassRepository.findById(entity.getShipClassId()) );
		Optional<Spaceship> opt = Optional.ofNullable(spaceshipRepository.findById(id));
		 
		 if ( opt.isPresent() && optShipClass.isPresent() ) {			 
			 Spaceship sp = opt.get();
			 sp.setName(entity.getName());
			 sp.setShipClassId(entity.getShipClassId());
			 spaceshipRepository.persistOrUpdate(sp);
			 return Optional.ofNullable(sp);
		 }	 
		 
		return Optional.empty();
	}

	@Override
	public int delete(ObjectId id) {
		
		if ( crewRepository.list("spaceshipId", id).isEmpty() ) {
			return spaceshipRepository.deleteById(id)? 0 : 1;
		}
		return 2;
	}


	
}

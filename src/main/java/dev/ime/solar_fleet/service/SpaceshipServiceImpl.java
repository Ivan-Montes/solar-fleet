package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpaceshipServiceImpl implements GenericService<Spaceship>{
	
	private final SpaceshipRepository spaceshipRepository;
	private final ShipClassRepository shipClassRepository;
	
	@Inject
	public SpaceshipServiceImpl(SpaceshipRepository spaceshipRepository, ShipClassRepository shipClassRepository) {
		super();
		this.spaceshipRepository = spaceshipRepository;
		this.shipClassRepository = shipClassRepository;
	}

	@Override
	public List<Spaceship> getAll() {
		return spaceshipRepository.findAll().list();
	}

	

	@Override
	public Optional<Spaceship> getById(ObjectId id) {
		return Optional.ofNullable(spaceshipRepository.findById(id));
	}

	@Override
	public Optional<Spaceship> create(Spaceship entity) {

		ObjectId shipClassId = entity.getShipClassId();

		if (shipClassId == null || !ObjectId.isValid(shipClassId.toString())) {
	        return Optional.empty();
	    }
		
		Optional<ShipClass> opt = Optional.ofNullable( shipClassRepository.findById(shipClassId) );
		
		if ( opt.isPresent() ) {
			spaceshipRepository.persist(entity);
			return Optional.ofNullable(entity);		
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Spaceship> update(ObjectId id, Spaceship entity) {
		
		 Optional<Spaceship> opt = Optional.ofNullable(spaceshipRepository.findById(id));
		 
		 if ( opt.isPresent() ) {			 
			 Spaceship sp = opt.get();
			 sp.setName(entity.getName());
			 sp.setShipClassId(entity.getShipClassId());
			 spaceshipRepository.persistOrUpdate(sp);
			 opt = Optional.ofNullable(sp);
		 }	 
		 
		return opt;
	}

	@Override
	public int delete(ObjectId id) {
		return spaceshipRepository.deleteById(id)?0:1;
	}

	
}

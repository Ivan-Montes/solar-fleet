package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import dev.ime.solar_fleet.tool.Checker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpaceshipServiceImpl implements GenericService<Spaceship>{
	
	private final SpaceshipRepository spaceshipRepository;
	private final ShipClassRepository shipClassRepository;
	private final Checker checker;
	
	@Inject
	public SpaceshipServiceImpl(SpaceshipRepository spaceshipRepository, ShipClassRepository shipClassRepository,
			Checker checker) {
		super();
		this.spaceshipRepository = spaceshipRepository;
		this.shipClassRepository = shipClassRepository;
		this.checker = checker;
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

		if ( !checker.checkObjectId(shipClassId) ) {
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
		
		ObjectId shipClassId = entity.getShipClassId();

		if ( !checker.checkObjectId(id) || !checker.checkObjectId(shipClassId)) {
	        return Optional.empty();
	    }
		
		Optional<ShipClass> optShipClass = Optional.ofNullable( shipClassRepository.findById(shipClassId) );
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
		return spaceshipRepository.deleteById(id)?0:1;
	}

	
}

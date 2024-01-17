package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpaceshipServiceImpl implements GenericService<Spaceship>{
	
	private final SpaceshipRepository spaceshipRepository;	
	
	@Inject
	public SpaceshipServiceImpl(SpaceshipRepository spaceshipRepository) {
		this.spaceshipRepository = spaceshipRepository;
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
		spaceshipRepository.persist(entity);		
		return Optional.ofNullable(entity);
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

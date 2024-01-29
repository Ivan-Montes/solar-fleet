package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import dev.ime.solar_fleet.tool.Checker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ShipClassServiceImpl implements GenericService<ShipClass>{

	
	private final ShipClassRepository shipClassRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final Checker checker;
	
	@Inject
	public ShipClassServiceImpl(ShipClassRepository shipClassRepository, Checker checker,SpaceshipRepository spaceshipRepository) {
		this.shipClassRepository = shipClassRepository;
		this.checker = checker;
		this.spaceshipRepository = spaceshipRepository;
	}

	@Override
	public List<ShipClass> getAll() {
		return shipClassRepository.listAll();
	}
	
	@Override
	public List<ShipClass> getAllPaged(int page) {
		return shipClassRepository.findAll().page(page - 1, 5).list();
	}
	
	@Override
	public Optional<ShipClass> getById(ObjectId id) {
		return Optional.ofNullable(shipClassRepository.findById(id));
	}

	@Override
	public Optional<ShipClass> create(ShipClass entity) {
		shipClassRepository.persist(entity);		
		return Optional.ofNullable(entity);
	}

	@Override
	public Optional<ShipClass> update(ObjectId id, ShipClass entity) {
		
		Optional<ShipClass> opt = Optional.ofNullable(shipClassRepository.findById(id));
		 
		 if ( opt.isPresent() ) {			 
			 ShipClass shipClass = opt.get();
			 shipClass.setName(entity.getName());
			 shipClassRepository.persistOrUpdate(shipClass);
			 opt = Optional.ofNullable(shipClass);
		 }	 
		 
		return opt;
	}

	@Override
	public int delete(ObjectId id) {		
		
		if ( !checker.checkObjectId(id) ) {
	        return 1;
	    }
		
		Optional<ShipClass> opt = Optional.ofNullable(shipClassRepository.findById(id));
		if ( opt.isPresent() && spaceshipRepository.list("shipClassId", id).isEmpty() ) {
	        return shipClassRepository.deleteById(id) ? 0 : 1;
	    }
		
		return 1;
	}

}

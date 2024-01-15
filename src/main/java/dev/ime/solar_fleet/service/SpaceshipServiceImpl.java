package dev.ime.solar_fleet.service;

import dev.ime.solar_fleet.repository.SpaceshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpaceshipServiceImpl {
	
	private final SpaceshipRepository spaceshipRepository;	
	
	@Inject
	public SpaceshipServiceImpl(SpaceshipRepository spaceshipRepository) {
		this.spaceshipRepository = spaceshipRepository;
	}

	
}

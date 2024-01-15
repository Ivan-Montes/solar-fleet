package dev.ime.solar_fleet.resource;

import dev.ime.solar_fleet.service.SpaceshipServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path("/spaceships")
public class SpaceshipResource {

	private final SpaceshipServiceImpl spaceshipServiceImpl;	
	
	@Inject
	public SpaceshipResource(SpaceshipServiceImpl spaceshipServiceImpl) {
		this.spaceshipServiceImpl = spaceshipServiceImpl;
	}

	
}

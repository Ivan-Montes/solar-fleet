package dev.ime.solar_fleet.resource;

import java.util.List;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.mapper.ShipClassMapper;
import dev.ime.solar_fleet.service.ShipClassServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/shipclasses")
public class ShipClassResource {

	private final ShipClassServiceImpl shipClassServiceImpl;
	private final ShipClassMapper shipClassMapper;
	
	@Inject
	public ShipClassResource(ShipClassServiceImpl shipClassServiceImpl, ShipClassMapper shipClassMapper) {
		super();
		this.shipClassServiceImpl = shipClassServiceImpl;
		this.shipClassMapper = shipClassMapper;
	}
	
	@GET
	public Response getAll() {
		
		List<ShipClass>list = shipClassServiceImpl.getAll();
		return list.isEmpty()? Response.noContent().build()
								:Response.ok(shipClassMapper.toListShipClassDto(list)).build();
		
	}
}

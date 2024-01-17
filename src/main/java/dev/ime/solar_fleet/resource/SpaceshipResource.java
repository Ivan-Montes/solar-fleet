package dev.ime.solar_fleet.resource;

import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

import dev.ime.solar_fleet.dto.spaceship.SpaceshipCreateDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipUpdateDto;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.mapper.SpaceshipMapper;
import dev.ime.solar_fleet.service.SpaceshipServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/spaceships")
public class SpaceshipResource {

	private final SpaceshipServiceImpl spaceshipServiceImpl;	
	private final SpaceshipMapper spaceshipMapper;
	
	@Inject
	public SpaceshipResource(SpaceshipServiceImpl spaceshipServiceImpl, SpaceshipMapper spaceshipMapper) {
		this.spaceshipServiceImpl = spaceshipServiceImpl;
		this.spaceshipMapper = spaceshipMapper;
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		
		List<Spaceship>list = spaceshipServiceImpl.getAll();
		return list.isEmpty()?	Response.ok("\"error\": \"Empty List -_-\"").build()
								:Response.ok(list.stream()
												.map(spaceshipMapper::toSpaceshipDto)
												.toList())
											.build();	
	}		
	
	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") ObjectId id) {
		
		Optional<Spaceship> opt = spaceshipServiceImpl.getById(id);		
		return opt.isPresent()? Response.ok( spaceshipMapper.toSpaceshipDto( opt.get() ) ).build()
								:Response.ok("\"error\": \"User not found\"").build();
		
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response create(SpaceshipCreateDto dto) {
		
		Optional<Spaceship> opt = spaceshipServiceImpl.create(spaceshipMapper.toSpaceshipFromCreate(dto));
		return opt.isPresent()? Response.status(Response.Status.CREATED)
										.entity( spaceshipMapper.toSpaceshipDto( opt.get() ) )
										.build()
								:Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@PUT
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") ObjectId id, SpaceshipUpdateDto dto) {
		
		Optional<Spaceship> opt = spaceshipServiceImpl.update(id, spaceshipMapper.toSpaceshipFromUpdate(dto));
		return opt.isPresent()? Response.ok( spaceshipMapper.toSpaceshipDto( opt.get() ) ).build()
								:Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") ObjectId id) {
		
		return spaceshipServiceImpl.delete(id) == 0? Response.ok("\"error\": \"Entity deleted\"").build()
													:Response.status(Response.Status.NOT_FOUND)
															.entity("\"error\": \"Entity NOT deleted\"")
															.build();		
	}
	
}

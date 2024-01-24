package dev.ime.solar_fleet.resource;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import dev.ime.solar_fleet.dto.spaceship.SpaceshipCreateDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipUpdateDto;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.mapper.SpaceshipMapper;
import dev.ime.solar_fleet.service.SpaceshipServiceImpl;
import dev.ime.solar_fleet.tool.Checker;
import dev.ime.solar_fleet.tool.MsgStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Spaceship", description="Spaceship Operations")
@Path("/api/spaceships")
public class SpaceshipResource {

	private final SpaceshipServiceImpl spaceshipServiceImpl;	
	private final SpaceshipMapper spaceshipMapper;
	private final Checker checker;
	
	@Inject
	public SpaceshipResource(SpaceshipServiceImpl spaceshipServiceImpl, SpaceshipMapper spaceshipMapper, Checker checker) {
		this.spaceshipServiceImpl = spaceshipServiceImpl;
		this.spaceshipMapper = spaceshipMapper;
		this.checker = checker;
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a List of all Spaceship", description="Get a List of all Spaceship, @return an object Response with a List of DTO's")
	public Response getAll(@QueryParam("page")String page){
		
		List<Spaceship>list;
		
		if ( checker.checkPage(page) ) {
			list = spaceshipServiceImpl.getAllPaged(Integer.valueOf(page));
        } else {
        	list = spaceshipServiceImpl.getAll();  		
        }
		
		return list.isEmpty()?	Response.ok(MsgStatus.EMPTY_LIST).build()
								:Response.ok(spaceshipMapper.toListSpaceshipDto(list)).build();	
	}		
	
	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a Spaceship according to an Id", description="Get a Spaceship according to an Id, @return an object Response with the Spaceship required in a DTO")
	
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Spaceship> opt = spaceshipServiceImpl.getById(new ObjectId(id));	
		
		return opt.isPresent()? Response.ok( spaceshipMapper.toSpaceshipDto( opt.get() ) ).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
		
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Create a new Spaceship", description="Create a new Spaceship, @return an object Response with the Spaceship in a DTO")
	public Response create(@Valid SpaceshipCreateDto dto) {
		
		Optional<Spaceship> opt = spaceshipServiceImpl.create(spaceshipMapper.toSpaceshipFromCreate(dto));
		
		return opt.isPresent()? Response.status(Response.Status.CREATED)
										.entity( spaceshipMapper.toSpaceshipDto( opt.get() ) )
										.build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	@PUT
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary="Update fields in a Spaceship", description="Update fields in a Spaceship, @return an object Response with the Spaceship modified in a DTO")
	public Response update(@PathParam("id") String id, @Valid SpaceshipUpdateDto dto) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Spaceship> opt = spaceshipServiceImpl.update(new ObjectId(id), spaceshipMapper.toSpaceshipFromUpdate(dto));
		return opt.isPresent()? Response.ok( spaceshipMapper.toSpaceshipDto( opt.get() ) ).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Delete a Spaceship by its Id", description="Delete a Spaceship by its Id, @return an object Response with a message")
	public Response delete(@PathParam("id") String id) {

		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		return spaceshipServiceImpl.delete(new ObjectId(id)) == 0? Response.ok(MsgStatus.ENTITY_DELETED).build()
													:Response.status(Response.Status.NOT_FOUND)
															.entity(MsgStatus.ENTITY_NOT_DELETED)
															.build();		
	}
	
}

package dev.ime.solar_fleet.resource;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.ime.solar_fleet.dto.position.PositionCreateDto;
import dev.ime.solar_fleet.dto.position.PositionUpdateDto;
import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.mapper.PositionMapper;
import dev.ime.solar_fleet.service.PositionServiceImpl;
import dev.ime.solar_fleet.tool.Checker;
import dev.ime.solar_fleet.tool.MsgStatus;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Position", description="Position Operations")
@Path("/api/positions")
public class PositionResource {

	private final PositionServiceImpl positionServiceImpl;
	private final PositionMapper positionMapper;
	private final Checker checker;
	
	@Inject
	public PositionResource(PositionServiceImpl positionServiceImpl, PositionMapper positionMapper, Checker checker) {
		super();
		this.positionServiceImpl = positionServiceImpl;
		this.positionMapper = positionMapper;
		this.checker = checker;
	}
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a List of all Position", description="Get a List of all Position, @return an object Response with a List of DTO's")
	public Response getAll(@QueryParam("page")String page) {
		
		List<Position>list;
		
		if ( checker.checkPage(page) ) {
			list = positionServiceImpl.getAllPaged(Integer.valueOf(page));
        } else {
        	list = positionServiceImpl.getAll();    		
        }
		
		return list.isEmpty()? Response.ok(MsgStatus.EMPTY_LIST).build()
				:Response.ok(positionMapper.toListPositionDto(list)).build();
		
	}
	

	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a Position according to an Id", description="Get a Position according to an Id, @return an object Response with the Position required in a DTO")
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Position>opt = positionServiceImpl.getById(new ObjectId(id));
		
		return opt.isPresent()? Response.ok(positionMapper.toPositionDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();		
		
	}

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Create a new Position", description="Create a new Position, @return an object Response with the Position in a DTO")
	public Response create(@Valid PositionCreateDto dto) {
		
		Optional<Position>opt = positionServiceImpl.create(positionMapper.toPositionFromCreateDto(dto));
		
		return opt.isPresent()? Response.status(Response.Status.CREATED)
									.entity( positionMapper.toPositionDto( opt.get() ) )
									.build()
							:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}

	@PUT
	@Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Update fields in a Position", description="Update fields in a Position, @return an object Response with the Position modified in a DTO")
	public Response update(@PathParam("id") String id, @Valid PositionUpdateDto dto) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Position>opt = positionServiceImpl.update(new ObjectId(id), positionMapper.toPositionFromUpdateDto(dto));
		
		return opt.isPresent()? Response.ok(positionMapper.toPositionDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();	
		
	}

	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Delete a Position by its Id", description="Delete a Position by its Id, @return an object Response with a message")
	public Response delete(@PathParam("id") String id) {

		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		int returnValue = positionServiceImpl.delete(new ObjectId(id));
		Response response;
		switch(returnValue){
		case 0:
			response = Response.ok(MsgStatus.ENTITY_DELETED).build();
			break;
		case 1:
			response = Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.ENTITY_NOT_DELETED).build();
			break;
		case 2:
			response = Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.ERROR_DELETE_ASSOCIATED_ITEMS).build();
			break;
		default:
			response = Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.ERROR_WTF).build();
			break;
		}
		return response;
	}
}

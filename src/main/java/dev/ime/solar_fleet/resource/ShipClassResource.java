package dev.ime.solar_fleet.resource;


import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.ime.solar_fleet.dto.shipclass.ShipClassDto;
import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.mapper.ShipClassMapper;
import dev.ime.solar_fleet.service.ShipClassServiceImpl;
import dev.ime.solar_fleet.tool.Checker;
import dev.ime.solar_fleet.tool.MsgStatus;
import jakarta.inject.Inject;
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

@Tag(name = "ShipClass", description="ShipClass Operations")
@Path("/api/shipclasses")
public class ShipClassResource {

	private final ShipClassServiceImpl shipClassServiceImpl;
	private final ShipClassMapper shipClassMapper;
	private final Checker checker;
	
	@Inject
	public ShipClassResource(ShipClassServiceImpl shipClassServiceImpl, ShipClassMapper shipClassMapper, Checker checker) {
		this.shipClassServiceImpl = shipClassServiceImpl;
		this.shipClassMapper = shipClassMapper;
		this.checker = checker;
	}
	
	@GET
	@Operation(summary="Get a List of all ShipClass", description="Get a List of all ShipClass, @return an object Response with a List of DTO's")
	public Response getAll(@QueryParam("page")String page) {
		
		List<ShipClass>list;
		
		if ( checker.checkPage(page) ) {
			list = shipClassServiceImpl.getAllPaged(Integer.valueOf(page));
        } else {
        	list = shipClassServiceImpl.getAll();    		
        }
		
		return list.isEmpty()? Response.ok(MsgStatus.EMPTY_LIST).build()
				:Response.ok(shipClassMapper.toListShipClassDto(list)).build();
	}
	
	@GET
	@Path("/{id}")
	@Operation(summary="Get a ShipClass according to an Id", description="Get a ShipClass according to an Id, @return an object Response with the ShipClass required in a DTO")
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<ShipClass>opt = shipClassServiceImpl.getById(new ObjectId(id));
		
		return opt.isPresent()? Response.ok(shipClassMapper.toShipClassDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();		
		
	}
	
	@POST
	@Operation(summary="Create a new ShipClass", description="Create a new ShipClass, @return an object Response with the ShipClass in a DTO")
	public Response create(ShipClassDto dto) {
		
		Optional<ShipClass>opt = shipClassServiceImpl.create(shipClassMapper.toShipClass(dto));
		
		return opt.isPresent()? Response.status(Response.Status.CREATED)
									.entity( shipClassMapper.toShipClassDto( opt.get() ) )
									.build()
							:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	@PUT
	@Path("/{id}")
	@Operation(summary="Update fields in a ShipClass", description="Update fields in a ShipClass, @return an object Response with the ShipClass modified in a DTO")
	public Response update(@PathParam("id") String id, ShipClassDto dto) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<ShipClass>opt = shipClassServiceImpl.update(new ObjectId(id), shipClassMapper.toShipClass(dto));
		
		return opt.isPresent()? Response.ok(shipClassMapper.toShipClassDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();	
		
	}
	
	@DELETE
	@Path("/{id}")
	@Operation(summary="Delete a ShipClass by its Id", description="Delete a ShipClass by its Id, @return an object Response with a message")
	public Response delete(@PathParam("id") String id) {

		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		return shipClassServiceImpl.delete(new ObjectId(id)) == 0? Response.ok(MsgStatus.ENTITY_DELETED).build()
													:Response.status(Response.Status.NOT_FOUND)
															.entity(MsgStatus.ENTITY_NOT_DELETED)
															.build();		
	}
}

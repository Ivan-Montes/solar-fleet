package dev.ime.solar_fleet.resource;


import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

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
    @Produces(MediaType.APPLICATION_JSON)
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
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<ShipClass>opt = shipClassServiceImpl.getById(new ObjectId(id));
		
		return opt.isPresent()? Response.ok(shipClassMapper.toShipClassDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();		
		
	}
	
	@POST
	public Response create(ShipClassDto dto) {
		
		Optional<ShipClass>opt = shipClassServiceImpl.create(shipClassMapper.toShipClass(dto));
		
		return opt.isPresent()? Response.status(Response.Status.CREATED)
									.entity( shipClassMapper.toShipClassDto( opt.get() ) )
									.build()
							:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	@PUT
	@Path("/{id}")
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

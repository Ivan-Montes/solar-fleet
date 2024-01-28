package dev.ime.solar_fleet.resource;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.mapper.CrewMapper;
import dev.ime.solar_fleet.service.CrewServiceImpl;
import dev.ime.solar_fleet.tool.Checker;
import dev.ime.solar_fleet.tool.MsgStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "Crew", description="Crew Operations")
@Path("/api/crew")
public class CrewResource {

	private final CrewServiceImpl crewServiceImpl;
	private final CrewMapper crewnMapper;
	private final Checker checker;

	@Inject
	private CrewResource(CrewServiceImpl crewServiceImpl, CrewMapper crewnMapper, Checker checker) {
		super();
		this.crewServiceImpl = crewServiceImpl;
		this.crewnMapper = crewnMapper;
		this.checker = checker;
	}
	

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a List of all Crew", description="Get a List of all Crew, @return an object Response with a List of DTO's")
	public Response getAll(@QueryParam("page")String page) {
		
		List<Crew>list;
		
		if ( checker.checkPage(page) ) {
			list = crewServiceImpl.getAllPaged(Integer.valueOf(page));
        } else {
        	list = crewServiceImpl.getAll();    		
        }
		
		return list.isEmpty()? Response.ok(MsgStatus.EMPTY_LIST).build()
				:Response.ok(crewnMapper.toListCrewDto(list)).build();
		
	}
	

	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a Crew according to an Id", description="Get a Crew according to an Id, @return an object Response with the Crew required in a DTO")
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Crew>opt = crewServiceImpl.getById(new ObjectId(id));
		
		return opt.isPresent()? Response.ok(crewnMapper.toCrewDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();		
		
	}
	
}

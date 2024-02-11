package dev.ime.solar_fleet.resource;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import dev.ime.solar_fleet.dto.crew.CrewCreateDto;
import dev.ime.solar_fleet.dto.crew.CrewUpdateDto;
import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.mapper.CrewMapper;
import dev.ime.solar_fleet.service.CrewServiceImpl;
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

@Tag(name = "Crew", description="Crew Operations")
@Path("/api/crew")
public class CrewResource {

	private final CrewServiceImpl crewServiceImpl;
	private final CrewMapper crewMapper;
	private final Checker checker;

	@Inject
	public CrewResource(CrewServiceImpl crewServiceImpl, CrewMapper crewnMapper, Checker checker) {
		super();
		this.crewServiceImpl = crewServiceImpl;
		this.crewMapper = crewnMapper;
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
				:Response.ok(crewMapper.toListCrewDto(list)).build();
		
	}
	

	@GET
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Get a Crew according to an Id", description="Get a Crew according to an Id, @return an object Response with the entity required in a DTO")
	public Response getById(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Crew>opt = crewServiceImpl.getById(new ObjectId(id));
		
		return opt.isPresent()? Response.ok(crewMapper.toCrewDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();		
		
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Create a new Crew", description="Create a new Crew, @return an object Response with the entity in a DTO")
	public Response create(@Valid CrewCreateDto dto) {
		
		Crew crew = crewMapper.toCrewFromCreate(dto);
		Optional<Crew> opt = crewServiceImpl.create(crew);
		
		return opt.isPresent()? Response.status(Response.Status.CREATED).entity( crewMapper.toCrewDto(opt.get())).build()
								:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	
	@PUT
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary="Update fields in a Crew", description="Update fields in a Crew, @return an object Response with the entity modified in a DTO")
	public Response update(@PathParam("id") String id, @Valid CrewUpdateDto dto ) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		Optional<Crew> opt = crewServiceImpl.update(new ObjectId(id), crewMapper.toCrewFromUpdate(dto));
		return opt.isPresent()? Response.ok( crewMapper.toCrewDto( opt.get() ) ).build()
				:Response.status(Response.Status.NOT_FOUND).entity(MsgStatus.RESOURCE_NOT_FOUND).build();
	}
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Operation(summary="Delete a Crew by its Id", description="Delete a Crew by its Id, @return an object Response with a message")
	public Response delete(@PathParam("id") String id) {
		
		if( !ObjectId.isValid(id) ){			
			return Response.status(400).entity(MsgStatus.INVALID_OBJECTID).build();
		}
		
		return crewServiceImpl.delete(new ObjectId(id)) == 0? Response.ok(MsgStatus.ENTITY_DELETED).build()
															:Response.status(Response.Status.NOT_FOUND)
															.entity(MsgStatus.ENTITY_NOT_DELETED)
															.build();		
	}
}

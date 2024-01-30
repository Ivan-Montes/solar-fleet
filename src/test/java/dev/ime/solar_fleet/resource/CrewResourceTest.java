package dev.ime.solar_fleet.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.dto.crew.CrewCreateDto;
import dev.ime.solar_fleet.dto.crew.CrewDto;
import dev.ime.solar_fleet.dto.crew.CrewUpdateDto;
import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.service.CrewServiceImpl;
import dev.ime.solar_fleet.tool.MsgStatus;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(CrewResource.class)
class CrewResourceTest {

	@InjectMock
	private CrewServiceImpl crewServiceImplMock;
	
	
	private final String nameTest = "Zapp";
	private final String lastnameTest = "Brannigan";
	private final String idTestError = "65a9qwromtt13b3df";
	private List<Crew>crewList;
	private Crew crewTest;
	private CrewCreateDto crewCreateDtoTest;
	private CrewUpdateDto crewUpdateDtoTest;
	private ObjectId objectIdTest;
	
	
	@BeforeEach
	private void createObjects() {
		objectIdTest = ObjectId.get();
		crewList = new ArrayList<>();
		crewTest = new Crew(objectIdTest, nameTest, lastnameTest, ObjectId.get(), ObjectId.get());
		crewCreateDtoTest = new CrewCreateDto(nameTest, lastnameTest, ObjectId.get().toString(), ObjectId.get().toString());
		crewUpdateDtoTest = new CrewUpdateDto(nameTest, lastnameTest, ObjectId.get().toString(), ObjectId.get().toString());
	}
	
	@Test
	void CrewResource_getAll_ReturnEmptyList() {
		
		doReturn(crewList).when(crewServiceImplMock).getAll();
		
		given()
		.when()
		.get()
		.then()
		.statusCode(200)
		.body(is(MsgStatus.EMPTY_LIST));
		
		Mockito.verify(crewServiceImplMock,times(1)).getAll();
	}

	@Test
	void CrewResource_getAll_ReturnList() {
		
		crewList.add(crewTest);
		doReturn(crewList).when(crewServiceImplMock).getAll();
		
		List<CrewDto>list = given()
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
				        .body()
				        .jsonPath()
				        .getList(".", CrewDto.class);
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		Mockito.verify(crewServiceImplMock, times(1)).getAll();

	}

	@Test
	void CrewResource_getAll_ReturnListPaged() {
		
		crewList.add(crewTest);
		doReturn(crewList).when(crewServiceImplMock).getAllPaged(Mockito.anyInt());
		
		given()
		.queryParam("page", "1")
		.when()
		.get()
		.then()
		.statusCode(200);		
		
		Mockito.verify(crewServiceImplMock, times(1)).getAllPaged(Mockito.anyInt());
		
	}
	
	@Test
	void CrewResource_getById_ReturnObject() {
		
		doReturn(Optional.ofNullable(crewTest)).when(crewServiceImplMock).getById(Mockito.any(ObjectId.class));
		
		CrewDto dto = given()
				.when()
				.get("/{id}", objectIdTest.toString())
				.then()
				.statusCode(200)
				.extract()
				.as(CrewDto.class);
		
		assertAll(
				()->Assertions.assertThat(dto).isNotNull(),
				()->Assertions.assertThat(dto.name()).isEqualTo(nameTest),
				()->Assertions.assertThat(dto.lastname()).isEqualTo(crewUpdateDtoTest.lastname())
			);
		verify(crewServiceImplMock,times(1)).getById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void CrewResource_getById_ReturnIdError() {
		
		given()
		.when()
		.get("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
	}

	@Test
	void CrewResource_getById_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(crewServiceImplMock).getById(Mockito.any(ObjectId.class));
		
		given()
		.when()
		.get("/{id}", objectIdTest.toString())
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(crewServiceImplMock,times(1)).getById(Mockito.any(ObjectId.class));

	}
	
	@Test
	void CrewResource_create_ReturnObject() {
		
		doReturn(Optional.ofNullable(crewTest)).when(crewServiceImplMock).create(Mockito.any(Crew.class));
		
		CrewDto dto = given()
				.contentType(ContentType.JSON)
				.body(crewCreateDtoTest)
				.post()
				.then()
				.statusCode(201)
				.extract().as(CrewDto.class);
		
		assertAll(
		()->Assertions.assertThat(dto).isNotNull(),
		()->Assertions.assertThat(dto.name()).isEqualTo(crewCreateDtoTest.name()),
		()->Assertions.assertThat(dto.lastname()).isEqualTo(crewUpdateDtoTest.lastname())
		);
		Mockito.verify(crewServiceImplMock,times(1)).create(Mockito.any(Crew.class));
	}

	@Test
	void CrewResource_create_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(crewServiceImplMock).create(Mockito.any(Crew.class));
		
		given()
		.contentType(ContentType.JSON)
		.body(crewCreateDtoTest)
		.post()
		.then()
		.statusCode(404)
		.body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(crewServiceImplMock,times(1)).create(Mockito.any(Crew.class));
	}
	
	@Test
	void CrewResource_update_ReturnObject() {
		
		doReturn(Optional.ofNullable(crewTest)).when(crewServiceImplMock).update(Mockito.any(ObjectId.class), Mockito.any(Crew.class));
		
		CrewDto dto = given()
						.contentType(ContentType.JSON)
						.body(crewUpdateDtoTest)
						.put("/{id}", objectIdTest.toString())
						.then()
						.statusCode(200)
						.extract().as(CrewDto.class);
		assertAll(
		()->Assertions.assertThat(dto).isNotNull(),
		()->Assertions.assertThat(dto.name()).isEqualTo(crewUpdateDtoTest.name()),
		()->Assertions.assertThat(dto.lastname()).isEqualTo(crewUpdateDtoTest.lastname())
		);
		Mockito.verify(crewServiceImplMock,times(1)).update(Mockito.any(ObjectId.class), Mockito.any(Crew.class));
	}
	

	@Test
	void CrewResource_update_ReturnIdError() {
		
		given()
		.contentType(ContentType.JSON)
		.body(crewUpdateDtoTest)
		.put("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}

	@Test
	void CrewResource_update_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(crewServiceImplMock).update(Mockito.any(ObjectId.class), Mockito.any(Crew.class));
		
		given()
		.contentType(ContentType.JSON)
		.body(crewUpdateDtoTest)
		.put("/{id}", objectIdTest.toString())
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(crewServiceImplMock,times(1)).update(Mockito.any(ObjectId.class),Mockito.any(Crew.class));
	}
	

	@Test
	void CrewResource_delete_ReturnOk() {
		
		doReturn(0).when(crewServiceImplMock).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", objectIdTest.toString())                
		.then()
		.statusCode(200)
        .body(is(MsgStatus.ENTITY_DELETED));
		
		Mockito.verify(crewServiceImplMock,times(1)).delete(Mockito.any(ObjectId.class));
	}

	@Test
	void CrewResource_delete_ReturnIdError() {
		
		given()
        .delete("/{id}", idTestError)                
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}

	@Test
	void CrewResource_delete_ReturnNotFound() {
		
		doReturn(1).when(crewServiceImplMock).delete(Mockito.any(ObjectId.class));

		given()
        .delete("/{id}", objectIdTest.toString())                
		.then()
		.statusCode(404)
        .body(is(MsgStatus.ENTITY_NOT_DELETED));
		
		Mockito.verify(crewServiceImplMock,times(1)).delete(Mockito.any(ObjectId.class));

	}
}

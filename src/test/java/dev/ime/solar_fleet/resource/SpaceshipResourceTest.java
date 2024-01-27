package dev.ime.solar_fleet.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.dto.spaceship.SpaceshipCreateDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipDto;
import dev.ime.solar_fleet.dto.spaceship.SpaceshipUpdateDto;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.service.SpaceshipServiceImpl;
import dev.ime.solar_fleet.tool.MsgStatus;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;



@QuarkusTest
@TestHTTPEndpoint(SpaceshipResource.class)
class SpaceshipResourceTest {

	@InjectSpy
	private SpaceshipServiceImpl spaceshipServiceImplMock;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "BlueNoah";
	private final String idTestError = "65a909b13b3df36";
	private Spaceship spaceshipTest;
	
	@Test
	void SpaceshipResource_getAll_ReturnList() {
				given()
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}
	
	@Test
	void SpaceshipResource_getAll_ReturnEmptyList() {
		
		List<Spaceship>list = new ArrayList<>();
		when(spaceshipServiceImplMock.getAll()).thenReturn(list);
		
				given()
		        .when()
		        .get()
		        .then()
		        .statusCode(200)
		        .body(is(MsgStatus.EMPTY_LIST));
				
		Mockito.verify(spaceshipServiceImplMock,times(1)).getAll();
	}
	

	@Test
	void SpaceshipResource_getAll_ReturnListPaged() {
		
				given()
				.queryParam("page", 1)
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}
	
	@Test
	void SpaceshipResource_getById_ReturnObject() {
		
		spaceshipTest = new Spaceship(new ObjectId(idTest),nameTest,new ObjectId(idTest));
		doReturn(Optional.ofNullable(spaceshipTest)).when(spaceshipServiceImplMock).getById(Mockito.any());
		
		SpaceshipDto spaceshipDto = given()
		.when()
		.get("/{id}",idTest)
		.then()
		.statusCode(200)
		.extract().as(SpaceshipDto.class);
		
		assertAll(
				()->Assertions.assertThat(spaceshipDto).isNotNull(),
				()->Assertions.assertThat(spaceshipDto.name()).isEqualTo(spaceshipTest.getName())
				);
		Mockito.verify(spaceshipServiceImplMock,times(1)).getById(Mockito.any());
	}
	
	@Test
	void SpaceshipResource_getById_ReturnIdError() {
		
		given()
		.when()
		.get("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}
	

	@Test
	void SpaceshipResource_getById_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(spaceshipServiceImplMock).getById(Mockito.any());
		
		given()
		.when()
		.get("/{id}", idTest)
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(spaceshipServiceImplMock,times(1)).getById(Mockito.any());

	}
	
	@Test
	void SpaceshipResource_create_ReturnObject() {
		
		spaceshipTest = new Spaceship(new ObjectId(idTest), nameTest, new ObjectId(idTest));
		SpaceshipCreateDto spaceshipCreateDto = new SpaceshipCreateDto(nameTest, idTest);
		doReturn(Optional.ofNullable(spaceshipTest)).when(spaceshipServiceImplMock).create(Mockito.any());
		
		SpaceshipDto spaceshipDto = given()
									.contentType(ContentType.JSON)
									.body(spaceshipCreateDto)
									.post()
									.then()
									.statusCode(201)
									.extract().as(SpaceshipDto.class);
		assertAll(
				()->Assertions.assertThat(spaceshipDto).isNotNull(),
				()->Assertions.assertThat(spaceshipDto.name()).isEqualTo(spaceshipCreateDto.name())
				);
		Mockito.verify(spaceshipServiceImplMock,times(1)).create(Mockito.any());

	}
	
	@Test
	void SpaceshipResource_create_ReturnNotFound() {
		
		spaceshipTest = new Spaceship(new ObjectId(idTest), nameTest, new ObjectId(idTest));
		SpaceshipCreateDto spaceshipCreateDto = new SpaceshipCreateDto(nameTest, idTest);
		doReturn(Optional.empty()).when(spaceshipServiceImplMock).create(Mockito.any());
		
		given()
		.contentType(ContentType.JSON)
		.body(spaceshipCreateDto)
		.post()
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(spaceshipServiceImplMock,times(1)).create(Mockito.any());

	}
	
	@Test
	void SpaceshipResource_update_ReturnObject() {
		
		spaceshipTest = new Spaceship(new ObjectId(idTest), nameTest, new ObjectId(idTest));
		SpaceshipUpdateDto spaceshipUpdateDto = new SpaceshipUpdateDto(nameTest, idTest);
		doReturn(Optional.ofNullable(spaceshipTest)).when(spaceshipServiceImplMock).update(Mockito.any(), Mockito.any());
		
		SpaceshipDto updated = given()
				.contentType(ContentType.JSON)
				.body(spaceshipUpdateDto)
				.put("/{id}", idTest)
				.then()
				.statusCode(200)
		        .extract().as(SpaceshipDto.class);
		
		assertAll(
				()->Assertions.assertThat(updated).isNotNull(),
				()->Assertions.assertThat(updated.name()).isEqualTo(spaceshipUpdateDto.name())
				);		
		Mockito.verify(spaceshipServiceImplMock,times(1)).update(Mockito.any(), Mockito.any());
	}
	
	@Test
	void SpaceshipResource_update_ReturnIdError() {

		SpaceshipUpdateDto spaceshipUpdateDto = new SpaceshipUpdateDto(nameTest, idTest);
		given()
        .contentType(ContentType.JSON)
        .body(spaceshipUpdateDto)
        .put("/{id}", idTestError)                
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}
	
	@Test
	void SpaceshipResource_update_ReturnNotFound() {
				
		SpaceshipUpdateDto spaceshipUpdateDto = new SpaceshipUpdateDto(nameTest, idTest);
		doReturn(Optional.empty()).when(spaceshipServiceImplMock).update(Mockito.any(), Mockito.any());
		
		given()
		.contentType(ContentType.JSON)
		.body(spaceshipUpdateDto)
		.put("/{id}", idTest)
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(spaceshipServiceImplMock,times(1)).update(Mockito.any(), Mockito.any());
	}
	

	@Test
	void SpaceshipResource_delete_ReturnOk() {
		
		doReturn(0).when(spaceshipServiceImplMock).delete(Mockito.any());
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(200)
        .body(is(MsgStatus.ENTITY_DELETED));
		
		Mockito.verify(spaceshipServiceImplMock,times(1)).delete(Mockito.any());
	}

	@Test
	void SpaceshipResource_delete_ReturnIdError() {
		
		given()
        .delete("/{id}", idTestError)                
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}

	@Test
	void SpaceshipResource_delete_ReturnNotFound() {
		
		given()
        .delete("/{id}", "999999999999999999999999")                
		.then()
		.statusCode(404)
        .body(is(MsgStatus.ENTITY_NOT_DELETED));
		
	}
}

package dev.ime.solar_fleet.resource;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.dto.position.PositionCreateDto;
import dev.ime.solar_fleet.dto.position.PositionDto;
import dev.ime.solar_fleet.dto.position.PositionUpdateDto;
import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.service.PositionServiceImpl;
import dev.ime.solar_fleet.tool.MsgStatus;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;


@QuarkusTest
@TestHTTPEndpoint(PositionResource.class)
class PositionResourceTest {
	
	@InjectMock
	private PositionServiceImpl positionServiceImpl;
	
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "Almirante General";
	private final String idTestError = "65a909b13b3df";
	private List<Position>positions;
	private Position positionTest;
	private PositionCreateDto positionCreateDtoTest;
	private PositionUpdateDto positionUpdateDtoTest;
	private ObjectId objectIdTest;
	
	@BeforeEach
	private void createObjects() {
		positions = new ArrayList<>();
		positionTest = new Position(new ObjectId(idTest),nameTest);
		objectIdTest = new ObjectId(idTest);
		positionCreateDtoTest = new PositionCreateDto(nameTest);
		positionUpdateDtoTest = new PositionUpdateDto(nameTest);
	}
	
	@Test
	void PositionResource_getAll_ReturnEmptyList() {
		
		doReturn(positions).when(positionServiceImpl).getAll();
		
		given()
		.when()
		.get()
		.then()
		.statusCode(200)
		.body(is(MsgStatus.EMPTY_LIST));
		
		Mockito.verify(positionServiceImpl,times(1)).getAll();

	}
	@Test
	void PositionResource_getAll_ReturnList() {
		
		positions.add(positionTest);
		doReturn(positions).when(positionServiceImpl).getAll();
		
		List<Position>list = given()
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
				        .body()
				        .jsonPath()
				        .getList(".", Position.class);
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		Mockito.verify(positionServiceImpl,times(1)).getAll();

	}
	@Test
	void PositionResource_getAll_ReturnListPaged() {
		
		positions.add(positionTest);
		doReturn(positions).when(positionServiceImpl).getAllPaged(Mockito.anyInt());
		
		given()
		.queryParam("page", "1")
		.when()
		.get()
		.then()
		.statusCode(200);
		
		Mockito.verify(positionServiceImpl,times(1)).getAllPaged(Mockito.anyInt());

	}


	@Test
	void PositionResource_getById_ReturnObject() {
		
		doReturn(Optional.ofNullable(positionTest)).when(positionServiceImpl).getById(Mockito.any(ObjectId.class));
		
		PositionDto position = given()
							.when()
							.get("/{id}", objectIdTest.toString())
							.then()
							.statusCode(200)
							.extract()
							.as(PositionDto.class);
		assertAll(
				()->Assertions.assertThat(position).isNotNull(),
				()->Assertions.assertThat(position.name()).isEqualTo(nameTest)
			);
		verify(positionServiceImpl,times(1)).getById(Mockito.any(ObjectId.class));					
		
	}
	
	@Test
	void PositionResource_getById_ReturnIdError() {
		
		given()
		.when()
		.get("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
	}
	

	@Test
	void PositionResource_getById_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(positionServiceImpl).getById(Mockito.any());
		
		given()
		.when()
		.get("/{id}", idTest)
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(positionServiceImpl,times(1)).getById(Mockito.any());

	}
	
	@Test
	void PositionResource_create_ReturnObject() {
		
		doReturn(Optional.ofNullable(positionTest)).when(positionServiceImpl).create(Mockito.any(Position.class));
		
		PositionDto positionDto = given()
						.contentType(ContentType.JSON)
						.body(positionCreateDtoTest)
						.post()
						.then()
						.statusCode(201)
						.extract().as(PositionDto.class);
		assertAll(
		()->Assertions.assertThat(positionDto).isNotNull(),
		()->Assertions.assertThat(positionDto.name()).isEqualTo(positionCreateDtoTest.name())
		);
		Mockito.verify(positionServiceImpl,times(1)).create(Mockito.any(Position.class));
		
	}
	
	@Test
	void PositionResource_create_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(positionServiceImpl).create(Mockito.any(Position.class));
		
		given()
		.contentType(ContentType.JSON)
		.body(positionCreateDtoTest)
		.post()
		.then()
		.statusCode(404)
		.body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(positionServiceImpl,times(1)).create(Mockito.any(Position.class));
		
	}
	
	@Test
	void PositionResource_update_ReturnObject() {
		
		doReturn(Optional.ofNullable(positionTest)).when(positionServiceImpl).update(Mockito.any(ObjectId.class),Mockito.any(Position.class));
		
		PositionDto positionDto = given()
						.contentType(ContentType.JSON)
						.body(positionUpdateDtoTest)
						.put("/{id}", idTest)
						.then()
						.statusCode(200)
						.extract().as(PositionDto.class);
		assertAll(
		()->Assertions.assertThat(positionDto).isNotNull(),
		()->Assertions.assertThat(positionDto.name()).isEqualTo(positionUpdateDtoTest.name())
		);
		Mockito.verify(positionServiceImpl,times(1)).update(Mockito.any(ObjectId.class),Mockito.any(Position.class));
	}
	

	@Test
	void PositionResource_update_ReturnIdError() {
		
		given()
		.contentType(ContentType.JSON)
		.body(positionUpdateDtoTest)
		.put("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}
	

	@Test
	void PositionResource_update_ReturnNotFound() {
		
		doReturn(Optional.empty()).when(positionServiceImpl).update(Mockito.any(ObjectId.class),Mockito.any(Position.class));
		
		given()
		.contentType(ContentType.JSON)
		.body(positionUpdateDtoTest)
		.put("/{id}", idTest)
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(positionServiceImpl,times(1)).update(Mockito.any(ObjectId.class),Mockito.any(Position.class));
					
	}
	
	@Test
	void PositionResource_delete_ReturnIdError() {
		
		given()
        .delete("/{id}", idTestError)
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
	}	
	
	@Test
	void PositionResource_delete_ReturnOk() {
		
		doReturn(0).when(positionServiceImpl).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(200)
        .body(is(MsgStatus.ENTITY_DELETED));
		
		Mockito.verify(positionServiceImpl,times(1)).delete(Mockito.any(ObjectId.class));

	}

	@Test
	void PositionResource_delete_Return1() {
		
		doReturn(1).when(positionServiceImpl).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(404)
        .body(is(MsgStatus.ENTITY_NOT_DELETED));
		
		Mockito.verify(positionServiceImpl,times(1)).delete(Mockito.any(ObjectId.class));

	}

	@Test
	void PositionResource_delete_Return2() {
		
		doReturn(2).when(positionServiceImpl).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(404)
        .body(is(MsgStatus.ERROR_DELETE_ASSOCIATED_ITEMS));
		
		Mockito.verify(positionServiceImpl,times(1)).delete(Mockito.any(ObjectId.class));

	}

	@Test
	void PositionResource_delete_ReturnOther() {
		
		doReturn(99).when(positionServiceImpl).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(404)
        .body(is(MsgStatus.ERROR_WTF));
		
		Mockito.verify(positionServiceImpl,times(1)).delete(Mockito.any(ObjectId.class));

	}
	
}

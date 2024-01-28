package dev.ime.solar_fleet.resource;



import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.dto.shipclass.ShipClassCreateDto;
import dev.ime.solar_fleet.dto.shipclass.ShipClassDto;
import dev.ime.solar_fleet.dto.shipclass.ShipClassUpdateDto;
import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.service.ShipClassServiceImpl;
import dev.ime.solar_fleet.tool.MsgStatus;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@QuarkusTest
@TestHTTPEndpoint(ShipClassResource.class)
class ShipClassResourceTest {

	@Inject
	private ShipClassServiceImpl shipClassServiceImplMock;
	
	private List<ShipClass>shipClasses;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "Class Frigate";
	private ShipClass shipClassTest;
	private ShipClassCreateDto shipClassCreateDtoTest;
	private ShipClassUpdateDto shipClassUpdateDtoTest;	
	
	@BeforeEach
	private void createObjects() {
		shipClasses = new ArrayList<>();
		shipClassTest = new ShipClass(new ObjectId(idTest),nameTest);
		shipClassCreateDtoTest = new ShipClassCreateDto(nameTest);
		shipClassUpdateDtoTest = new ShipClassUpdateDto(nameTest);
	}
	
	@Test
	void ShipClassResource_getAll_ReturnEmptyList() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		Mockito.when(shipClassServiceImplMock.getAll()).thenReturn(shipClasses);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		
		given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .body(is(MsgStatus.EMPTY_LIST));
		
		Mockito.verify(shipClassServiceImplMock,times(1)).getAll();	
	}

	
	@Test
	void ShipClassResource_getAll_ReturnList() {
				given()
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}

	@Test
	void ShipClassResource_getAll_ReturnListPaged() {
				given()
				.queryParam("page", 1)
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}	


	@Test
	void ShipClassResource_getById_ReturnNewObject() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		doReturn(Optional.ofNullable(shipClassTest)).when(shipClassServiceImplMock).getById(Mockito.any(ObjectId.class));		
		
		ShipClassDto shipClassDto = given()
									.when()
									.get("/{id}", idTest)
									.then()
									.statusCode(200)
									.extract().as(ShipClassDto.class);
		
		assertAll(
				()->Assertions.assertThat(shipClassDto).isNotNull(),
				()->Assertions.assertThat(shipClassDto.name()).isEqualTo(shipClassTest.getName())
				);
		verify(shipClassServiceImplMock,times(1)).getById(Mockito.any(ObjectId.class));					

	}
	
	@Test
	void ShipClassResource_getById_ReturnNotFound() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		doReturn(Optional.empty()).when(shipClassServiceImplMock).getById(Mockito.any());

		given()
		.when()
		.get("/{id}", idTest)
		.then()
		.statusCode(404)
        .body(is(MsgStatus.RESOURCE_NOT_FOUND));
		
		Mockito.verify(shipClassServiceImplMock,times(1)).getById(Mockito.any());
		
	}
	
	@Test
	void ShipClassResource_getById_ReturnIdError() {
		
		given()
		.when()
		.get("/{id}","xdxdxdxd")
		.then()
		.statusCode(400)
        .body(is(MsgStatus.INVALID_OBJECTID));
		
	}
	
	@Test
	void ShipClassResource_create_ReturnObject() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		doReturn(Optional.ofNullable(shipClassTest)).when(shipClassServiceImplMock).create(Mockito.any(ShipClass.class));
		
		ShipClassDto  saved = given()
                .contentType(ContentType.JSON)
                .body(shipClassCreateDtoTest)
                .post()
                .then()
                .statusCode(201)
                .extract().as(ShipClassDto.class);
		
		assertAll(
				()->Assertions.assertThat(saved).isNotNull(),
				()->Assertions.assertThat(saved.name()).isEqualTo(shipClassCreateDtoTest.name())
				);
		Mockito.verify(shipClassServiceImplMock,times(1)).create(Mockito.any(ShipClass.class));
	
	}

	@Test
	void ShipClassResource_create_ReturnObjectVoid() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		Mockito.when(shipClassServiceImplMock.create(Mockito.any())).thenReturn(Optional.empty());
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		ShipClassCreateDto shipClassCreateDto = new ShipClassCreateDto("TestShipClass");
		
		given()
                .contentType(ContentType.JSON)
                .body(shipClassCreateDto)
                .post()
                .then()
                .statusCode(404)
                .body(is(MsgStatus.RESOURCE_NOT_FOUND));		
	}	

	@Test
	void ShipClassResource_update_ReturnObject() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		doReturn(Optional.ofNullable(shipClassTest)).when(shipClassServiceImplMock).update(Mockito.any(ObjectId.class),Mockito.any(ShipClass.class));
				
		ShipClassDto updated = given()
                .contentType(ContentType.JSON)
                .body(shipClassUpdateDtoTest)
                .put("/{id}", idTest)
                .then()
                .statusCode(200)
                .extract().as(ShipClassDto.class);
		
		assertAll(
				()->Assertions.assertThat(updated).isNotNull(),
				()->Assertions.assertThat(updated.name()).isEqualTo(shipClassUpdateDtoTest.name())
				);
		Mockito.verify(shipClassServiceImplMock,times(1)).update(Mockito.any(ObjectId.class), Mockito.any(ShipClass.class));
		
	}


	@Test
	void ShipClassResource_update_ReturnIdError() {
		
		given()
        .contentType(ContentType.JSON)
        .body(new ShipClassUpdateDto("Updated Name"))
        .put("/{id}", "lolololo")                
		.then()
		.statusCode(400);
		
	}

	@Test
	void ShipClassResource_update_ReturnNotFound() {
		
		given()
        .contentType(ContentType.JSON)
        .body(new ShipClassUpdateDto("Updated Name"))
        .put("/{id}", "65a909b13b3df39999999999")                
		.then()
		.statusCode(404);
		
	}
	

	@Test
	void ShipClassResource_delete_ReturnIdError() {
		
		given()
        .delete("/{id}", "jgjftff")                
		.then()
		.statusCode(400);
		
	}

	@Test
	void ShipClassResource_delete_ReturnNotFound() {
		
		given()
        .delete("/{id}", "65a909b13b39999999999999")                
		.then()
		.statusCode(404);
		
	}
	
	@Test
	void ShipClassResource_delete_ReturnOk() {
		
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		doReturn(0).when(shipClassServiceImplMock).delete(Mockito.any(ObjectId.class));
		
		given()
        .delete("/{id}", idTest)                
		.then()
		.statusCode(200)
        .body(is(MsgStatus.ENTITY_DELETED));
		
		Mockito.verify(shipClassServiceImplMock,times(1)).delete(Mockito.any(ObjectId.class));

	}

}

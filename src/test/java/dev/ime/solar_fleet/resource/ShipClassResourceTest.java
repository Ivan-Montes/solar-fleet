package dev.ime.solar_fleet.resource;



import org.assertj.core.api.Assertions;
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

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@QuarkusTest
@TestHTTPEndpoint(ShipClassResource.class)
class ShipClassResourceTest {

	@Inject
	private ShipClassServiceImpl shipClassServiceImplMock;
	
	@Test
	void ShipClassResource_getAll_ReturnEmptyList() {
		
		List<ShipClass>list = new ArrayList<>();
		shipClassServiceImplMock = Mockito.mock(ShipClassServiceImpl.class);
		Mockito.when(shipClassServiceImplMock.getAll()).thenReturn(list);
		QuarkusMock.installMockForType(shipClassServiceImplMock, ShipClassServiceImpl.class);
		
		given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .body(is(MsgStatus.EMPTY_LIST));
				
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
		
		ShipClassCreateDto shipClassCreateDto = new ShipClassCreateDto("TestShipClass");
		
		ShipClassDto  saved = given()
                .contentType(ContentType.JSON)
                .body(shipClassCreateDto)
                .post()
                .then()
                .statusCode(201)
                .extract().as(ShipClassDto.class);
		
		
		ShipClassDto shipClassDto = given()
									.when()
									.get("/{id}", saved.id().toString())
									.then()
									.statusCode(200)
									.extract().as(ShipClassDto.class);
		
		Assertions.assertThat(shipClassDto).isNotNull();
		Assertions.assertThat(shipClassDto.name()).isEqualTo(saved.name());
		
		given()
		.when()
		.delete("/{id}", saved.id().toString())
		.then()
		.statusCode(200);		
	}
	
	@Test
	void ShipClassResource_getById_ReturnNotFound() {
		
		given()
		.when()
		.get("/{id}","65a909b13b3df39999999999")
		.then()
		.statusCode(404);
		
	}
	
	@Test
	void ShipClassResource_getById_ReturnIdError() {
		
		given()
		.when()
		.get("/{id}","xdxdxdxd")
		.then()
		.statusCode(400);
		
	}
	
	@Test
	void ShipClassResource_create_ReturnObject() {
		
		ShipClassCreateDto shipClassCreateDto = new ShipClassCreateDto("TestShipClass");
		
		ShipClassDto  saved = given()
                .contentType(ContentType.JSON)
                .body(shipClassCreateDto)
                .post()
                .then()
                .statusCode(201)
                .extract().as(ShipClassDto.class);
		
		Assertions.assertThat(saved).isNotNull();
		Assertions.assertThat(saved.name()).isEqualTo(shipClassCreateDto.name());
		
		given()
		.when()
		.delete("/{id}", saved.id().toString())
		.then()
		.statusCode(200);	
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
		
		ShipClassCreateDto shipClassCreateDto = new ShipClassCreateDto("TestShipClass Name");
		ShipClassUpdateDto shipClassUpdateDto = new ShipClassUpdateDto("Updated Name");
		ShipClassDto  saved = given()
                .contentType(ContentType.JSON)
                .body(shipClassCreateDto)
                .post()
                .then()
                .statusCode(201)
                .extract().as(ShipClassDto.class);
		
		ShipClassDto updated = given()
                .contentType(ContentType.JSON)
                .body(shipClassUpdateDto)
                .put("/{id}", saved.id().toString())
                .then()
                .statusCode(200)
                .extract().as(ShipClassDto.class);		
		
		Assertions.assertThat(updated).isNotNull();
		Assertions.assertThat(updated.name()).isEqualTo(shipClassUpdateDto.name());
		
		given()
		.when()
		.delete("/{id}", saved.id().toString())
		.then()
		.statusCode(200);
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
}

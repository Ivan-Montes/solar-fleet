package dev.ime.solar_fleet.resource;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.ime.solar_fleet.dto.shipclass.ShipClassDto;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
@TestHTTPEndpoint(ShipClassResource.class)
class ShipClassResourceTest {

	@Test
	void ShipClassResource_getAll_ReturnList() {
				given()
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}
	
	@Test
	void ShipClassResource_getById_ReturnObject() {
		
		ShipClassDto shipClassDto = given()
									.when()
									.get("/{id}","65a909b13b3df36e11df2de3")
									.then()
									.statusCode(200)
									.extract().as(ShipClassDto.class);
		
		Assertions.assertThat(shipClassDto).isNotNull();
		
	}

}

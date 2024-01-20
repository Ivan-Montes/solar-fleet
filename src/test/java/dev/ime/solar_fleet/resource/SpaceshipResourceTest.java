package dev.ime.solar_fleet.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;



@QuarkusTest
@TestHTTPEndpoint(SpaceshipResource.class)
class SpaceshipResourceTest {

	@Test
	void SpaceshipResource_getAll_ReturnList() {
				given()
		        .when()
		        .get()
		        .then()
		        .statusCode(200);
	}

}

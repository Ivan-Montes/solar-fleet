package dev.ime.solar_fleet;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
	    info = @Info(
	        title="Solar-fleet API",
	        version = "1.0.0",
	        contact = @Contact(
	            name = "Ivan",
	            url = "https://www.github.com/ivan-montes",
	            email = "ivan@github.com"),
	        license = @License(
	            name = "GPL 3",
	            url = "https://choosealicense.com/licenses/gpl-3.0/"))
	)
public class OpenApiInfoApplication  extends Application {

}

package dev.ime.solar_fleet.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import org.bson.types.ObjectId;

import dev.ime.solar_fleet.tool.RegexPattern;

@RegisterForReflection
@MongoEntity(database = "solarfleetdb", collection= "spaceship")
public class Spaceship {

	@NotNull(message="{NotNull.Entity.id}")
	private ObjectId id;

	@NotEmpty(message="{NotEmpty.Entity.field]")
	@Size(min = 1, max = 50, message="{Size.Entity.name}")
	@Pattern( regexp = RegexPattern.NAME_BASIC, message="{Pattern.Entity.name}")
	private String name;

	@NotNull(message="{NotNull.Entity.id}")
	private ObjectId shipClassId;

	public Spaceship() {
		super();		
	}	
	
	public Spaceship(ObjectId id, String name, ObjectId shipClassId) {
		super();
		this.id = id;
		this.name = name;
		this.shipClassId = shipClassId;
	}


	public ObjectId getShipClassId() {
		return shipClassId;
	}

	public void setShipClassId(ObjectId shipClassId) {
		this.shipClassId = shipClassId;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, shipClassId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Spaceship other = (Spaceship) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(shipClassId, other.shipClassId);
	}

	@Override
	public String toString() {
		return "Spaceship [id=" + id + ", name=" + name + ", shipClassId=" + shipClassId + "]";
	}
	
}

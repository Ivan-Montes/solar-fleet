package dev.ime.solar_fleet.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.Objects;

import org.bson.types.ObjectId;

@MongoEntity
public class Spaceship {
	
	private ObjectId id;
	
	private String name;
	
	private Long shipClass;

	public Spaceship() {
		super();		
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

	public Long getShipClass() {
		return shipClass;
	}

	public void setShipClass(Long shipClass) {
		this.shipClass = shipClass;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, shipClass);
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
				&& Objects.equals(shipClass, other.shipClass);
	}

	@Override
	public String toString() {
		return "Spaceship [id=" + id + ", name=" + name + ", shipClass=" + shipClass + "]";
	}
	
	
	
}

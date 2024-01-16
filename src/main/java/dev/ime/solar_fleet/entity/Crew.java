package dev.ime.solar_fleet.entity;

import java.util.Objects;
import org.bson.types.ObjectId;
import io.quarkus.mongodb.panache.common.MongoEntity;


@MongoEntity(database = "solarfleetdb", collection= "crew")
public class Crew {

	private ObjectId id;
	
	private String name;
	
	private String lastname;
	
	private ObjectId spaceshipId;
	
	private ObjectId positionId;

	public Crew() {
		super();
	}

	public Crew(ObjectId id, String name, String lastname, ObjectId spaceshipId, ObjectId positionId) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.spaceshipId = spaceshipId;
		this.positionId = positionId;
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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public ObjectId getSpaceshipId() {
		return spaceshipId;
	}

	public void setSpaceshipId(ObjectId spaceshipId) {
		this.spaceshipId = spaceshipId;
	}

	public ObjectId getPositionId() {
		return positionId;
	}

	public void setPositionId(ObjectId positionId) {
		this.positionId = positionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, lastname, name, positionId, spaceshipId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Crew other = (Crew) obj;
		return Objects.equals(id, other.id) && Objects.equals(lastname, other.lastname)
				&& Objects.equals(name, other.name) && Objects.equals(positionId, other.positionId)
				&& Objects.equals(spaceshipId, other.spaceshipId);
	}

	@Override
	public String toString() {
		return "Crew [id=" + id + ", name=" + name + ", lastname=" + lastname + ", spaceshipId=" + spaceshipId
				+ ", positionId=" + positionId + "]";
	}
	
}

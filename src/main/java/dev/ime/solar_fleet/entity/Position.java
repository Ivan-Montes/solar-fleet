package dev.ime.solar_fleet.entity;

import java.util.Objects;
import org.bson.types.ObjectId;

import dev.ime.solar_fleet.tool.RegexPattern;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@RegisterForReflection
@MongoEntity( collection= "position" )
public class Position {

	@NotNull(message="{NotNull.Entity.id}")
	private ObjectId id;

	@NotEmpty(message="{NotEmpty.Entity.field]")
	@Size(min = 1, max = 50, message="{Size.Entity.name}")
	@Pattern( regexp = RegexPattern.NAME_BASIC, message="{Pattern.Entity.name}")
	private String name;

	public Position() {
		super();
	}

	public Position(ObjectId id, String name) {
		super();
		this.id = id;
		this.name = name;
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
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Position [id=" + id + ", name=" + name + "]";
	}
	
	

}

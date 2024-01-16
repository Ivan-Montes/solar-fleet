package dev.ime.solar_fleet.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

public interface GenericService<T> {

	List<T>getAll();
	Optional<T> getById(ObjectId id);
	Optional<T> create(T entity);
	Optional<T> update(ObjectId id, T entity);
	int delete(ObjectId id);
	
}

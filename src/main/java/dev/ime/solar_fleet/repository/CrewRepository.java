package dev.ime.solar_fleet.repository;

import dev.ime.solar_fleet.entity.Crew;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CrewRepository implements PanacheMongoRepository<Crew> {

}

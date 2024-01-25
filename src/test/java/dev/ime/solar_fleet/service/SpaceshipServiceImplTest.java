package dev.ime.solar_fleet.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import dev.ime.solar_fleet.tool.Checker;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;


@QuarkusTest
class SpaceshipServiceImplTest {
	
	@Inject
	private SpaceshipServiceImpl spaceshipServiceImpl;
	@InjectMock
	private SpaceshipRepository spaceshipRepositoryMock;
	@InjectMock
	private ShipClassRepository shipClassRepositoryMock;
	@InjectMock
	private Checker checkerMock;
	
	private List<Spaceship>spaceships;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "SSV Normandy";
	private Spaceship spaceshipTest;
	private ObjectId objectIdTest;
	
	@BeforeEach
	private void createObjects() {
		spaceships = new ArrayList<>();
		spaceshipTest = new Spaceship(new ObjectId(idTest),nameTest, new ObjectId(idTest));
		objectIdTest = new ObjectId(idTest);
	}
	
	@Test
	void SpaceshipServiceImpl_getAll_ReturnList() {		
		spaceships.add(spaceshipTest);
		doReturn(spaceships).when(spaceshipRepositoryMock).listAll();
		
		List<Spaceship>list = spaceshipServiceImpl.getAll();
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		verify(spaceshipRepositoryMock,times(1)).listAll();
	}

	@Test
	void SpaceshipServiceImpl_getById_ReturnObject() {
		
		doReturn(spaceshipTest).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Spaceship> opt = spaceshipServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	
}

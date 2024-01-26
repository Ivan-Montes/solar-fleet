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
	private ShipClass shipClassTest;
	
	@BeforeEach
	private void createObjects() {
		
		spaceships = new ArrayList<>();
		spaceshipTest = new Spaceship(new ObjectId(idTest),nameTest, new ObjectId(idTest));
		objectIdTest = new ObjectId(idTest);
		shipClassTest = new ShipClass(new ObjectId(idTest),nameTest);

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
	

	@Test
	void SpaceshipServiceImpl_getById_ReturnNull() {
		
		doReturn(null).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Spaceship> opt = spaceshipServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void SpaceshipServiceImpl_create_ReturnObject() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(spaceshipRepositoryMock).persist(Mockito.any(Spaceship.class));

		Optional<Spaceship> opt = spaceshipServiceImpl.create(spaceshipTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(checkerMock,times(1)).checkObjectId(Mockito.any(ObjectId.class));
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(spaceshipRepositoryMock,times(1)).persist(Mockito.any(Spaceship.class));
	}
	

	@Test
	void SpaceshipServiceImpl_create_ReturnEmptyForBadShipClassId() {
		
		doReturn(false).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));

		Optional<Spaceship> opt = spaceshipServiceImpl.create(spaceshipTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(checkerMock,times(1)).checkObjectId(Mockito.any(ObjectId.class));
	}
	

	@Test
	void SpaceshipServiceImpl_create_ReturnEmptyForNotShipClassId() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(null).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));

		Optional<Spaceship> opt = spaceshipServiceImpl.create(spaceshipTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(checkerMock,times(1)).checkObjectId(Mockito.any(ObjectId.class));
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void SpaceshipServiceImpl_update_ReturnObject() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(spaceshipTest).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(spaceshipRepositoryMock).persistOrUpdate(Mockito.any(Spaceship.class));

		Optional<Spaceship> opt = spaceshipServiceImpl.update(objectIdTest, spaceshipTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(checkerMock,times(2)).checkObjectId(Mockito.any(ObjectId.class));
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(spaceshipRepositoryMock,times(1)).persistOrUpdate(Mockito.any(Spaceship.class));
	}
	
	@Test
	void SpaceshipServiceImpl_update_ReturnEmptyBadObjectId() {
		
		doReturn(false).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		
		Optional<Spaceship> opt = spaceshipServiceImpl.update(objectIdTest,spaceshipTest);

		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);
		verify(checkerMock,times(1)).checkObjectId(Mockito.any(ObjectId.class));
	}
	

	@Test
	void SpaceshipServiceImpl_update_ReturnEmptyForSpaceshipNotFound() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(null).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));

		Optional<Spaceship> opt = spaceshipServiceImpl.update(objectIdTest, spaceshipTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);
		verify(checkerMock,times(2)).checkObjectId(Mockito.any(ObjectId.class));
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void SpaceshipServiceImpl_delete_ReturnInt() {
		
		doReturn(true).when(spaceshipRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = spaceshipServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isZero();
		verify(spaceshipRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void SpaceshipServiceImpl_delete_ReturnIntFalse() {
		
		doReturn(false).when(spaceshipRepositoryMock).deleteById(Mockito.any(ObjectId.class));

		int returnValue = spaceshipServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		verify(spaceshipRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}
}

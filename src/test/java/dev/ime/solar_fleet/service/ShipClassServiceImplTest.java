package dev.ime.solar_fleet.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.entity.ShipClass;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class ShipClassServiceImplTest {

	@Inject
	private ShipClassServiceImpl shipClassServiceImpl;
	@InjectMock
	private SpaceshipRepository spaceshipRepositoryMock;
	@InjectMock
	private ShipClassRepository shipClassRepositoryMock;	
	
	private List<ShipClass>shipclasses;
	private List<Spaceship>spaceships;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "Class Frigate";
	private ShipClass shipClassTest;
	private ObjectId objectIdTest;
	private Spaceship spaceshipTest;
	
	@BeforeEach
	private void createObjects() {
		shipclasses = new ArrayList<>();
		spaceships = new ArrayList<>();
		shipClassTest = new ShipClass(new ObjectId(idTest),nameTest);
		objectIdTest = new ObjectId(idTest);
		spaceshipTest = new Spaceship(ObjectId.get(),nameTest,ObjectId.get());
	}
	
	@Test
	void ShipClassServiceImpl_getAll_ReturnList() {		
		
		shipclasses.add(shipClassTest);
		doReturn(shipclasses).when(shipClassRepositoryMock).listAll();
		
		List<ShipClass>list = shipClassServiceImpl.getAll();
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		verify(shipClassRepositoryMock,times(1)).listAll();
	}

	@Test
	void ShipClassServiceImpl_getById_ReturnObject() {
		
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<ShipClass> opt = shipClassServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}

	@Test
	void ShipClassServiceImpl_getById_ReturnNull() {
		
		doReturn(null).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<ShipClass> opt = shipClassServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);		
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void ShipClassServiceImpl_create_ReturnObject() {
		
		doNothing().when(shipClassRepositoryMock).persist(Mockito.any(ShipClass.class));
		
		Optional<ShipClass> opt = shipClassServiceImpl.create(shipClassTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(shipClassRepositoryMock,times(1)).persist(Mockito.any(ShipClass.class));
	}
	
	@Test
	void ShipClassServiceImpl_update_ReturnObject() {
		
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(shipClassRepositoryMock).persistOrUpdate(Mockito.any(ShipClass.class));
		
		Optional<ShipClass> opt = shipClassServiceImpl.update(objectIdTest, shipClassTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);		
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(shipClassRepositoryMock,times(1)).persistOrUpdate(Mockito.any(ShipClass.class));
	}
	
	@Test
	void ShipClassServiceImpl_update_ReturnNull() {
		
		doReturn(null).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<ShipClass> opt = shipClassServiceImpl.update(objectIdTest, shipClassTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);		
		verify(shipClassRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}


	@Test
	void ShipClassServiceImpl_delete_ReturnIntOk() {
		
		doReturn(Collections.emptyList()).when(spaceshipRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));
		doReturn(true).when(shipClassRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isZero();
		verify(spaceshipRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));
		verify(shipClassRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));

	}
	
	@Test
	void ShipClassServiceImpl_delete_ReturnIntFail() {		
		
		doReturn(Collections.emptyList()).when(spaceshipRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));
		doReturn(false).when(shipClassRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		verify(spaceshipRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));
		verify(shipClassRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));

	}

	@Test
	void ShipClassServiceImpl_delete_ReturnIntNotEmptyList() {		
		
		spaceships.add(spaceshipTest);
		doReturn(spaceships).when(spaceshipRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));

		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(2);
		verify(spaceshipRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));

	}
	
}

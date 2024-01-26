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
import dev.ime.solar_fleet.repository.ShipClassRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import dev.ime.solar_fleet.tool.Checker;
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
	@InjectMock
	private Checker checkerMock;
	
	private List<ShipClass>shipclasses;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "Class Frigate";
	private ShipClass shipClassTest;
	private ObjectId objectIdTest;
	
	@BeforeEach
	private void createObjects() {
		shipclasses = new ArrayList<>();
		shipClassTest = new ShipClass(new ObjectId(idTest),nameTest);
		objectIdTest = new ObjectId(idTest);
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
	void ShipClassServiceImpl_delete_ReturnInt() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(shipClassTest).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(Collections.emptyList()).when(spaceshipRepositoryMock).list(Mockito.anyString(), Mockito.any(ObjectId.class));
		doReturn(true).when(shipClassRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isZero();
		
	}
	
	@Test
	void ShipClassServiceImpl_delete_ReturnIntNotFound() {
		
		doReturn(true).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		doReturn(null).when(shipClassRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		
	}
	
	@Test
	void ShipClassServiceImpl_delete_ReturnIntBadObjectId() {
		
		doReturn(false).when(checkerMock).checkObjectId(Mockito.any(ObjectId.class));
		
		int returnValue = shipClassServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		
	}
}

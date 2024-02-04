package dev.ime.solar_fleet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.entity.Spaceship;
import dev.ime.solar_fleet.repository.CrewRepository;
import dev.ime.solar_fleet.repository.PositionRepository;
import dev.ime.solar_fleet.repository.SpaceshipRepository;
import dev.ime.solar_fleet.tool.Checker;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;


@QuarkusTest
class CrewServiceImplTest {

	@Inject
	private CrewServiceImpl crewServiceImpl;

	@InjectMock
	private  CrewRepository crewRepositoryMock;
	
	@InjectMock
	private SpaceshipRepository spaceshipRepositoryMock;
	
	@InjectMock
	private PositionRepository positionRepositoryMock;	

	@InjectMock
	private Checker checkerMock;
	
	private List<Crew>crewList;
	private final String nameTest = "Zapp";
	private final String lastnameTest = "Brannigan";
	private Crew crewTest;
	private ObjectId objectIdTest;
	
	@BeforeEach
	private void createObjects() {

		objectIdTest = ObjectId.get();
		crewList = new ArrayList<>();
		crewTest = new Crew(objectIdTest, nameTest, lastnameTest, ObjectId.get(), ObjectId.get());
	}
	
	@Test
	void CrewServiceImpl_getAll_ReturnList() {	
		
		crewList.add(crewTest);
		doReturn(crewList).when(crewRepositoryMock).listAll();
		
		List<Crew>list = crewServiceImpl.getAll();
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		verify(crewRepositoryMock, times(1)).listAll();
	}
	

	@Test
	void CrewServiceImpl_getById_ReturnObject() {
		
		doReturn(crewTest).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Crew> opt = crewServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest),
				()->Assertions.assertThat(opt.get().getLastname()).isEqualTo(lastnameTest)
			);	
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void CrewServiceImpl_getById_ReturnNull() {
		
		doReturn(null).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Crew> opt = crewServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void CrewServiceImpl_create_ReturnErrorForSpaceshipNull() {
		
		doReturn(null).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Crew> opt = crewServiceImpl.create(crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void CrewServiceImpl_create_ReturnErrorForBothNull() {
		
		crewTest.setPositionId(null);
		crewTest.setSpaceshipId(null);
		doNothing().when(crewRepositoryMock).persist(Mockito.any(Crew.class));

		Optional<Crew> opt = crewServiceImpl.create(crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest),
				()->Assertions.assertThat(opt.get().getLastname()).isEqualTo(lastnameTest)
			);	
		verify(crewRepositoryMock,times(1)).persist(Mockito.any(Crew.class));
	}

	@Test
	void CrewServiceImpl_create_ReturnErrorForPositionNull() {
		
		crewTest.setSpaceshipId(null);
		doReturn(null).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Crew> opt = crewServiceImpl.create(crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void CrewServiceImpl_create_ReturnErrorForPositionObject() {
		
		doReturn(new Spaceship()).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(new Position()).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(crewRepositoryMock).persist(Mockito.any(Crew.class));

		Optional<Crew> opt = crewServiceImpl.create(crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest),
				()->Assertions.assertThat(opt.get().getLastname()).isEqualTo(lastnameTest)
			);	
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(crewRepositoryMock,times(1)).persist(Mockito.any(Crew.class));
	}
	
	@Test
	void CrewServiceImpl_update_ReturnErrorIdNotFound() {
		
		doReturn(null).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Crew> opt = crewServiceImpl.update(objectIdTest, crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));

	}	

	@Test
	void CrewServiceImpl_update_ReturnErrorForSpaceBad() {
		
		doReturn(crewTest).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(null).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));

		Optional<Crew> opt = crewServiceImpl.update(objectIdTest, crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));

	}
	
	@Test
	void CrewServiceImpl_update_ReturnErrorForPositionBad() {
		
		doReturn(crewTest).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(new Spaceship()).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(null).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));

		Optional<Crew> opt = crewServiceImpl.update(objectIdTest, crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));

	}
	
	@Test
	void CrewServiceImpl_update_ReturnObjectForOk() {
		
		doReturn(crewTest).when(crewRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(new Spaceship()).when(spaceshipRepositoryMock).findById(Mockito.any(ObjectId.class));
		doReturn(new Position()).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(crewRepositoryMock).persistOrUpdate(Mockito.any(Crew.class));

		Optional<Crew> opt = crewServiceImpl.update(objectIdTest, crewTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest),
				()->Assertions.assertThat(opt.get().getLastname()).isEqualTo(lastnameTest)
			);
		verify(crewRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(spaceshipRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(crewRepositoryMock,times(1)).persistOrUpdate(Mockito.any(Crew.class));

	}
	
	@Test
	void CrewServiceImpl_delete_ReturnIntForOk() {
		
		doReturn(true).when(crewRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = crewServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isZero();
		verify(crewRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}

	@Test
	void CrewServiceImpl_delete_ReturnIntForFalse() {
		
		doReturn(false).when(crewRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = crewServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		verify(crewRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}
}

package dev.ime.solar_fleet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import dev.ime.solar_fleet.entity.Crew;
import dev.ime.solar_fleet.entity.Position;
import dev.ime.solar_fleet.repository.CrewRepository;
import dev.ime.solar_fleet.repository.PositionRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;



@QuarkusTest
class PositionServiceImplTest {

	@Inject
	private PositionServiceImpl positionServiceImpl;
	
	@InjectMock
	private PositionRepository positionRepositoryMock;
	
	@InjectMock
	private CrewRepository crewRepositoryMock;
	
	@Mock
	private PanacheQuery<Position> panacheQuery;
	
	private List<Position> positions;
	private List<Crew>crewList;
	private final String idTest = "65a909b13b3df36e11df2de9";
	private final String nameTest = "Sargento";
	private Position positionTest;
	private ObjectId objectIdTest;
	
	@SuppressWarnings("unchecked")
	@BeforeEach
	private void createObjects() {
		panacheQuery = Mockito.mock(PanacheQuery.class);
		positions  = new ArrayList<>();
		crewList =  new ArrayList<>();
		objectIdTest = new ObjectId(idTest);
		positionTest = new Position(new ObjectId(idTest), nameTest);
		
	}	
	
	@Test
	void PositionServiceImpl_getAll_ReturnList() {		

		positions.add(positionTest);
		doReturn(positions).when(positionRepositoryMock).listAll();
		List<Position>list = positionServiceImpl.getAll();
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		verify(positionRepositoryMock,times(1)).listAll();
	}
	

	@Test
	void PositionServiceImpl_getAllPaged_ReturnList() {	

		positions.add(positionTest);
		Mockito.when(positionRepositoryMock.findAll()).thenReturn(panacheQuery);
		Mockito.when(panacheQuery.page(Mockito.anyInt(), Mockito.anyInt())).thenReturn(panacheQuery);
		Mockito.when(panacheQuery.list()).thenReturn(positions);

		List<Position>list = positionServiceImpl.getAllPaged(1);
		
		assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1)
				);
		verify(panacheQuery,times(1)).page(Mockito.anyInt(), Mockito.anyInt());
		verify(panacheQuery,times(1)).list();
		verify(positionRepositoryMock,times(1)).findAll();
	}

	@Test
	void PositionServiceImpl_getById_ReturnObject() {
		
		doReturn(positionTest).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Position> opt = positionServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}

	@Test
	void PositionServiceImpl_getById_ReturnNull() {
		
		doReturn(null).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		
		Optional<Position> opt = positionServiceImpl.getById(objectIdTest);
				
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);	
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	}
	
	@Test
	void PositionServiceImpl_create_ReturnObject() {
		
		doNothing().when(positionRepositoryMock).persist(Mockito.any(Position.class));
		
		Optional<Position> opt = positionServiceImpl.create(positionTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);	
		verify(positionRepositoryMock,times(1)).persist(Mockito.any(Position.class));
	
	}
	
	@Test
	void PositionServiceImpl_update_ReturnObject() {
		
		doReturn(positionTest).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));
		doNothing().when(positionRepositoryMock).persistOrUpdate(Mockito.any(Position.class));

		Optional<Position> opt = positionServiceImpl.update(objectIdTest,positionTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt.get().getName()).isEqualTo(nameTest)
			);		
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
		verify(positionRepositoryMock,times(1)).persistOrUpdate(Mockito.any(Position.class));
	
	}
	
	@Test
	void PositionServiceImpl_update_ReturnNull() {
		
		doReturn(null).when(positionRepositoryMock).findById(Mockito.any(ObjectId.class));

		Optional<Position> opt = positionServiceImpl.update(objectIdTest,positionTest);
		
		assertAll(
				()->Assertions.assertThat(opt).isNotNull(),
				()->Assertions.assertThat(opt).isEmpty()
			);		
		verify(positionRepositoryMock,times(1)).findById(Mockito.any(ObjectId.class));
	
	}
	
	@Test
	void PositionServiceImpl_delete_ReturnIntOk() {
		
		doReturn(Collections.emptyList()).when(crewRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));
		doReturn(true).when(positionRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = positionServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isZero();
		verify(crewRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));
		verify(positionRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void PositionServiceImpl_delete_ReturnIntFail() {
		
		doReturn(Collections.emptyList()).when(crewRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));
		doReturn(false).when(positionRepositoryMock).deleteById(Mockito.any(ObjectId.class));
		
		int returnValue = positionServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(1);
		verify(crewRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));
		verify(positionRepositoryMock,times(1)).deleteById(Mockito.any(ObjectId.class));
	}
	

	@Test
	void PositionServiceImpl_delete_ReturnIntNotEmptyList() {
		
		crewList.add(new Crew());
		doReturn(crewList).when(crewRepositoryMock).list(Mockito.anyString(), Mockito.any(Object[].class));
		
		int returnValue = positionServiceImpl.delete(objectIdTest);
		
		Assertions.assertThat(returnValue).isEqualTo(2);
		verify(crewRepositoryMock,times(1)).list(Mockito.anyString(), Mockito.any(Object[].class));
	}
}

package dev.ime.solar_fleet.tool;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
class CheckerTest {

	private Checker checker;
	
	@BeforeEach
	private void createObjects() {
		checker = new Checker();
	}
	
	@Test
	void Checker_checkPage_ReturnFalseForNull() {
		
		boolean result = checker.checkPage(null);
		
		assertAll(
				()->Assertions.assertThat(result).isFalse()
				);
		
	}

	@Test
	void Checker_checkPage_ReturnFalseForBadString() {
		
		boolean result = checker.checkPage("badibi");
		
		assertAll(
				()->Assertions.assertThat(result).isFalse()
				);
		
	}

	@Test
	void Checker_checkStringForValidObjectId_ReturnFalseForNull() {
		
		boolean result = checker.checkStringForValidObjectId(null);
		
		assertAll(
				()->Assertions.assertThat(result).isFalse()
				);
		
	}
	

	@Test
	void Checker_checkStringForValidObjectId_ReturnFalseBadString() {
		
		boolean result = checker.checkStringForValidObjectId("bu");
		
		assertAll(
				()->Assertions.assertThat(result).isFalse()
				);
		
	}
}

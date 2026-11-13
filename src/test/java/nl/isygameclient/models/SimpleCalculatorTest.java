package nl.isygameclient.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpleCalculatorTest {
	@Test
	void twoPlusTwoShouldEqualFour() {
		var calculator = new SimpleCalculator();
		assertEquals(4, calculator.add(2, 2));
	}

	@Test
	void ThreePlusSevenShouldEqualTen() {
		var calculator = new SimpleCalculator();
		assertEquals(10, calculator.add(3, 7));
	}
}
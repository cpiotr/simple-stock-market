package pl.ciruk.stockmarket.math;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DecimalsTest {

	@Test
	public void shouldDenoteZero() throws Exception {
		assertTrue(Decimals.isZero(BigDecimal.ZERO));
	}

	@Test
	public void shouldNotDenoteZero() throws Exception {
		assertFalse(Decimals.isZero(BigDecimal.valueOf(1e-10)));
	}

	@Test
	public void shouldCalculateSquareRoot() throws Exception {
		// Given
		BigDecimal decimal = new BigDecimal(49);

		// When
		BigDecimal root = Decimals.nthRoot(2, decimal);

		// Then
		assertThat(root, is(equalTo(
				Decimals.applyDefaultScaleTo(new BigDecimal(7)))));
	}

	@Test
	public void shouldCalculateCubicRoot() throws Exception {
		// Given
		BigDecimal decimal = new BigDecimal(729);

		// When
		BigDecimal root = Decimals.nthRoot(3, decimal);

		// Then
		assertThat(root, is(equalTo(
				Decimals.applyDefaultScaleTo(new BigDecimal(9)))));
	}

	@Test
	public void shouldCalculateNthRoot() throws Exception {
		// Given
		BigDecimal decimal = new BigDecimal(4782969);

		// When
		BigDecimal root = Decimals.nthRoot(14, decimal);

		// Then
		assertThat(root, is(equalTo(
				Decimals.applyDefaultScaleTo(new BigDecimal(3)))));
	}
}
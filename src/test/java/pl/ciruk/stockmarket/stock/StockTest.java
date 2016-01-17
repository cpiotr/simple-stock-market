package pl.ciruk.stockmarket.stock;

import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.ciruk.stockmarket.math.Decimals.applyDefaultScaleTo;

public class StockTest {

	@Test
	public void shouldCallDividendYieldCalculationDuringPERatioCalculation() throws Exception {
		// Given
		Stock stock = mockStock();

		// When
		stock.calculatePriceEarningsRatioFor(BigDecimal.ONE);

		// Then
		verify(stock).calculateDividendYieldFor(applyDefaultScaleTo(BigDecimal.ONE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPriceIsEmpty() throws Exception {
		// Given
		Stock stock = mockStock();
		BigDecimal emptyPrice = null;

		// When
		stock.calculatePriceEarningsRatioFor(emptyPrice);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailWhenDividendIsZero() throws Exception {
		// Given
		Stock stock = mockStock();
		when(stock.calculateDividendYieldFor(any())).thenReturn(BigDecimal.ZERO);

		// When
		stock.calculatePriceEarningsRatioFor(BigDecimal.ZERO);
	}

	private Stock mockStock() {
		Stock stock = Mockito.mock(Stock.class);
		when(stock.calculatePriceEarningsRatioFor(any(BigDecimal.class))).thenCallRealMethod();
		when(stock.calculateDividendYieldFor(any(BigDecimal.class))).thenReturn(BigDecimal.ONE);
		return stock;
	}
}
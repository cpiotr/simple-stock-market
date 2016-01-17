package pl.ciruk.stockmarket.stock;

import org.junit.Before;
import org.junit.Test;
import pl.ciruk.stockmarket.math.Decimals;

import java.math.BigDecimal;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.ciruk.stockmarket.math.Decimals.applyDefaultScaleTo;
import static pl.ciruk.stockmarket.stock.Stocks.samplePreferredStock;
import static pl.ciruk.stockmarket.stock.Stocks.symbol;

public class PreferredStockTest {

	private Random random;

	@Before
	public void setUp() throws Exception {
		random = new Random();
	}

	@Test
	public void shouldCalculateDividendYield() throws Exception {
		// Given
		PreferredStock stock = samplePreferredStock();
		BigDecimal priceInPennies = BigDecimal.valueOf(random.nextDouble());

		// When
		BigDecimal dividendYield = stock.calculateDividendYieldFor(priceInPennies);

		// Then
		assertThat(dividendYield, is(
				Decimals.divide(
						applyDefaultScaleTo(stock.getFixedDividend())
								.multiply(applyDefaultScaleTo(stock.getParValueInPennies())),
						applyDefaultScaleTo(priceInPennies))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCalculationForEmptyPrice() throws Exception {
		// Given
		PreferredStock stock = samplePreferredStock();
		BigDecimal emptyPrice = null;

		// When
		stock.calculateDividendYieldFor(emptyPrice);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCalculationWhenPriceIsZero() throws Exception {
		// Given
		PreferredStock stock = samplePreferredStock();

		// When
		stock.calculateDividendYieldFor(BigDecimal.ZERO);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailCalculationWhenFixedDividendIsMissing() throws Exception {
		// Given
		BigDecimal emptyFixedDividend = null;
		PreferredStock stock = new PreferredStock(symbol(), BigDecimal.ONE, emptyFixedDividend, null);

		// When
		stock.calculateDividendYieldFor(BigDecimal.ONE);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailCalculationWhenParValueIsMissing() throws Exception {
		// Given
		BigDecimal emptyParValue = null;
		PreferredStock stock = new PreferredStock(symbol(), emptyParValue, BigDecimal.ONE, null);

		// When
		stock.calculateDividendYieldFor(BigDecimal.ONE);
	}
}
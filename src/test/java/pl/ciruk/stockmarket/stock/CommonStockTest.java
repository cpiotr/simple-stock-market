package pl.ciruk.stockmarket.stock;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.ciruk.stockmarket.math.Decimals.applyDefaultScaleTo;

public class CommonStockTest {

	private Random random;

	@Before
	public void setUp() throws Exception {
		random = new Random();
	}

	@Test
	public void shouldCalculateDividendYield() throws Exception {
		// Given
		CommonStock stock = Stocks.sampleCommonStock();
		BigDecimal price = BigDecimal.valueOf(random.nextDouble());

		// When
		BigDecimal dividendYield = stock.calculateDividendYieldFor(price);

		// Then
		assertThat(dividendYield, is(equalTo(
				applyDefaultScaleTo(stock.getLastDividendInPennies())
						.divide(applyDefaultScaleTo(price), RoundingMode.HALF_UP))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPriceIsEmpty() throws Exception {
		// Given
		CommonStock stock = Stocks.sampleCommonStock();
		BigDecimal emptyPrice = null;

		// When
		stock.calculateDividendYieldFor(emptyPrice);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenPriceIsZero() throws Exception {
		// Given
		CommonStock stock = Stocks.sampleCommonStock();

		// When
		stock.calculateDividendYieldFor(BigDecimal.ZERO);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailWhenLastDividendIsEmpty() throws Exception {
		// Given
		CommonStock stock = new CommonStock(Stocks.symbol(), BigDecimal.ONE, null);
		BigDecimal price = BigDecimal.ONE;

		// When
		stock.calculateDividendYieldFor(price);
	}
}
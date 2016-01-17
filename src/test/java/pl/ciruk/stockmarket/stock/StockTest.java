package pl.ciruk.stockmarket.stock;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.mockito.Mockito;
import pl.ciruk.stockmarket.trade.Trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

	@Test
	public void shouldStoreTradesInOrder() throws Exception {
		// Given
		Stock stock = sampleStock();
		Trade firstTrade = Trade.builder().build();
		ensureDifferentTimestamp();
		Trade secondTrade = Trade.builder().build();
		ensureDifferentTimestamp();
		Trade thirdTrade = Trade.builder().build();
		ensureDifferentTimestamp();

		// When
		stock.record(firstTrade);
		stock.record(secondTrade);
		stock.record(thirdTrade);

		// Then
		List<Trade> tradeList = stock.streamOfTrades().collect(toList());
		assertThat(tradeList, is(equalTo(Lists.newArrayList(thirdTrade, secondTrade, firstTrade))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailRecordingEmptyTrade() throws Exception {
		// Given
		Stock stock = sampleStock();
		Trade emptyTrade = null;

		// When
		stock.record(emptyTrade);
	}

	private void ensureDifferentTimestamp() throws InterruptedException {
		TimeUnit.MILLISECONDS.sleep(100);
	}

	private Stock sampleStock() {
		return new Stock(Stocks.symbol(), BigDecimal.ONE) {
				@Override
				public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
					return BigDecimal.ONE;
				}
			};
	}

	private Stock mockStock() {
		Stock stock = Mockito.mock(Stock.class);
		when(stock.calculatePriceEarningsRatioFor(any(BigDecimal.class))).thenCallRealMethod();
		when(stock.calculateDividendYieldFor(any(BigDecimal.class))).thenReturn(BigDecimal.ONE);
		return stock;
	}
}
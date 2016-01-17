package pl.ciruk.stockmarket.stock;

import com.google.common.collect.Lists;
import org.junit.Test;
import pl.ciruk.stockmarket.trade.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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

	@Test
	public void shouldCalculateWeightedPriceWhenNoTradesWereRecorded() throws Exception {
		// Given
		Stock stock = sampleStock();

		// When
		BigDecimal price = stock.calculateVolumeWeightedPrice(allTrades());

		// Then
		assertThat(price, is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void shouldCalculateWeightedPrice() throws Exception {
		// Given
		Stock stock = sampleStock();
		Trade firstTrade = Trade.builder()
				.price(new BigDecimal("100.5"))
				.quantity(new BigDecimal("6"))
				.build();
		stock.record(firstTrade);

		Trade secondTrade = Trade.builder()
				.price(new BigDecimal("79.66"))
				.quantity(new BigDecimal("88"))
				.build();
		stock.record(secondTrade);

		BigDecimal expectedPrice = BigDecimal.valueOf(100.5 * 6 + 79.66 * 88)
				.divide(BigDecimal.valueOf(6 + 88), RoundingMode.HALF_UP);

		// When
		BigDecimal price = stock.calculateVolumeWeightedPrice(allTrades());

		// Then
		assertThat(price, is(equalTo(expectedPrice)));
	}

	@Test
	public void shouldUsePredicateToSelectTradesFotWeightedPriceCalculation() throws Exception {
		// Given
		Stock stock = stockWithTwoTrades();
		Predicate predicate = mock(Predicate.class);

		// When
		stock.calculateVolumeWeightedPrice(predicate);

		// Then
		verify(predicate, times(2)).test(any());
	}

	private Stock stockWithTwoTrades() {
		Stock stock = sampleStock();
		Trade firstTrade = Trade.builder()
				.price(new BigDecimal("10023.3455"))
				.quantity(new BigDecimal("2346"))
				.build();

		Trade secondTrade = Trade.builder()
				.price(new BigDecimal("279.66"))
				.quantity(new BigDecimal("9988"))
				.build();

		stock.record(firstTrade);
		stock.record(secondTrade);
		return stock;
	}

	private Predicate<Trade> allTrades() {
		return t -> true;
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
		Stock stock = mock(Stock.class);
		when(stock.calculatePriceEarningsRatioFor(any(BigDecimal.class))).thenCallRealMethod();
		when(stock.calculateDividendYieldFor(any(BigDecimal.class))).thenReturn(BigDecimal.ONE);
		return stock;
	}
}
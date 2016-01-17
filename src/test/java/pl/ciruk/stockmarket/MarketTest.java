package pl.ciruk.stockmarket;

import org.junit.Before;
import org.junit.Test;
import pl.ciruk.stockmarket.stock.CommonStock;
import pl.ciruk.stockmarket.stock.PreferredStock;
import pl.ciruk.stockmarket.stock.Stock;
import pl.ciruk.stockmarket.trade.Side;
import pl.ciruk.stockmarket.trade.Trade;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.ciruk.stockmarket.Matchers.closeTo;
import static pl.ciruk.stockmarket.Matchers.emptyOptional;
import static pl.ciruk.stockmarket.Matchers.optionalValue;
import static pl.ciruk.stockmarket.stock.Stocks.symbol;

public class MarketTest {

	private Market market;

	@Before
	public void setUp() throws Exception {
		market = new Market();
		market.register(
				new CommonStock(symbol(), BigDecimal.ONE, BigDecimal.ONE)
		);
	}

	@Test
	public void shouldRecordSampleTrade() throws Exception {
		// Given
		Trade trade = Trade.builder()
				.price(BigDecimal.ONE)
				.quantity(BigDecimal.TEN)
				.side(Side.BUY)
				.stock(symbol())
				.build();

		// When
		market.record(trade);

		// Then
		Long numberOfTrades = market.findStockFor(symbol())
				.map(Stock::streamOfTrades)
				.map(Stream::count)
				.orElse(0L);
		assertThat(numberOfTrades, is(equalTo(1L)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotRecordEmptyTrade() throws Exception {
		// Given
		Trade emptyTrade = null;

		// When
		market.record(emptyTrade);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotRecordTradeWithoutStockSymbol() throws Exception {
		// Given
		Trade trade = Trade.builder()
				.build();

		// When
		market.record(trade);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotRecordTradeWhenStockIsNotRegistered() throws Exception {
		// Given
		Trade trade = Trade.builder()
				.stock(unregisteredStock())
				.build();

		// When
		market.record(trade);
	}

	@Test
	public void shouldFindRegisteredStock() throws Exception {
		// Given
		market = new Market();
		market.register(
				new PreferredStock(registeredStock(), BigDecimal.ONE, BigDecimal.TEN, null)
		);

		// When
		Optional<String> stock = market.findStockFor(registeredStock())
				.map(Stock::getSymbol);

		// Then
		assertThat(stock, is(optionalValue(equalTo(registeredStock()))));
	}

	@Test
	public void shouldNotFindUnregisteredStock() throws Exception {
		// Given
		market = new Market();
		market.register(
				new PreferredStock(registeredStock(), BigDecimal.ONE, BigDecimal.TEN, null)
		);

		// When
		Optional<String> stock = market.findStockFor(unregisteredStock())
				.map(Stock::getSymbol);

		// Then
		assertThat(stock, is(emptyOptional()));
	}

	@Test
	public void shouldCalculateAllShareIndex() throws Exception {
		// Given
		marketHasANumberOfStocks();

		market.record(Trade.builder().stock("TEA").price(BigDecimal.valueOf(2)).quantity(BigDecimal.TEN).build());
		market.record(Trade.builder().stock("TEA").price(BigDecimal.valueOf(4)).quantity(BigDecimal.valueOf(20)).build());
		double teaTotalPrice = 2 * 10 + 4 * 20;
		double teaTotalQuantity = 10 + 20;
		double teaPrice = teaTotalPrice / teaTotalQuantity;

		market.record(Trade.builder().stock("POP").price(BigDecimal.valueOf(3)).quantity(BigDecimal.TEN).build());
		market.record(Trade.builder().stock("POP").price(BigDecimal.valueOf(9)).quantity(BigDecimal.valueOf(20)).build());
		double popTotalPrice = 3 * 10 + 9 * 20;
		double popTotalQuantity = 10 + 20;
		double popPrice = popTotalPrice / popTotalQuantity;

		market.record(Trade.builder().stock("ALE").price(BigDecimal.valueOf(5)).quantity(BigDecimal.TEN).build());
		market.record(Trade.builder().stock("ALE").price(BigDecimal.valueOf(2)).quantity(BigDecimal.valueOf(20)).build());
		double aleTotalPrice = 5 * 10 + 2 * 20;
		double aleTotalQuantity = 10 + 20;
		double alePrice = aleTotalPrice / aleTotalQuantity;

		// When
		BigDecimal allShareIndex = market.calculateAllShareIndex();

		// Then
		BigDecimal index = BigDecimal.valueOf(Math.pow(teaPrice * popPrice * alePrice, 1.0 / 3.0));
		assertThat(allShareIndex, is(closeTo(index)));
	}

	private void marketHasANumberOfStocks() {
		market = new Market();
		market.register(
				new CommonStock("TEA", BigDecimal.valueOf(100), BigDecimal.ZERO)
		);
		market.register(
				new CommonStock("POP", BigDecimal.valueOf(100), BigDecimal.valueOf(8))
		);
		market.register(
				new CommonStock("ALE", BigDecimal.valueOf(60), BigDecimal.valueOf(23))
		);
		market.register(
				new PreferredStock("GIN", BigDecimal.valueOf(100), BigDecimal.valueOf(2), BigDecimal.valueOf(8))
		);
		market.register(
				new CommonStock("JOE", BigDecimal.valueOf(250), BigDecimal.valueOf(13))
		);
	}

	private String registeredStock() {
		return "JPM";
	}

	private String unregisteredStock() {
		return UUID.randomUUID().toString();
	}
}
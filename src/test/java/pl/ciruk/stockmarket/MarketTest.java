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
				new PreferredStock(registeredStock(), BigDecimal.ONE, BigDecimal.TEN)
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
				new PreferredStock(registeredStock(), BigDecimal.ONE, BigDecimal.TEN)
		);

		// When
		Optional<String> stock = market.findStockFor(unregisteredStock())
				.map(Stock::getSymbol);

		// Then
		assertThat(stock, is(emptyOptional()));
	}



	private String registeredStock() {
		return "JPM";
	}

	private String unregisteredStock() {
		return UUID.randomUUID().toString();
	}
}
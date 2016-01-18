package pl.ciruk.stockmarket;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import pl.ciruk.stockmarket.math.Decimals;
import pl.ciruk.stockmarket.stock.Stock;
import pl.ciruk.stockmarket.trade.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Market {
	private Map<String, Stock> stocks = new ConcurrentHashMap<>();

	public void record(Trade trade) {
		log.debug("record(): trade={}", trade);
		Preconditions.checkArgument(trade != null, "Trade cannot be null");

		String stock = trade.getStock();
		checkStock(stock);

		stocks.get(stock)
				.record(trade);
	}

	public BigDecimal calculateRecentWeightedPriceFor(String stock) {
		log.debug("calculateRecentWeightedPriceFor(): stock={}", stock);
		checkStock(stock);

		LocalDateTime quarterAgo = LocalDateTime.now().minusMinutes(15);
		return stocks.get(stock)
				.calculateVolumeWeightedPrice(
						trade -> trade.getTimestamp().isAfter(quarterAgo)
				);
	}

	public BigDecimal calculateDividendYieldFor(String stock, BigDecimal price) {
		log.debug("calculateDividendYieldFor(): stock={}", stock);
		checkStock(stock);

		return stocks.get(stock)
				.calculateDividendYieldFor(price);
	}

	public BigDecimal calculatePriceEarningRatioFor(String stock, BigDecimal price) {
		log.debug("calculatePriceEarningRatioFor(): stock={}, price={}", stock, price);
		checkStock(stock);

		return stocks.get(stock)
				.calculatePriceEarningsRatioFor(price);
	}

	public BigDecimal calculateAllShareIndex() {
		log.debug("calculateAllShareIndex()");

		AtomicInteger numberOfStocks = new AtomicInteger(0);
		BigDecimal product = stocks.values().stream()
				.filter(stock -> stock.containsTrades())
				.map(stock -> calculateRecentWeightedPriceFor(stock.getSymbol()))
				.peek(s -> numberOfStocks.incrementAndGet())
				.reduce(BigDecimal.ONE, BigDecimal::multiply);
		log.trace("calculateAllShareIndex(): number of stocks={}, product={}", numberOfStocks.get(), product);

		return Decimals.nthRoot(numberOfStocks.get(), product);
	}

	Optional<Stock> findStockFor(String symbol) {
		return Optional.ofNullable(
				stocks.get(symbol)
		);
	}

	public void register(Stock stock) {
		log.debug("register(): stock={}", stock);

		stocks.putIfAbsent(stock.getSymbol(), stock);
	}

	private void checkStock(String stock) {
		Preconditions.checkArgument(stock != null, "Trade's stock cannot be null");
		Preconditions.checkState(stocks.containsKey(stock), "Stock %s is not registered on the market", stock);
	}
}

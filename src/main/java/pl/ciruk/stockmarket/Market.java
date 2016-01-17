package pl.ciruk.stockmarket;

import com.google.common.base.Preconditions;
import pl.ciruk.stockmarket.stock.Stock;
import pl.ciruk.stockmarket.trade.Trade;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Market {
	private Map<String, Stock> stocks = new ConcurrentHashMap<>();

	public void record(Trade trade) {
		Preconditions.checkArgument(trade != null, "Trade cannot be null");

		String stock = trade.getStock();
		Preconditions.checkArgument(stock != null, "Trade's stock cannot be null");
		Preconditions.checkState(stocks.containsKey(stock), "Stock %s is not registered on the market", stock);

		stocks.get(stock).record(trade);
	}

	public Optional<Stock> findStockFor(String symbol) {
		return Optional.ofNullable(
				stocks.get(symbol)
		);
	}

	public void register(Stock stock) {
		stocks.putIfAbsent(stock.getSymbol(), stock);
	}
}

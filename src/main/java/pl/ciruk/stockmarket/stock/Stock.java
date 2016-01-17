package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.math.Decimals;
import pl.ciruk.stockmarket.trade.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static pl.ciruk.stockmarket.math.Decimals.isZero;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Stock {

	@Getter
	private final String symbol;

	@Getter
	private final BigDecimal parValueInPennies;

	private final Set<Trade> trades = new TreeSet<>(Trade.BY_TIMESTAMP.reversed());

	public abstract BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies);

	public BigDecimal calculatePriceEarningsRatioFor(BigDecimal priceInPennies) {
		Preconditions.checkArgument(priceInPennies != null, "Price cannot be null");
		BigDecimal normalizedPrice = Decimals.applyDefaultScaleTo(priceInPennies);

		BigDecimal dividendYield = calculateDividendYieldFor(normalizedPrice);
		Preconditions.checkState(dividendYield.compareTo(BigDecimal.ZERO) != 0, "Dividend cannot be equal to zero");

		return normalizedPrice.divide(
				dividendYield,
				RoundingMode.HALF_UP);
	}

	public void record(Trade trade) {
		Preconditions.checkArgument(trade != null, "Trade cannot be null");

		trades.add(trade);
	}

	public BigDecimal calculateVolumeWeightedPrice(Predicate<Trade> tradeSelector) {
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.ZERO;

		for (Trade trade : trades) {
			if (tradeSelector.test(trade)) {
				totalPrice = totalPrice.add(
						trade.getPrice().multiply(trade.getQuantity())
				);

				totalQuantity = totalQuantity.add(
						trade.getQuantity()
				);
			}
		}

		if (isZero(totalQuantity)) {
			return BigDecimal.ZERO;
		} else {
			return totalPrice.divide(totalQuantity, RoundingMode.HALF_UP);
		}
	}

	public Stream<Trade> streamOfTrades() {
		return trades.stream();
	}
}

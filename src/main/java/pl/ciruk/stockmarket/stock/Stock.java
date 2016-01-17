package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.math.Decimals;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Stock {

	private final String symbol;

	private final BigDecimal parValueInPennies;

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
}

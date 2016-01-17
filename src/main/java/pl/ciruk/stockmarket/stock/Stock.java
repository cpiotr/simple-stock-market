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
		Preconditions.checkArgument(priceInPennies.compareTo(BigDecimal.ZERO) != 0, "Price cannot be equal to zero");
		BigDecimal normalizedPrice = Decimals.applyDefaultScaleTo(priceInPennies);

		return calculateDividendYieldFor(normalizedPrice)
				.divide(priceInPennies, RoundingMode.HALF_UP);
	}
}

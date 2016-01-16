package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.ciruk.stockmarket.math.Decimals.applyDefaultScaleTo;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PreferredStock extends Stock {
	private final BigDecimal fixedDividend;

	public PreferredStock(String symbol, BigDecimal parValueInPennies, BigDecimal fixedDividend) {
		super(symbol, parValueInPennies);
		this.fixedDividend = fixedDividend;
	}

	@Override
	public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
		Preconditions.checkArgument(priceInPennies != null, "Price cannot be null");
		Preconditions.checkArgument(BigDecimal.ZERO.compareTo(priceInPennies) != 0, "Price cannot be equal to zero");
		BigDecimal normalizedPrice = applyDefaultScaleTo(priceInPennies);

		Preconditions.checkState(fixedDividend != null);
		BigDecimal normalizedFixedDividend = applyDefaultScaleTo(fixedDividend);

		Preconditions.checkState(parValueInPennies != null);
		BigDecimal normalizedParValue = applyDefaultScaleTo(parValueInPennies);

		return normalizedFixedDividend.multiply(normalizedParValue)
				.divide(normalizedPrice, RoundingMode.HALF_UP);
	}
}

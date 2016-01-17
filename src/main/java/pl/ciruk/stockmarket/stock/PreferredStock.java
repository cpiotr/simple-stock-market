package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.math.Decimals;

import java.math.BigDecimal;

import static pl.ciruk.stockmarket.math.Decimals.applyDefaultScaleTo;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PreferredStock extends Stock {

	public PreferredStock(String symbol, BigDecimal parValueInPennies, BigDecimal fixedDividend, BigDecimal lastDividendInPennies) {
		super(symbol, parValueInPennies, fixedDividend, lastDividendInPennies);
	}

	@Override
	public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
		Preconditions.checkArgument(priceInPennies != null, "Price cannot be null");
		Preconditions.checkArgument(BigDecimal.ZERO.compareTo(priceInPennies) != 0, "Price cannot be equal to zero");
		BigDecimal normalizedPrice = applyDefaultScaleTo(priceInPennies);

		Preconditions.checkState(getFixedDividend() != null);
		BigDecimal normalizedFixedDividend = applyDefaultScaleTo(getFixedDividend());

		Preconditions.checkState(getParValueInPennies() != null);
		BigDecimal normalizedParValue = applyDefaultScaleTo(getParValueInPennies());

		return Decimals.divide(
				normalizedFixedDividend.multiply(normalizedParValue),
				normalizedPrice);
	}
}

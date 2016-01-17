package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.math.Decimals;

import java.math.BigDecimal;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonStock extends Stock {
	public CommonStock(String symbol, BigDecimal parValueInPennies, BigDecimal fixedDividend, BigDecimal lastDividendInPennies) {
		super(symbol, parValueInPennies, fixedDividend, lastDividendInPennies);
	}

	public CommonStock(String symbol, BigDecimal parValueInPennies, BigDecimal lastDividendInPennies) {
		super(symbol, parValueInPennies, null, lastDividendInPennies);
	}

	@Override
	public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
		Preconditions.checkArgument(priceInPennies != null, "Price cannot be null");
		Preconditions.checkArgument(priceInPennies.compareTo(BigDecimal.ZERO) != 0, "Price cannot be equal to zero");
		BigDecimal normalizedPrice = Decimals.applyDefaultScaleTo(priceInPennies);

		Preconditions.checkState(getLastDividendInPennies() != null);
		BigDecimal normalizedDivided = Decimals.applyDefaultScaleTo(getLastDividendInPennies());

		return Decimals.divide(
				normalizedDivided,
				normalizedPrice);
	}
}

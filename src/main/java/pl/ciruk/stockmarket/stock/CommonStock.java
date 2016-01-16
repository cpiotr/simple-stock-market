package pl.ciruk.stockmarket.stock;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.math.Decimals;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonStock extends Stock {
	private final BigDecimal lastDividendInPennies;

	public CommonStock(String symbol, BigDecimal parValueInPennies, BigDecimal lastDividendInPennies) {
		super(symbol, parValueInPennies);
		this.lastDividendInPennies = lastDividendInPennies;
	}

	@Override
	public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
		Preconditions.checkArgument(priceInPennies != null, "Price cannot be null");
		Preconditions.checkArgument(priceInPennies.compareTo(BigDecimal.ZERO) != 0, "Price cannot be equal to zero");
		BigDecimal normalizedPrice = Decimals.applyDefaultScaleTo(priceInPennies);

		Preconditions.checkState(lastDividendInPennies != null);
		BigDecimal normalizedDivided = Decimals.applyDefaultScaleTo(lastDividendInPennies);

		return normalizedDivided.divide(
				normalizedPrice,
				RoundingMode.HALF_UP
		);
	}
}

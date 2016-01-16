package pl.ciruk.stockmarket.stock;

import java.math.BigDecimal;

public class CommonStock extends Stock {
	private final BigDecimal lastDividendInPennies;

	public CommonStock(String symbol, BigDecimal parValueInPennies, BigDecimal lastDividendInPennies) {
		super(symbol, parValueInPennies);
		this.lastDividendInPennies = lastDividendInPennies;
	}

	@Override
	public BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies) {
		throw new UnsupportedOperationException();
	}
}

package pl.ciruk.stockmarket;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;

@Value
@Builder
public class Stock {
	String symbol;

	StockType type;

	BigDecimal lastDividendInPennies;

	BigInteger fixedDividend;

	BigDecimal parValueInPennies;
}

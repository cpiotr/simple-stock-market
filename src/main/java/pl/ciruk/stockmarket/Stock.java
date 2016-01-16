package pl.ciruk.stockmarket;

import lombok.Value;

import java.math.BigDecimal;

@Value
public abstract class Stock {
	String symbol;

	BigDecimal parValueInPennies;
}

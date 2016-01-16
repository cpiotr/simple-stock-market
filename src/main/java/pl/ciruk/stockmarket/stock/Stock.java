package pl.ciruk.stockmarket.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Stock {

	String symbol;

	BigDecimal parValueInPennies;

	public abstract BigDecimal calculateDividendYieldFor(BigDecimal priceInPennies);
}

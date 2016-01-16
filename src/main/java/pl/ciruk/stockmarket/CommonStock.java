package pl.ciruk.stockmarket;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class CommonStock extends Stock{
	BigDecimal lastDividendInPennies;
}

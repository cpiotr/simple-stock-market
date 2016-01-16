package pl.ciruk.stockmarket;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class PreferredStock extends Stock{
	BigDecimal fixedDividend;
}

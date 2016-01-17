package pl.ciruk.stockmarket.math;

import java.math.BigDecimal;

public class Decimals {
	public static BigDecimal applyDefaultScaleTo(BigDecimal value) {
		return value.setScale(12, BigDecimal.ROUND_HALF_UP);
	}

	public static boolean isZero(BigDecimal decimal) {
		return decimal.compareTo(BigDecimal.ZERO) == 0;
	}
}

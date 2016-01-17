package pl.ciruk.stockmarket.math;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

public class Decimals {
	private static final int SCALE = 24;

	private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

	public static BigDecimal applyDefaultScaleTo(BigDecimal value) {
		return value.setScale(SCALE, ROUNDING_MODE);
	}

	public static boolean isZero(BigDecimal decimal) {
		return decimal.compareTo(BigDecimal.ZERO) == 0;
	}

	public static BigDecimal nthRoot(int n, BigDecimal decimal) {
		Preconditions.checkArgument(
				decimal.compareTo(BigDecimal.ZERO) >= 0,
				"Value cannot be negative");

		if (decimal.equals(BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}

		BigDecimal precision = new BigDecimal("0.1").movePointLeft(SCALE);

		BigDecimal previousValue = decimal;
		BigDecimal value = decimal.divide(new BigDecimal(n), SCALE, ROUNDING_MODE);
		while (value.subtract(previousValue).abs().compareTo(precision) > 0) {
			previousValue = value;
			value = BigDecimal.valueOf(n - 1.0)
					.multiply(value)
					.add(decimal.divide(value.pow(n - 1), SCALE, ROUNDING_MODE))
					.divide(new BigDecimal(n), SCALE, ROUNDING_MODE);
		}
		return value;
	}

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
		return dividend.divide(divisor, SCALE, ROUNDING_MODE);
	}
}

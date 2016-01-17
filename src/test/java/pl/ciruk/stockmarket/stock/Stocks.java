package pl.ciruk.stockmarket.stock;

import java.math.BigDecimal;
import java.util.Random;

public class Stocks {
	private static Random RANDOM = new Random();

	public static PreferredStock samplePreferredStock() {
		BigDecimal parValueInPennies = BigDecimal.valueOf(RANDOM.nextLong());
		BigDecimal fixedDividend = BigDecimal.valueOf(RANDOM.nextDouble());
		return new PreferredStock(symbol(), parValueInPennies, fixedDividend, null);
	}

	public static String symbol() {
		return "TEA";
	}

	public static CommonStock sampleCommonStock() {
		BigDecimal parValueInPennies = BigDecimal.valueOf(RANDOM.nextLong());
		BigDecimal lastDividedInPennies = BigDecimal.valueOf(RANDOM.nextDouble());
		return new CommonStock(symbol(), parValueInPennies, lastDividedInPennies);
	}
}

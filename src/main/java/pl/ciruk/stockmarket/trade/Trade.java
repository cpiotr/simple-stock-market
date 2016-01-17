package pl.ciruk.stockmarket.trade;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@ToString
@Builder
@EqualsAndHashCode
public class Trade {
	public static final Comparator<Trade> BY_TIMESTAMP = Comparator.comparing(Trade::getTimestamp)
			.thenComparing(Trade::getStock, Comparator.nullsFirst(Comparator.naturalOrder()))
			.thenComparing(Trade::getQuantity, Comparator.nullsFirst(Comparator.naturalOrder()))
			.thenComparing(Trade::getPrice, Comparator.nullsFirst(Comparator.naturalOrder()));

	private String stock;

	private BigDecimal quantity;

	private Side side;

	private BigDecimal price;

	// Initialization in place, so that it's not available in builder
	private final LocalDateTime timestamp = LocalDateTime.now();
}

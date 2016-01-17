package pl.ciruk.stockmarket.trade;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Trade implements Comparable<Trade> {
	private String stock;

	private BigDecimal quantity;

	private Side side;

	private BigDecimal price;

	private final LocalDateTime timestamp = LocalDateTime.now();

	@Override
	public int compareTo(Trade other) {
		return timestamp.compareTo(other.timestamp);
	}
}

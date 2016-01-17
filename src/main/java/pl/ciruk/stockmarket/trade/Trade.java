package pl.ciruk.stockmarket.trade;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.ciruk.stockmarket.stock.Stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Trade {
	private Stock stock;

	private BigDecimal quantity;

	private Side side;

	private BigDecimal price;

	private final LocalDateTime timestamp = LocalDateTime.now();
}

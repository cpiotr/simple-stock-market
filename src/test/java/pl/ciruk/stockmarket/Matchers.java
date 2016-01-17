package pl.ciruk.stockmarket;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class Matchers {
	public static <T> Matcher<Optional<T>> optionalValue(Matcher<T> valueMatcher) {
		return new TypeSafeMatcher<Optional<T>>() {
			@Override
			protected boolean matchesSafely(Optional<T> item) {
				return item.map(v -> valueMatcher.matches(v))
						.orElse(false);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("optional not empty and ")
						.appendDescriptionOf(valueMatcher);
			}
		};
	}

	public static <T> Matcher<Optional<T>> emptyOptional() {
		return new TypeSafeMatcher<Optional<T>>() {
			@Override
			protected boolean matchesSafely(Optional<T> item) {
				return !item.isPresent();
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("empty optional");
			}
		};
	}

	public static Matcher<BigDecimal> closeTo(BigDecimal decimal) {
		return new TypeSafeMatcher<BigDecimal>() {
			@Override
			protected boolean matchesSafely(BigDecimal item) {
				return item.subtract(decimal).abs().setScale(10, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("close to ")
						.appendValue(decimal);
			}
		};
	}
}

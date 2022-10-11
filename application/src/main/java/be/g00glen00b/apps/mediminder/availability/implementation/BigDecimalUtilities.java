package be.g00glen00b.apps.mediminder.availability.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalUtilities {

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    private BigDecimalUtilities() {
    }

    public static boolean isPositive(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static BigDecimal toPercentage(BigDecimal value) {
        return value.divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}

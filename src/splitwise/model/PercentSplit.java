package splitwise.model;

import java.math.BigDecimal;
import java.util.Objects;

public class PercentSplit extends Split {
    public PercentSplit(User user, BigDecimal percent) {
        super(user, Objects.requireNonNull(percent, "percent must not be null"));
    }
}



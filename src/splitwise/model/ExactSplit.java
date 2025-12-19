package splitwise.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ExactSplit extends Split {
    public ExactSplit(User user, BigDecimal amount) {
        super(user, Objects.requireNonNull(amount, "amount must not be null"));
    }
}



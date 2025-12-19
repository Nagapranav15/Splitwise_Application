package splitwise.model;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Split {
    private final User user;
    private BigDecimal value;

    protected Split(User user, BigDecimal value) {
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}



package splitwise.model;

import java.math.BigDecimal;

public class EqualSplit extends Split {
    public EqualSplit(User user) {
        super(user, BigDecimal.ZERO);
    }
}



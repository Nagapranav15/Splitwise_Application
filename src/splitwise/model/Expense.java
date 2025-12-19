package splitwise.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Expense {
    private final String id;
    private final String description;
    private final BigDecimal amount;
    private final User paidBy;
    private final List<Split> splits;
    private final SplitType splitType;
    private final Group group;

    public Expense(String id,
                   String description,
                   BigDecimal amount,
                   User paidBy,
                   List<Split> splits,
                   SplitType splitType,
                   Group group) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.description = description;
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.paidBy = Objects.requireNonNull(paidBy, "paidBy must not be null");
        this.splits = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(splits, "splits must not be null")));
        this.splitType = Objects.requireNonNull(splitType, "splitType must not be null");
        this.group = Objects.requireNonNull(group, "group must not be null");
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public List<Split> getSplits() {
        return Collections.unmodifiableList(splits);
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public Group getGroup() {
        return group;
    }
}



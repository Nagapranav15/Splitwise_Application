package splitwise.service;

import splitwise.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExpenseService {
    private final Map<String, Expense> expenses = new HashMap<>();
    private final GroupService groupService;
    private final BalanceService balanceService;
    private int expenseIdCounter = 1;

    public ExpenseService(GroupService groupService, BalanceService balanceService) {
        this.groupService = groupService;
        this.balanceService = balanceService;
    }

    public Expense addExpense(String description,
                               BigDecimal amount,
                               String paidByUserId,
                               List<Split> splits,
                               SplitType splitType,
                               String groupId) {
        // Validate group exists
        Group group = groupService.getGroupByIdOrThrow(groupId);
        User paidBy = groupService.getUserService().getUserByIdOrThrow(paidByUserId);

        // Validate paidBy is in group
        if (!group.hasMember(paidBy)) {
            throw new IllegalArgumentException("User " + paidByUserId + " is not a member of group " + groupId);
        }

        // Validate splits before processing (for PERCENT, validate percentages)
        validateSplitsBeforeProcessing(splits, splitType, amount, group);

        // Process splits based on type
        List<Split> processedSplits = processSplits(splits, splitType, amount, group);

        // Validate splits after processing (for EXACT, validate amounts)
        validateSplitsAfterProcessing(processedSplits, splitType, amount);

        // Create expense
        String expenseId = "EXP" + expenseIdCounter++;
        Expense expense = new Expense(expenseId, description, amount, paidBy, processedSplits, splitType, group);
        expenses.put(expenseId, expense);

        // Update balances
        balanceService.updateBalances(expense);

        return expense;
    }

    private List<Split> processSplits(List<Split> splits, SplitType splitType, BigDecimal amount, Group group) {
        List<Split> processedSplits = new ArrayList<>();

        for (Split split : splits) {
            User user = split.getUser();
            // Group membership validation is done in validateSplitsBeforeProcessing

            Split processedSplit;
            switch (splitType) {
                case EQUAL:
                    processedSplit = new EqualSplit(user);
                    break;
                case EXACT:
                    processedSplit = new ExactSplit(user, split.getValue());
                    break;
                case PERCENT:
                    processedSplit = new PercentSplit(user, split.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown split type: " + splitType);
            }
            processedSplits.add(processedSplit);
        }

        // Calculate values for EQUAL and PERCENT splits
        if (splitType == SplitType.EQUAL) {
            BigDecimal perPersonAmount = amount.divide(
                    BigDecimal.valueOf(processedSplits.size()),
                    2,
                    RoundingMode.HALF_UP
            );
            for (Split split : processedSplits) {
                split.setValue(perPersonAmount);
            }
        } else if (splitType == SplitType.PERCENT) {
            for (Split split : processedSplits) {
                BigDecimal percent = split.getValue();
                BigDecimal share = amount.multiply(percent).divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
                split.setValue(share);
            }
        }

        return processedSplits;
    }

    private void validateSplitsBeforeProcessing(List<Split> splits, SplitType splitType, BigDecimal totalAmount, Group group) {
        if (splits.isEmpty()) {
            throw new IllegalArgumentException("Expense must have at least one participant");
        }

        // Validate PERCENT splits before processing (check percentages sum to 100)
        if (splitType == SplitType.PERCENT) {
            BigDecimal totalPercent = BigDecimal.ZERO;
            for (Split split : splits) {
                if (!group.hasMember(split.getUser())) {
                    throw new IllegalArgumentException("User " + split.getUser().getId() + " is not a member of the group");
                }
                totalPercent = totalPercent.add(split.getValue());
            }
            if (totalPercent.compareTo(BigDecimal.valueOf(100)) != 0) {
                throw new IllegalArgumentException(
                        "Sum of percentages (" + totalPercent + ") does not equal 100%");
            }
        }
    }

    private void validateSplitsAfterProcessing(List<Split> splits, SplitType splitType, BigDecimal totalAmount) {
        if (splitType == SplitType.EXACT) {
            BigDecimal sum = BigDecimal.ZERO;
            for (Split split : splits) {
                sum = sum.add(split.getValue());
            }
            if (sum.compareTo(totalAmount) != 0) {
                throw new IllegalArgumentException(
                        "Sum of exact splits (" + sum + ") does not equal total amount (" + totalAmount + ")");
            }
        }
    }

    public Map<String, Expense> getAllExpenses() {
        return new HashMap<>(expenses);
    }

    public Optional<Expense> getExpenseById(String id) {
        return Optional.ofNullable(expenses.get(id));
    }
}


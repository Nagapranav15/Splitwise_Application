package splitwise;

import splitwise.model.*;
import splitwise.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Splitwise Expense Sharing System ===\n");

        // Initialize services
        UserService userService = new UserService();
        GroupService groupService = new GroupService(userService);
        BalanceService balanceService = new BalanceService();
        ExpenseService expenseService = new ExpenseService(groupService, balanceService);

        // Create users
        System.out.println("1. Creating users...");
        User alice = userService.createUser("U1", "Alice");
        User bob = userService.createUser("U2", "Bob");
        User charlie = userService.createUser("U3", "Charlie");
        User diana = userService.createUser("U4", "Diana");
        System.out.println("   Created: " + alice.getName() + ", " + bob.getName() + ", " + 
                          charlie.getName() + ", " + diana.getName());

        // Create group
        System.out.println("\n2. Creating group...");
        Group vacationGroup = groupService.createGroup("G1", "Vacation Trip");
        groupService.addUserToGroup("G1", "U1");
        groupService.addUserToGroup("G1", "U2");
        groupService.addUserToGroup("G1", "U3");
        groupService.addUserToGroup("G1", "U4");
        System.out.println("   Created group: " + vacationGroup.getName() + " with 4 members");

        // Add expenses with EQUAL split
        System.out.println("\n3. Adding expenses...");
        
        // Expense 1: Hotel - EQUAL split
        System.out.println("\n   Expense 1: Hotel booking - $400 (EQUAL split)");
        List<Split> splits1 = new ArrayList<>();
        splits1.add(new EqualSplit(alice));
        splits1.add(new EqualSplit(bob));
        splits1.add(new EqualSplit(charlie));
        splits1.add(new EqualSplit(diana));
        expenseService.addExpense(
                "Hotel booking",
                new BigDecimal("400.00"),
                "U1",
                splits1,
                SplitType.EQUAL,
                "G1"
        );
        System.out.println("   ✓ Alice paid $400, split equally among 4 people ($100 each)");

        // Expense 2: Restaurant - EXACT split
        System.out.println("\n   Expense 2: Restaurant - $150 (EXACT split)");
        List<Split> splits2 = new ArrayList<>();
        splits2.add(new ExactSplit(alice, new BigDecimal("50.00")));
        splits2.add(new ExactSplit(bob, new BigDecimal("50.00")));
        splits2.add(new ExactSplit(charlie, new BigDecimal("30.00")));
        splits2.add(new ExactSplit(diana, new BigDecimal("20.00")));
        expenseService.addExpense(
                "Restaurant dinner",
                new BigDecimal("150.00"),
                "U2",
                splits2,
                SplitType.EXACT,
                "G1"
        );
        System.out.println("   ✓ Bob paid $150, split exactly: Alice $50, Bob $50, Charlie $30, Diana $20");

        // Expense 3: Taxi - PERCENT split
        System.out.println("\n   Expense 3: Taxi - $200 (PERCENT split)");
        List<Split> splits3 = new ArrayList<>();
        splits3.add(new PercentSplit(alice, new BigDecimal("30")));
        splits3.add(new PercentSplit(bob, new BigDecimal("30")));
        splits3.add(new PercentSplit(charlie, new BigDecimal("25")));
        splits3.add(new PercentSplit(diana, new BigDecimal("15")));
        expenseService.addExpense(
                "Taxi rides",
                new BigDecimal("200.00"),
                "U3",
                splits3,
                SplitType.PERCENT,
                "G1"
        );
        System.out.println("   ✓ Charlie paid $200, split by percent: Alice 30%, Bob 30%, Charlie 25%, Diana 15%");

        // View balances
        System.out.println("\n4. Viewing balances...");
        balanceService.printAllBalances(userService.getAllUsers());

        // View individual user balance
        System.out.println("\n5. Viewing individual balances...");
        balanceService.printBalancesForUser("U1", userService.getAllUsers());
        balanceService.printBalancesForUser("U2", userService.getAllUsers());
        balanceService.printBalancesForUser("U3", userService.getAllUsers());
        balanceService.printBalancesForUser("U4", userService.getAllUsers());

        // Settle balance
        System.out.println("\n6. Settling balances...");
        BigDecimal bobOwesAlice = balanceService.getBalance("U2", "U1");
        System.out.println("   Bob owes Alice: " + bobOwesAlice);
        if (bobOwesAlice.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("   Settling $30 from Bob to Alice...");
            balanceService.settleBalance("U2", "U1", new BigDecimal("30.00"));
            System.out.println("   ✓ Partial settlement completed");
        }

        // View balances after settlement
        System.out.println("\n7. Balances after settlement...");
        balanceService.printAllBalances(userService.getAllUsers());

        // Demonstrate edge case handling
        System.out.println("\n8. Demonstrating edge case handling...");
        
        // Try to add expense with invalid exact split
        System.out.println("\n   Attempting to add expense with invalid EXACT split (sum != total)...");
        try {
            List<Split> invalidSplits = new ArrayList<>();
            invalidSplits.add(new ExactSplit(alice, new BigDecimal("50.00")));
            invalidSplits.add(new ExactSplit(bob, new BigDecimal("50.00")));
            expenseService.addExpense(
                    "Invalid expense",
                    new BigDecimal("150.00"),
                    "U1",
                    invalidSplits,
                    SplitType.EXACT,
                    "G1"
            );
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ Caught expected error: " + e.getMessage());
        }

        // Try to settle more than owed
        System.out.println("\n   Attempting to settle more than owed...");
        try {
            BigDecimal currentBalance = balanceService.getBalance("U2", "U1");
            balanceService.settleBalance("U2", "U1", currentBalance.add(new BigDecimal("100.00")));
        } catch (IllegalArgumentException e) {
            System.out.println("   ✓ Caught expected error: " + e.getMessage());
        }

        System.out.println("\n=== Demo Complete ===");
    }
}


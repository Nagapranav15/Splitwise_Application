package splitwise.service;

import splitwise.model.Expense;
import splitwise.model.Split;
import splitwise.model.User;

import java.math.BigDecimal;
import java.util.*;

public class BalanceService {
    // Map: User A -> (User B -> Amount) means A owes B that amount
    private final Map<String, Map<String, BigDecimal>> balances = new HashMap<>();

    public void updateBalances(Expense expense) {
        User paidBy = expense.getPaidBy();
        List<Split> splits = expense.getSplits();

        for (Split split : splits) {
            User participant = split.getUser();
            BigDecimal share = split.getValue();

            // Skip if participant is the one who paid
            if (participant.equals(paidBy)) {
                continue;
            }

            // Participant owes the paidBy user
            addBalance(participant.getId(), paidBy.getId(), share);
        }
    }

    private void addBalance(String debtorId, String creditorId, BigDecimal amount) {
        balances.computeIfAbsent(debtorId, k -> new HashMap<>())
                .merge(creditorId, amount, BigDecimal::add);
    }

    public void settleBalance(String fromUserId, String toUserId, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(fromUserId, toUserId);
        
        if (amount.compareTo(currentBalance) > 0) {
            throw new IllegalArgumentException(
                    "Cannot settle more than owed. " + fromUserId + " owes " + toUserId + " only " + currentBalance);
        }

        BigDecimal remaining = currentBalance.subtract(amount);
        
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            // Partial settlement
            balances.get(fromUserId).put(toUserId, remaining);
        } else {
            // Full settlement
            balances.get(fromUserId).remove(toUserId);
            if (balances.get(fromUserId).isEmpty()) {
                balances.remove(fromUserId);
            }
        }
    }

    public BigDecimal getBalance(String fromUserId, String toUserId) {
        return balances.getOrDefault(fromUserId, Collections.emptyMap())
                .getOrDefault(toUserId, BigDecimal.ZERO);
    }

    public Map<String, BigDecimal> getBalancesForUser(String userId) {
        Map<String, BigDecimal> userBalances = new HashMap<>();
        
        // What this user owes to others
        Map<String, BigDecimal> owes = balances.getOrDefault(userId, Collections.emptyMap());
        for (Map.Entry<String, BigDecimal> entry : owes.entrySet()) {
            userBalances.put("owes " + entry.getKey(), entry.getValue());
        }
        
        // What others owe this user
        for (Map.Entry<String, Map<String, BigDecimal>> entry : balances.entrySet()) {
            if (!entry.getKey().equals(userId)) {
                BigDecimal owed = entry.getValue().getOrDefault(userId, BigDecimal.ZERO);
                if (owed.compareTo(BigDecimal.ZERO) > 0) {
                    userBalances.put("owed by " + entry.getKey(), owed);
                }
            }
        }
        
        return userBalances;
    }

    public Map<String, Map<String, BigDecimal>> getAllBalances() {
        // Return simplified balances
        return simplifyBalances();
    }

    /**
     * Simplifies balances to minimize the number of transactions.
     * Uses a greedy approach to find cycles and reduce transactions.
     */
    private Map<String, Map<String, BigDecimal>> simplifyBalances() {
        // Create a working copy
        Map<String, Map<String, BigDecimal>> simplified = new HashMap<>();
        for (Map.Entry<String, Map<String, BigDecimal>> entry : balances.entrySet()) {
            Map<String, BigDecimal> innerMap = new HashMap<>(entry.getValue());
            simplified.put(entry.getKey(), innerMap);
        }

        // Find and eliminate cycles (A owes B, B owes C, C owes A)
        boolean changed = true;
        while (changed) {
            changed = false;
            List<String> users = new ArrayList<>(simplified.keySet());
            
            for (int i = 0; i < users.size(); i++) {
                String userA = users.get(i);
                Map<String, BigDecimal> aBalances = simplified.get(userA);
                if (aBalances == null) continue;
                
                for (String userB : new ArrayList<>(aBalances.keySet())) {
                    Map<String, BigDecimal> bBalances = simplified.get(userB);
                    if (bBalances == null) continue;
                    
                    BigDecimal aOwesB = aBalances.get(userB);
                    BigDecimal bOwesA = bBalances.getOrDefault(userA, BigDecimal.ZERO);
                    
                    if (aOwesB.compareTo(BigDecimal.ZERO) > 0 && bOwesA.compareTo(BigDecimal.ZERO) > 0) {
                        // Both owe each other - simplify
                        BigDecimal min = aOwesB.min(bOwesA);
                        aOwesB = aOwesB.subtract(min);
                        bOwesA = bOwesA.subtract(min);
                        
                        if (aOwesB.compareTo(BigDecimal.ZERO) == 0) {
                            aBalances.remove(userB);
                        } else {
                            aBalances.put(userB, aOwesB);
                        }
                        
                        if (bOwesA.compareTo(BigDecimal.ZERO) == 0) {
                            bBalances.remove(userA);
                        } else {
                            bBalances.put(userA, bOwesA);
                        }
                        
                        if (aBalances.isEmpty()) {
                            simplified.remove(userA);
                        }
                        if (bBalances.isEmpty()) {
                            simplified.remove(userB);
                        }
                        
                        changed = true;
                    }
                }
            }
        }

        // Remove zero balances
        simplified.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        return simplified;
    }

    public void printBalancesForUser(String userId, Map<String, User> users) {
        Map<String, BigDecimal> userBalances = getBalancesForUser(userId);
        User user = users.get(userId);
        
        if (userBalances.isEmpty()) {
            System.out.println(user.getName() + " has no balances.");
            return;
        }
        
        System.out.println("\n=== Balances for " + user.getName() + " ===");
        for (Map.Entry<String, BigDecimal> entry : userBalances.entrySet()) {
            String key = entry.getKey();
            BigDecimal amount = entry.getValue();
            
            if (key.startsWith("owes ")) {
                String creditorId = key.substring(5);
                User creditor = users.get(creditorId);
                System.out.println("  Owes " + creditor.getName() + ": " + amount);
            } else if (key.startsWith("owed by ")) {
                String debtorId = key.substring(8);
                User debtor = users.get(debtorId);
                System.out.println("  Owed by " + debtor.getName() + ": " + amount);
            }
        }
    }

    public void printAllBalances(Map<String, User> users) {
        Map<String, Map<String, BigDecimal>> allBalances = getAllBalances();
        
        if (allBalances.isEmpty()) {
            System.out.println("\n=== All Balances ===");
            System.out.println("No outstanding balances.");
            return;
        }
        
        System.out.println("\n=== All Balances (Simplified) ===");
        for (Map.Entry<String, Map<String, BigDecimal>> entry : allBalances.entrySet()) {
            String debtorId = entry.getKey();
            User debtor = users.get(debtorId);
            
            for (Map.Entry<String, BigDecimal> balanceEntry : entry.getValue().entrySet()) {
                String creditorId = balanceEntry.getKey();
                BigDecimal amount = balanceEntry.getValue();
                User creditor = users.get(creditorId);
                
                System.out.println(debtor.getName() + " owes " + creditor.getName() + ": " + amount);
            }
        }
    }
}


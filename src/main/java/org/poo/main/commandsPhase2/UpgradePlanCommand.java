package org.poo.main.commandsPhase2;

import org.poo.main.CurrencyConverter;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.commands.Command;

import java.util.List;

/**
 * Command for upgrading a user's account plan.
 * Ensures proper validation, fee calculation, and transaction logging.
 */
public final class UpgradePlanCommand implements Command {

    private static final double STANDARD_TO_SILVER_FEE = 100.0;
    private static final double STANDARD_TO_GOLD_FEE = 350.0;
    private static final double SILVER_TO_GOLD_FEE = 250.0;

    private final String newPlanType;
    private final String accountIBAN;
    private final int timestamp;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;

    /**
     * Constructor for UpgradePlanCommand.
     *
     * @param newPlanType       the new plan type to upgrade to.
     * @param accountIBAN       the account's IBAN.
     * @param timestamp         the transaction timestamp.
     * @param users             the list of users.
     * @param currencyConverter the currency converter for fee calculation.
     */
    public UpgradePlanCommand(final String newPlanType,
                              final String accountIBAN,
                              final int timestamp,
                              final List<User> users,
                              final CurrencyConverter currencyConverter) {
        this.newPlanType = newPlanType;
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
        this.users = users;
        this.currencyConverter = currencyConverter;
    }

    /**
     * Executes the plan upgrade command.
     * Validates user and account, calculates the fee, deducts the balance,
     * upgrades the plan, and logs the transaction.
     */
    @Override
    public void execute() {
        User user = null;
        Account account = null;

        for (User u : users) {
            for (Account acc : u.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    user = u;
                    account = acc;
                    break;
                }
            }
        }
        if (user == null || account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (user.getPlanType().equals(newPlanType)) {
            throw new IllegalArgumentException("The user already has the " + newPlanType + " plan.");
        }

        List<String> plans = List.of("standard", "student", "silver", "gold");
        int currentPlanIndex = plans.indexOf(user.getPlanType());
        int newPlanIndex = plans.indexOf(newPlanType);

        if (newPlanIndex < currentPlanIndex) {
            throw new IllegalArgumentException("You cannot downgrade your plan.");
        }

        double fee = 0.0;
        if (user.getPlanType().equals("standard") || user.getPlanType().equals("student")) {
            if (newPlanType.equals("silver")) {
                fee = STANDARD_TO_SILVER_FEE;
            } else if (newPlanType.equals("gold")) {
                fee = STANDARD_TO_GOLD_FEE;
            }
        } else if (user.getPlanType().equals("silver") && newPlanType.equals("gold")) {
            fee = SILVER_TO_GOLD_FEE;
        }

        double convertedFee = currencyConverter.convert(fee, "RON", account.getCurrency());

        if (account.getBalance() < convertedFee) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - convertedFee);
        user.upgradePlanType(newPlanType);
        Transaction transaction3 = Transaction.upgradePlanTransaction(
                accountIBAN,
                "Upgrade plan",
                newPlanType,
                timestamp
        );
        account.getOwner().addTransaction(transaction3);
    }
}

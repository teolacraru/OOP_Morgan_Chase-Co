package org.poo.main.splitpayments2;

import org.poo.main.CurrencyConverter;
import org.poo.main.User;
import org.poo.main.commands.Command;
import org.poo.main.commands.SplitPaymentCommand;

import java.util.List;

/**
 * Command for accepting a split payment by a user.
 * Processes the payment once all participants have accepted.
 */
public final class AcceptSplitPaymentCommand implements Command {

    private final String email;
    private final int timestamp;
    private final List<SplitPaymentStatus> activeTransactions;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;

    /**
     * Constructs an AcceptSplitPaymentCommand.
     *
     * @param email              the email of the user accepting the payment.
     * @param timestamp          the current timestamp.
     * @param activeTransactions the list of active split payment transactions.
     * @param users              the list of users involved.
     * @param currencyConverter  the utility for currency conversion.
     */
    public AcceptSplitPaymentCommand(final String email,
                                     final int timestamp,
                                     final List<SplitPaymentStatus> activeTransactions,
                                     final List<User> users,
                                     final CurrencyConverter currencyConverter) {
        this.email = email;
        this.timestamp = timestamp;
        this.activeTransactions = activeTransactions;
        this.users = users;
        this.currencyConverter = currencyConverter;
    }

    /**
     * Executes the command to accept a split payment.
     * If all participants have accepted, the payment is processed.
     *
     * @throws IllegalArgumentException if no active transaction is found for the user.
     */
    @Override
    public void execute() {
        for (User user : users) {
            SplitPaymentStatus currentTransaction = user.peekTransaction();

            if (currentTransaction != null
                    && currentTransaction.getUserStatus().containsKey(email)) {
                currentTransaction.accept(email);

                if (currentTransaction.allAccepted()) {
                    new SplitPaymentCommand(
                            currentTransaction.getTimestamp(),
                            currentTransaction.getAmount(),
                            currentTransaction.getCurrency(),
                            currentTransaction.getSplitPaymentType(),
                            currentTransaction.getAccountIbans(),
                            currentTransaction.getAmountForUsers(),
                            users,
                            currencyConverter,
                            currentTransaction
                    ).execute();
                }
                user.pollTransaction();
                return;
            }
        }
        throw new IllegalArgumentException("No active transaction found for user: " + email);
    }
}

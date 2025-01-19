package org.poo.main.splitpayments2;

import org.poo.main.User;
import org.poo.main.commands.Command;

import java.util.List;

/**
 * Command for rejecting a split payment by a user.
 * Removes the transaction from the user's active transactions.
 */
public final class RejectSplitPaymentCommand implements Command {

    private final String email;
    private final int timestamp;
    private final List<SplitPaymentStatus> activeTransactions;
    private final List<User> users;

    /**
     * Constructs a RejectSplitPaymentCommand.
     *
     * @param email              the email of the user rejecting the payment.
     * @param timestamp          the current timestamp.
     * @param activeTransactions the list of active split payment transactions.
     * @param users              the list of users involved.
     */
    public RejectSplitPaymentCommand(final String email,
                                     final int timestamp,
                                     final List<SplitPaymentStatus> activeTransactions,
                                     final List<User> users) {
        this.email = email;
        this.timestamp = timestamp;
        this.activeTransactions = activeTransactions;
        this.users = users;
    }

    /**
     * Executes the command to reject a split payment.
     * Removes the transaction from the user's active transactions.
     *
     * @throws IllegalArgumentException if the user is not found.
     */
    @Override
    public void execute() {
        User targetUser = findUserByEmail(email);
        if (targetUser == null) {
            throw new IllegalArgumentException("User not found: " + email);
        }

        SplitPaymentStatus currentTransaction = targetUser.peekTransaction();
        if (currentTransaction == null) {
            return;
        }
        currentTransaction.reject(email);
        targetUser.pollTransaction();
    }

    /**
     * Finds a user by their email.
     *
     * @param targetEmail the email to search for.
     * @return the user with the matching email or null if not found.
     */
    private User findUserByEmail(final String targetEmail) {
        for (User user : users) {
            if (user.getEmail().equals(targetEmail)) {
                return user;
            }
        }
        return null;
    }
}

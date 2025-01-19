package org.poo.main.splitpayments2;

import org.poo.main.CurrencyConverter;
import org.poo.main.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the status of a split payment transaction.
 * Tracks user responses and transaction details.
 */
public final class SplitPaymentStatus {

    private final String transactionId;
    private final int timestamp;
    private final double amount;
    private final String currency;
    private final String splitPaymentType;
    private final List<String> accountIbans;
    private final List<Double> amountForUsers;
    private final Map<String, Boolean> userStatus;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;
    private boolean failed;

    /**
     * Constructs a SplitPaymentStatus object.
     *
     * @param transactionId     the unique transaction ID.
     * @param userEmails        the list of user emails involved in the transaction.
     * @param timestamp         the timestamp of the transaction.
     * @param amount            the total amount to be split.
     * @param currency          the currency of the transaction.
     * @param splitPaymentType  the type of split payment.
     * @param accountIbans      the list of account IBANs.
     * @param amountForUsers    the list of amounts for each user.
     * @param users             the list of user objects.
     * @param currencyConverter the utility for currency conversion.
     */
    public SplitPaymentStatus(final String transactionId,
                              final List<String> userEmails,
                              final int timestamp,
                              final double amount,
                              final String currency,
                              final String splitPaymentType,
                              final List<String> accountIbans,
                              final List<Double> amountForUsers,
                              final List<User> users,
                              final CurrencyConverter currencyConverter) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
        this.splitPaymentType = splitPaymentType;
        this.accountIbans = accountIbans;
        this.amountForUsers = amountForUsers;
        this.userStatus = new HashMap<>();
        for (String email : userEmails) {
            userStatus.put(email, null);
        }
        this.users = users;
        this.currencyConverter = currencyConverter;
        this.failed = false;
    }

    /**
     * Accepts the transaction for a user.
     *
     * @param email the email of the user accepting the transaction.
     */
    public void accept(final String email) {
        if (!userStatus.containsKey(email)) {
            throw new IllegalArgumentException("User not part of this transaction: " + email);
        }
        if (failed) {
            return;
        }
        userStatus.put(email, true);
    }

    /**
     * Rejects the transaction for a user.
     *
     * @param email the email of the user rejecting the transaction.
     */
    public void reject(final String email) {
        if (!userStatus.containsKey(email)) {
            throw new IllegalArgumentException("User not part of this transaction: " + email);
        }
        userStatus.put(email, false);
    }

    /**
     * Marks the transaction as failed.
     *
     * @param reason the reason for the failure.
     */
    public void markAsFailed(final String reason) {
        this.failed = true;
    }

    /**
     * Checks if the transaction has failed.
     *
     * @return true if the transaction has failed, false otherwise.
     */
    public boolean isFailed() {
        return failed;
    }

    /**
     * Gets the timestamp of the transaction.
     *
     * @return the transaction timestamp.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the total amount of the transaction.
     *
     * @return the transaction amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the currency of the transaction.
     *
     * @return the transaction currency.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Gets the type of split payment.
     *
     * @return the split payment type.
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    /**
     * Gets the account IBANs associated with the transaction.
     *
     * @return the list of account IBANs.
     */
    public List<String> getAccountIbans() {
        return accountIbans;
    }

    /**
     * Gets the amounts for each user.
     *
     * @return the list of amounts.
     */
    public List<Double> getAmountForUsers() {
        return amountForUsers;
    }

    /**
     * Checks if all users have accepted the transaction.
     *
     * @return true if all users accepted, false otherwise.
     */
    public boolean allAccepted() {
        return userStatus.values().stream().allMatch(value -> Boolean.TRUE.equals(value));
    }

    /**
     * Checks if any user has rejected the transaction.
     *
     * @return true if any user rejected, false otherwise.
     */
    public boolean anyRejected() {
        return userStatus.containsValue(Boolean.FALSE);
    }

    /**
     * Gets the user statuses for the transaction.
     *
     * @return a map of user emails and their statuses.
     */
    public Map<String, Boolean> getUserStatus() {
        return userStatus;
    }
}

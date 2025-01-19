package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Represents a financial transaction with various attributes and utility methods.
 */
public final class Transaction {
    private final int timestamp;
    private final String description;
    private final String senderIBAN;
    private final String receiverIBAN;
    private final double amount;
    private final String currency;
    private final String transferType;
    private final String card;
    private final String cardHolder;
    private final String commerciant;
    private final String transactionType;
    private final List<String> accounts;
    private final List<Double> amounts;

    /**
     * Constructs a Transaction object.
     *
     * @param timestamp       the timestamp of the transaction
     * @param description     the description of the transaction
     * @param senderIBAN      the sender's IBAN
     * @param receiverIBAN    the receiver's IBAN
     * @param amount          the amount involved in the transaction
     * @param currency        the currency of the transaction
     * @param transferType    the type of transfer
     * @param card            the card used for the transaction
     * @param cardHolder      the card holder's name
     * @param commerciant     the merchant involved in the transaction
     * @param transactionType the type of transaction
     * @param accounts        the list of accounts involved in the transaction
     */
    public Transaction(final int timestamp, final String description, final String senderIBAN,
                       final String receiverIBAN, final double amount, final String currency,
                       final String transferType, final String card, final String cardHolder,
                       final String commerciant, final String transactionType,
                       final List<String> accounts, final List<Double> amounts) {
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.currency = currency;
        this.transferType = transferType;
        this.card = card;
        this.cardHolder = cardHolder;
        this.commerciant = commerciant;
        this.transactionType = transactionType;
        this.accounts = accounts;
        this.amounts = amounts;
    }


    /**
     * Creates a send money transaction.
     *
     * @param timestamp    the timestamp of the transaction
     * @param description  the description of the transaction
     * @param senderIBAN   the sender's IBAN
     * @param receiverIBAN the receiver's IBAN
     * @param amount       the amount to send
     * @param currency     the currency of the transaction
     * @param transferType the type of transfer
     * @return a Transaction representing a send money transaction
     */
    public static Transaction createSendMoneyTransaction(final int timestamp,
                                                         final String description,
                                                         final String senderIBAN,
                                                         final String receiverIBAN,
                                                         final double amount,
                                                         final String currency,
                                                         final String transferType) {
        return new Transaction(
                timestamp,
                description,
                senderIBAN,
                receiverIBAN,
                amount,
                currency,
                transferType,
                null,
                null,
                null,
                "sendMoney",
                null,
                null
        );
    }

    /**
     * Creates a card transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the transaction
     * @param senderIBAN  the sender's IBAN
     * @param card        the card used for the transaction
     * @param cardHolder  the name of the card holder
     * @return a Transaction representing a card transaction
     */
    public static Transaction createCardTransaction(final int timestamp,
                                                    final String description,
                                                    final String senderIBAN,
                                                    final String card,
                                                    final String cardHolder) {
        return new Transaction(
                timestamp,
                description,
                senderIBAN,
                null,
                0,
                null,
                null,
                card,
                cardHolder,
                null,
                "createCard",
                null,
                null
        );
    }

    /**
     * Creates a pay online transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the transaction
     * @param amount      the amount involved in the transaction
     * @param commerciant the merchant involved in the transaction
     * @return a Transaction representing a pay online transaction
     */
    public static Transaction createPayOnlineTransaction(final int timestamp,
                                                         final String description,
                                                         final double amount,
                                                         final String commerciant) {
        return new Transaction(
                timestamp,
                description,
                null,
                null,
                amount,
                null,
                null,
                null,
                null,
                commerciant,
                "payOnline",
                null,
                null
        );
    }

    /**
     * Creates an add account transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the transaction
     * @param senderIBAN  the sender's IBAN
     * @param currency    the currency of the transaction
     * @return a Transaction representing an add account transaction
     */
    public static Transaction addAccountTransaction(final int timestamp,
                                                    final String description,
                                                    final String senderIBAN,
                                                    final String currency) {
        return new Transaction(
                timestamp,
                description,
                senderIBAN,
                null,
                0.0,
                currency,
                null,
                null,
                null,
                null,
                "addAccount",
                null,
                null
        );
    }

    /**
     * Creates a split payment transaction.
     *
     * @param timestamp   the timestamp of the transaction
     * @param description the description of the transaction
     * @param amount      the amount to split
     * @param currency    the currency of the transaction
     * @param accounts    the list of accounts involved in the split
     * @return a Transaction representing a split payment transaction
     */
    public static Transaction splitPaymentTransaction(final int timestamp,
                                                      final String description,
                                                      final double amount,
                                                      final String currency,
                                                      final List<String> accounts) {
        return new Transaction(
                timestamp,
                description,
                null,
                null,
                amount,
                currency,
                null,
                null,
                null,
                null,
                "splitPayment",
                accounts,
                null
        );
    }

    /**
     * Creates a custom split payment transaction.
     *
     * @param timestamp   the timestamp of the transaction.
     * @param description the description of the transaction.
     * @param amount      the total amount to be split.
     * @param currency    the currency of the transaction.
     * @param accounts    the list of accounts involved.
     * @param amounts     the list of amounts per account.
     * @return a Transaction representing a custom split payment transaction.
     */
    public static Transaction splitPaymentCustomTransaction(final int timestamp,
                                                            final String description,
                                                            final double amount,
                                                            final String currency,
                                                            final List<String> accounts,
                                                            final List<Double> amounts) {
        return new Transaction(
                timestamp,
                description,
                "custom",
                null,
                amount,
                currency,
                null,
                null,
                null,
                null,
                "splitPaymentCustom",
                accounts,
                amounts
        );
    }

    /**
     * Creates a custom split payment transaction with an error.
     *
     * @param timestamp   the timestamp of the transaction.
     * @param description the description of the transaction.
     * @param receiverIBAN the IBAN for the error.
     * @param amount      the amount involved.
     * @param currency    the currency of the transaction.
     * @param accounts    the list of accounts involved.
     * @param amounts     the list of amounts per account.
     * @return a Transaction representing a custom split payment transaction with an error.
     */
    public static Transaction splitPaymentCustomErrorTransaction(final int timestamp,
                                                                 final String description,
                                                                 final String receiverIBAN,
                                                                 final double amount,
                                                                 final String currency,
                                                                 final List<String> accounts,
                                                                 final List<Double> amounts) {
        return new Transaction(
                timestamp,
                description,
                "custom",
                receiverIBAN,
                amount,
                currency,
                null,
                null,
                null,
                null,
                "splitPaymentCustom",
                accounts,
                amounts
        );
    }

    /**
     * Creates a split payment transaction with equal shares and an error.
     *
     * @param timestamp   the timestamp of the transaction.
     * @param description the description of the transaction.
     * @param receiverIBAN the IBAN for the error.
     * @param amount      the total amount to be split.
     * @param currency    the currency of the transaction.
     * @param accounts    the list of accounts involved.
     * @return a Transaction representing an equal split payment transaction with an error.
     */
    public static Transaction splitPaymentEqualErrorTransaction(final int timestamp,
                                                                final String description,
                                                                final String receiverIBAN,
                                                                final double amount,
                                                                final String currency,
                                                                final List<String> accounts) {
        return new Transaction(
                timestamp,
                description,
                "equal",
                receiverIBAN,
                amount,
                currency,
                null,
                null,
                null,
                null,
                "splitPaymentEqual",
                accounts,
                null
        );
    }

    /**
     * Creates a transaction for deleting an account.
     *
     * @param timestamp   the timestamp of the transaction.
     * @param description the description of the transaction.
     * @return a Transaction representing the deletion of an account.
     */
    public static Transaction deleteAccountTransaction(final int timestamp,
                                                       final String description) {
        return new Transaction(
                timestamp,
                description,
                null,
                null,
                0.0,
                null,
                null,
                null,
                null,
                null,
                "deleteAccount",
                null,
                null
        );
    }

    /**
     * Creates a transaction for deleting a card.
     *
     * @param senderIBAN  the IBAN of the card owner.
     * @param timestamp   the timestamp of the transaction.
     * @param card        the card to be deleted.
     * @param cardHolder  the holder of the card.
     * @param description the description of the transaction.
     * @return a Transaction representing the deletion of a card.
     */
    public static Transaction deleteCardTransaction(final String senderIBAN,
                                                    final int timestamp,
                                                    final String card,
                                                    final String cardHolder,
                                                    final String description) {
        return new Transaction(
                timestamp,
                description,
                senderIBAN,
                null,
                0.0,
                null,
                null,
                card,
                cardHolder,
                null,
                "deleteCard",
                null,
                null
        );
    }

    /**
     * Creates a transaction for upgrading an account plan.
     *
     * @param senderIBAN   the IBAN of the account being upgraded.
     * @param description  the description of the transaction.
     * @param receiverIBAN the new plan type for the account.
     * @param timestamp    the timestamp of the transaction.
     * @return a Transaction representing the upgrade of an account plan.
     */
    public static Transaction upgradePlanTransaction(final String senderIBAN,
                                                     final String description,
                                                     final String receiverIBAN,
                                                     final int timestamp) {
        return new Transaction(
                timestamp,
                description,
                senderIBAN,
                receiverIBAN,
                0.0,
                null,
                null,
                null,
                null,
                null,
                "upgradePlan",
                null,
                null
        );
    }

    /**
     * Creates a transaction for cash withdrawal.
     *
     * @param description the description of the transaction.
     * @param amount      the amount withdrawn.
     * @param timestamp   the timestamp of the transaction.
     * @return a Transaction representing a cash withdrawal.
     */
    public static Transaction cashWithdrawalTransaction(final String description,
                                                        final double amount,
                                                        final int timestamp) {
        return new Transaction(
                timestamp,
                description,
                null,
                null,
                amount,
                null,
                null,
                null,
                null,
                null,
                "cashWithdrawal",
                null,
                null
        );
    }

    /**
     * Creates a transaction for adding interest to an account.
     *
     * @param description the description of the transaction.
     * @param amount      the interest amount added.
     * @param currency    the currency of the interest.
     * @param timestamp   the timestamp of the transaction.
     * @return a Transaction representing the addition of interest to an account.
     */
    public static Transaction addInterestTransaction(final String description,
                                                     final double amount,
                                                     final String currency,
                                                     final int timestamp) {
        return new Transaction(
                timestamp,
                description,
                null,
                null,
                amount,
                currency,
                null,
                null,
                null,
                null,
                "addInterest",
                null,
                null
        );
    }


    /**
     * Converts the transaction to a JSON representation.
     *
     * @return an ObjectNode representing the transaction in JSON format
     */
    public ObjectNode toJson() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode transactionNode = mapper.createObjectNode();

        transactionNode.put("timestamp", timestamp);
        transactionNode.put("description", description);

        if (senderIBAN != null) {
            transactionNode.put("senderIBAN", senderIBAN);
        }
        if (receiverIBAN != null) {
            transactionNode.put("receiverIBAN", receiverIBAN);
        }
        transactionNode.put("amount", amount);
        if (transferType != null) {
            transactionNode.put("transferType", transferType);
        }
        if (card != null) {
            transactionNode.put("card", card);
        }
        if (cardHolder != null) {
            transactionNode.put("cardHolder", cardHolder);
        }
        if (commerciant != null) {
            transactionNode.put("commerciant", commerciant);
        }

        return transactionNode;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getCard() {
        return card;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public List<Double> getAmounts() {
        return amounts;
    }
}

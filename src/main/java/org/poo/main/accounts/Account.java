package org.poo.main.accounts;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.cards.Card;
import org.poo.main.commandsPhase2.CashbackStrategy;
import org.poo.main.commandsPhase2.SpendingThresholdCashbackStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a generic bank account.
 * Designed to be safely extended by subclasses.
 */
public abstract class Account {
    private final List<Card> cards;
    private final List<Transaction> transactions;
    private String iban;
    private User owner;
    private double balance;
    private double totalSpent;
    private String currency;
    private double minBalance;
    private String planType;
    private CashbackStrategy cashbackStrategy;

    /**
     * Constructor for Account.
     *
     * @param iban     the account's IBAN
     * @param currency the account's currency
     * @param owner    the owner of the account
     */
    public Account(final String iban, final String currency, final User owner) {
        this.iban = iban;
        this.currency = currency;
        this.balance = 0.0;
        this.cards = new ArrayList<>();
        this.owner = owner;
        this.minBalance = 0.0;
        this.transactions = new ArrayList<>();
        this.planType = owner.getOccupation().equalsIgnoreCase("student")
                ? "student"
                : "standard";
        this.totalSpent = 0.0;
        this.cashbackStrategy = new SpendingThresholdCashbackStrategy();
    }

    /**
     * Sets the cashback strategy for the account.
     *
     * @param cashbackStrategy the cashback strategy to set
     */
    public void setCashbackStrategy(final CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Adds an amount to the total spent by the account.
     *
     * @param amount the amount to add
     */
    public void addToTotalSpent(final double amount) {
        this.totalSpent += amount;
    }

    /**
     * Retrieves the total spent by the account.
     *
     * @return the total spent as a double
     */
    public double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Retrieves the plan type of the account.
     *
     * @return the plan type as a String
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * Sets the plan type for the account.
     *
     * @param planType the plan type to set
     */
    public void setPlanType(final String planType) {
        this.planType = planType;
    }

    /**
     * Retrieves the owner of the account.
     *
     * @return the owner as a User object
     */
    public final User getOwner() {
        return owner;
    }

    /**
     * Abstract method to retrieve the account type.
     *
     * @return the account type as a String
     */
    public abstract String getAccountType();

    /**
     * Retrieves the IBAN of the account.
     *
     * @return the IBAN as a String
     */
    public final String getIBAN() {
        return iban;
    }

    /**
     * Sets the IBAN of the account.
     *
     * @param newIban the new IBAN to set
     */
    public final void setIBAN(final String newIban) {
        this.iban = newIban;
    }

    /**
     * Retrieves the balance of the account.
     *
     * @return the balance as a double
     */
    public final double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     *
     * @param balance the balance to set
     */
    public final void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * Retrieves the currency of the account.
     *
     * @return the currency as a String
     */
    public final String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account.
     *
     * @param currency the currency to set
     */
    public final void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Retrieves the list of cards associated with the account.
     *
     * @return a list of Card objects
     */
    public final List<Card> getCards() {
        return cards;
    }

    /**
     * Adds a card to the account.
     *
     * @param card the Card object to add
     */
    public final void addCard(final Card card) {
        this.cards.add(card);
    }

    /**
     * Removes a card from the account by its card number.
     *
     * @param cardNumber the card number to remove
     */
    public final void removeCard(final String cardNumber) {
        this.cards.removeIf(card -> card.getCardNumber().equals(cardNumber));
    }

    /**
     * Retrieves the minimum balance of the account.
     *
     * @return the minimum balance as a double
     */
    public final double getMinBalance() {
        return minBalance;
    }

    /**
     * Sets the minimum balance for the account.
     *
     * @param minBalance the minimum balance to set
     */
    public final void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * Deposits an amount into the account.
     *
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if the amount is not positive
     */
    public final void deposit(final double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    /**
     * Retrieves the list of transactions for the account.
     *
     * @return a list of Transaction objects
     */
    public final List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the account.
     *
     * @param transaction the Transaction object to add
     */
    public final void addTransaction(final Transaction transaction) {
        this.transactions.add(transaction);
    }
}

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
    private String planType; // Added plan type
    private CashbackStrategy cashbackStrategy;

    /**
     * Constructor for Account.
     *
     * @param iban     the account's IBAN.
     * @param currency the account's currency.
     * @param owner    the owner of the account.
     */
    public Account(final String iban, final String currency, final User owner) {
        this.iban = iban;
        this.currency = currency;
        this.balance = 0.0;
        this.cards = new ArrayList<>();
        this.owner = owner;
        this.minBalance = 0.0;
        this.transactions = new ArrayList<>();
        this.planType = owner.getOccupation().equalsIgnoreCase("student") ? "student" : "standard"; // Setăm plan implicit
        this.totalSpent = 0.0;
        this.cashbackStrategy = new SpendingThresholdCashbackStrategy();


    }

    public void setCashbackStrategy(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public double applyCashback(double amount, String merchantCategory) {
        return cashbackStrategy.calculateCashback(amount, this, merchantCategory);
    }

    public void addToTotalSpent(double amount) {
        this.totalSpent += amount;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public String getPlanType() {
        return planType;
    }

    /**
     * Sets the plan type for the account.
     *
     * @param planType the plan type to set.
     */
    public void setPlanType(String planType) {
        this.planType = planType;
    }
    /**
     * Gets the account owner.
     *
     * @return the User object representing the account owner.
     */
    public final User getOwner() {
        return owner;
    }

    /**
     * Sets the account owner.
     *
     * @param owner the User object to set as the owner.
     */
    public final void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * Gets the account type.
     *
     * @return the account type as a String.
     */
    public abstract String getAccountType();

    /**
     * Gets the account's IBAN.
     *
     * @return the IBAN as a String.
     */
    public final String getIBAN() {
        return iban;
    }

    /**
     * Sets the account's IBAN.
     *
     * @param iban the IBAN to set.
     */
    public final void setIBAN(final String iban) {
        this.iban = iban;
    }

    /**
     * Gets the account balance.
     *
     * @return the balance as a double.
     */
    public final double getBalance() {
        return balance;
    }

    /**
     * Sets the account balance.
     *
     * @param balance the balance to set.
     */
    public final void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * Gets the account currency.
     *
     * @return the currency as a String.
     */
    public final String getCurrency() {
        return currency;
    }

    /**
     * Sets the account currency.
     *
     * @param currency the currency to set.
     */
    public final void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets the cards associated with the account.
     *
     * @return a list of Card objects.
     */
    public final List<Card> getCards() {
        return cards;
    }

    /**
     * Adds a card to the account.
     *
     * @param card the Card object to add.
     */
    public final void addCard(final Card card) {
        this.cards.add(card);
    }

    /**
     * Removes a card from the account by card number.
     *
     * @param cardNumber the card number to remove.
     */
    public final void removeCard(final String cardNumber) {
        this.cards.removeIf(card -> card.getCardNumber().equals(cardNumber));
    }

    /**
     * Gets the minimum balance of the account.
     *
     * @return the minimum balance as a double.
     */
    public final double getMinBalance() {
        return minBalance;
    }

    /**
     * Sets the minimum balance of the account.
     *
     * @param minBalance the minimum balance to set.
     */
    public final void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * Deposits an amount into the account.
     *
     * @param amount the amount to deposit.
     */
    public final void deposit(final double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    /**
     * Gets the account's transactions.
     *
     * @return a list of Transaction objects.
     */
    public final List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the account.
     *
     * @param transaction the Transaction object to add.
     */
    public final void addTransaction(final Transaction transaction) {
        this.transactions.add(transaction);
    }
}

package org.poo.main;

import org.poo.main.accounts.Account;
import org.poo.main.splitpayments2.SplitPaymentStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Represents a user with accounts, transactions, and personal details.
 */
public class User {
    private final List<Account> accounts;
    private final Queue<SplitPaymentStatus> pendingTransactions;
    public String firstName;
    public String lastName;
    public String email;
    private Map<String, String> aliases;
    private List<Transaction> transactions;
    private String birthDate;
    private String occupation;
    private String planType;

    /**
     * Default constructor for User.
     */
    public User() {
        this.accounts = new ArrayList<>();
        this.planType = "standard";
        this.pendingTransactions = new LinkedList<>();
    }

    /**
     * Constructs a User with personal details.
     *
     * @param firstName  the user's first name
     * @param lastName   the user's last name
     * @param email      the user's email address
     * @param birthDate  the user's birth date in the format yyyy-MM-dd
     * @param occupation the user's occupation
     */
    public User(final String firstName, final String lastName, final String email,
                final String birthDate, final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.aliases = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.pendingTransactions = new LinkedList<>();
        this.birthDate = birthDate;
        this.occupation = occupation;
        this.planType = occupation.equalsIgnoreCase("student") ? "student" : "standard";
    }

    /**
     * Constructs a User with an email.
     *
     * @param email the user's email address
     */
    public User(final String email) {
        this.accounts = new ArrayList<>();
        this.email = email;
        this.pendingTransactions = new LinkedList<>();
    }

    /**
     * Calculates the user's age based on their birth date.
     *
     * @return the user's age in years
     */
    public int calculateAge() {
        LocalDate birthDateParsed = LocalDate.parse(this.birthDate);
        return Period.between(birthDateParsed, LocalDate.now()).getYears();
    }

    /**
     * Retrieves the user's plan type.
     *
     * @return the user's plan type
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * Updates the user's plan type.
     *
     * @param newPlanType the new plan type to set
     */
    public void upgradePlanType(final String newPlanType) {
        this.planType = newPlanType;
        for (Account account : accounts) {
            account.setPlanType(newPlanType);
        }
    }

    /**
     * Adds an account to the user's list of accounts.
     *
     * @param account the account to add
     */
    public void addAccount(final Account account) {
        this.accounts.add(account);
    }

    /**
     * Retrieves the user's accounts.
     *
     * @return a list of the user's accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Retrieves the user's transactions.
     *
     * @return a list of the user's transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the user's transaction history.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Retrieves the user's pending split payment transactions.
     *
     * @return a queue of pending transactions
     */
    public Queue<SplitPaymentStatus> getPendingTransactions() {
        return pendingTransactions;
    }

    /**
     * Adds a split payment transaction to the pending queue.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(final SplitPaymentStatus transaction) {
        pendingTransactions.add(transaction);
    }

    /**
     * Retrieves and removes the head of the pending transactions queue.
     *
     * @return the next transaction in the queue, or null if the queue is empty
     */
    public SplitPaymentStatus pollTransaction() {
        return pendingTransactions.poll();
    }

    /**
     * Retrieves, but does not remove, the head of the pending transactions queue.
     *
     * @return the next transaction in the queue, or null if the queue is empty
     */
    public SplitPaymentStatus peekTransaction() {
        return pendingTransactions.peek();
    }

    /**
     * Adds an alias for an account IBAN.
     *
     * @param alias       the alias name
     * @param accountIBAN the associated IBAN
     */
    public void addAlias(final String alias, final String accountIBAN) {
        aliases.put(alias, accountIBAN);
    }

    /**
     * Retrieves the IBAN associated with a given alias.
     *
     * @param alias the alias name
     * @return the associated IBAN, or null if not found
     */
    public String getIBANFromAlias(final String alias) {
        return aliases.get(alias);
    }

    /**
     * Retrieves the user's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Retrieves the user's occupation.
     *
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }
}

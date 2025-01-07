package org.poo.main;

import org.poo.main.accounts.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    private final List<Account> accounts;
    private Map<String, String> aliases;
    private List<Transaction> transactions;


    public User() {
        this.accounts = new ArrayList<>();
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.aliases = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds an account to the user's list of accounts.
     *
     * @param account the account to be added
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
     * @param transaction the transaction to be added
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Adds an alias for an account IBAN.
     *
     * @param alias       the alias name
     * @param accountIBAN the account IBAN associated with the alias
     */
    public void addAlias(final String alias, final String accountIBAN) {
        aliases.put(alias, accountIBAN);
    }

    /**
     * Retrieves the IBAN associated with a given alias.
     *
     * @param alias the alias name
     * @return the IBAN associated with the alias, or null if not found
     */
    public String getIBANFromAlias(final String alias) {
        return aliases.get(alias);
    }

    /**
     * Retrieves the user's first name.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the new first name to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the new last name to set
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the user's email.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email address to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }
}

package org.poo.main.accounts;

import org.poo.main.User;

/**
 * A factory class for creating different types of accounts.
 */
public final class AccountFactory {

    // Private constructor to prevent instantiation
    private AccountFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates an account based on the specified type.
     *
     * @param type         the type of account to create ("savings" or "classic").
     * @param iban         the IBAN of the account.
     * @param currency     the currency of the account.
     * @param interestRate the interest rate for savings accounts (ignored for classic accounts).
     * @param user         the owner of the account.
     * @return the created account.
     */
    public static Account createAccount(final String type, final String iban, final String currency,
                                        final double interestRate, final User user) {
        switch (type.toLowerCase()) {
            case "savings":
                return new SavingsAccount(iban, currency, interestRate, user);
            case "classic":
                return new ClassicAccount(iban, currency, user);
            default:
                throw new IllegalArgumentException("Invalid account type: " + type);
        }
    }
}

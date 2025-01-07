package org.poo.main.accounts;

import org.poo.main.User;

/**
 * Represents a classic account type.
 */
public class ClassicAccount extends Account {

    /**
     * Constructor for ClassicAccount.
     *
     * @param iban     the IBAN of the account.
     * @param currency the currency of the account.
     * @param user     the owner of the account.
     */
    public ClassicAccount(final String iban, final String currency, final User user) {
        super(iban, currency, user);
    }

    /**
     * Returns the type of the account as "classic".
     *
     * @return the account type as a String.
     */
    @Override
    public String getAccountType() {
        return "classic";
    }
}

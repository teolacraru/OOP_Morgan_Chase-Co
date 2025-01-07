package org.poo.main.accounts;

import org.poo.main.User;

/**
 * Represents a savings account with an associated interest rate.
 */
public class SavingsAccount extends Account {

    private double interestRate;

    /**
     * Constructs a SavingsAccount instance.
     *
     * @param iban          the IBAN of the account.
     * @param currency      the currency of the account.
     * @param interestRate  the interest rate of the account.
     * @param user          the owner of the account.
     */
    public SavingsAccount(final String iban,
                          final String currency,
                          final double interestRate,
                          final User user) {
        super(iban, currency, user);
        this.interestRate = interestRate;
    }

    /**
     * Returns the type of the account.
     *
     * @return the account type as "savings".
     */
    @Override
    public String getAccountType() {
        return "savings";
    }

    /**
     * Returns the current interest rate of the account.
     *
     * @return the interest rate.
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Updates the interest rate of the account.
     *
     * @param newInterestRate the new interest rate to set.
     */
    public void setInterestRate(final double newInterestRate) {
        this.interestRate = newInterestRate;
    }
}

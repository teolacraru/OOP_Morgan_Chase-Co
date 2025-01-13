package org.poo.main.commands;

import org.poo.main.CurrencyConverter;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;
/**
 * Command to send money between accounts.
 */
public class SendMoneyCommand implements Command {
    private final String accountIbanOrAlias;
    private final String receiverIbanOrAlias;
    private final double amount;
    private final int timestamp;
    private final String description;
    private final CurrencyConverter currencyConverter;
    private final List<User> users;

    /**
     * Constructor for SendMoneyCommand.
     *
     * @param accountIbanOrAlias   the sender's IBAN or alias.
     * @param amount               the amount to send.
     * @param receiverIbanOrAlias  the receiver's IBAN or alias.
     * @param timestamp            the timestamp of the transaction.
     * @param description          the description of the transaction.
     * @param currencyConverter    the currency converter utility.
     * @param users                the list of users.
     */
    public SendMoneyCommand(final String accountIbanOrAlias,
                            final double amount,
                            final String receiverIbanOrAlias,
                            final int timestamp,
                            final String description,
                            final CurrencyConverter currencyConverter,
                            final List<User> users) {
        this.accountIbanOrAlias = accountIbanOrAlias;
        this.amount = amount;
        this.receiverIbanOrAlias = receiverIbanOrAlias;
        this.timestamp = timestamp;
        this.description = description;
        this.currencyConverter = currencyConverter;
        this.users = users;
    }

    /**
     * Executes the command to send money.
     */
    @Override
    public void execute() {
        Account senderAccount = null;
        Account recieverAccount = null;

        String resolvedReceiverIBAN = receiverIbanOrAlias;
        for (User u : users) {
            String tempIBAN = u.getIBANFromAlias(receiverIbanOrAlias);
            if (tempIBAN != null) {
                resolvedReceiverIBAN = tempIBAN;
                break;
            }
        }

        for (User u : users) {
            String tempIBAN = u.getIBANFromAlias(accountIbanOrAlias);
            if (tempIBAN != null) {
                return;
            }
        }

        for (User u : users) {
            for (Account account : u.getAccounts()) {
                if (account.getIBAN().equals(accountIbanOrAlias)) {
                    senderAccount = account;
                    break;
                }
            }
        }

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(resolvedReceiverIBAN)) {
                    recieverAccount = account;
                    break;
                }
            }
        }
        if (senderAccount == null || recieverAccount == null) {
            return;
        }

        double commission = calculateCommission(senderAccount, amount);


        double finalAmount = amount;
        if(timestamp == 8) {
            System.out.println(commission);
        }
        if (!senderAccount.getCurrency().equals(recieverAccount.getCurrency())) {
            finalAmount = currencyConverter.convert(
                    amount, senderAccount.getCurrency(), recieverAccount.getCurrency());
        }
        double senderInitialAmount = senderAccount.getBalance();
        double recieverInitialAmount = recieverAccount.getBalance();
        if (senderInitialAmount - amount >= 0) {
            senderAccount.setBalance(senderInitialAmount - amount - commission);
            recieverAccount.setBalance(recieverInitialAmount + finalAmount);
            if(timestamp == 8) {
                System.out.println(senderAccount.getBalance() + " " + commission);
            }
        } else {
            Transaction transaction =
                    Transaction.addAccountTransaction(
                            timestamp, "Insufficient funds",
                            null, null);
            senderAccount.getOwner().addTransaction(transaction);
            senderAccount.addTransaction(transaction);
            return;
        }


        Transaction senderTransaction = Transaction.createSendMoneyTransaction(
                timestamp, description, senderAccount.getIBAN(), recieverAccount.getIBAN(),
                amount, senderAccount.getCurrency(), "sent"
        );
        senderAccount.getOwner().addTransaction(senderTransaction);
        senderAccount.addTransaction(senderTransaction);

        Transaction receiverTransaction = Transaction.createSendMoneyTransaction(
                timestamp, description, senderAccount.getIBAN(), recieverAccount.getIBAN(),
                finalAmount, recieverAccount.getCurrency(), "received"
        );
        recieverAccount.getOwner().addTransaction(receiverTransaction);
        recieverAccount.addTransaction(receiverTransaction);

    }

    /**
     * Calculates the commission based on the account's plan type.
     *
     * @param senderAccount the sender's account.
     * @param amount        the amount being sent.
     * @return the calculated commission.
     */
    private double calculateCommission(Account senderAccount, double amount) {
        String planType = senderAccount.getPlanType();
        double commission = 0.0;
        double amountInRON = senderAccount.getCurrency().equals("RON")
                ? amount
                : currencyConverter.convert(amount, senderAccount.getCurrency(), "RON");

        switch (planType.toLowerCase()) {
            case "standard":
                commission = amount * 0.002; // 0.2% comision
                break;

            case "student":
                commission = 0.0; // Fără comision
                break;

            case "silver":
                if (amountInRON >= 500) {
                    commission = amount * 0.001; // 0.1% comision pentru sume >= 500 RON
                } else {
                    commission = 0.0; // Fără comision pentru sume < 500 RON
                }
                break;

            case "gold":
                commission = 0.0; // Fără comision
                break;

            default:
                throw new IllegalArgumentException("Unknown plan type: " + planType);
        }

        return commission;
    }
}

package org.poo.main.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.main.CurrencyConverter;
import org.poo.main.ExchangeRate;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;
import org.poo.main.cards.CardFactory;

import java.util.List;
import java.util.Map;

/**
 * Command for processing online payments.
 */
public class PayOnlineCommand implements Command {
    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final int timestamp;
    private final String description;
    private final String commerciant;
    private final String email;
    private final List<ExchangeRate> exchangeRates;
    private final CurrencyConverter currencyConverter;
    private final ArrayNode output;
    private final Map<String, Double> commerciantTotals;
    private final List<User> users;

    /**
     * Constructor for PayOnlineCommand.
     *
     * @param cardNumber        the card number used for payment.
     * @param amount            the amount to be paid.
     * @param currency          the currency of the payment.
     * @param timestamp         the time of the payment.
     * @param description       the payment description.
     * @param commerciant       the merchant receiving the payment.
     * @param email             the email of the user.
     * @param users             the list of users.
     * @param exchangeRates     the exchange rates for currency conversion.
     * @param currencyConverter the currency converter.
     * @param output            the output JSON structure.
     * @param commerciantTotals the map of merchant totals.
     */
    public PayOnlineCommand(final String cardNumber,
                            final double amount,
                            final String currency,
                            final int timestamp,
                            final String description,
                            final String commerciant,
                            final String email,
                            final List<User> users,
                            final List<ExchangeRate> exchangeRates,
                            final CurrencyConverter currencyConverter,
                            final ArrayNode output,
                            final Map<String, Double> commerciantTotals) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
        this.users = users;
        this.exchangeRates = exchangeRates;
        this.currencyConverter = currencyConverter;
        this.output = output;
        this.commerciantTotals = commerciantTotals;
    }

    /**
     * Executes the online payment command.
     */
    @Override
    public void execute() {
        Account account = null;
        Card card = null;
        User user = null;

        for (User u : users) {
            for (Account ac : u.getAccounts()) {
                for (Card c : ac.getCards()) {
                    if (c.getCardNumber().equals(cardNumber)) {
                        account = ac;
                        card = c;
                        user = u;
                        break;
                    }
                }
            }
        }
        if (card == null) {
            throw new IllegalArgumentException("Card not found");
        }

        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("User is not the owner of the card");
        }

        if (card.getStatus().equals("frozen")) {
            Transaction transaction = Transaction.addAccountTransaction(
                    timestamp, "The card is frozen",
                    null, null);
            user.addTransaction(transaction);
            return;
        }
        double finalAmount;
        finalAmount = currencyConverter.convert(
                amount, currency, account.getCurrency());
        Transaction transactionCreated = null;
        Transaction transactionRemoved = null;
        if (account.getBalance() >= finalAmount) {
            account.setBalance(account.getBalance() - finalAmount);

            for (Card card1 : account.getCards()) {
                if (card1.getCardNumber().equals(cardNumber)
                        && card1.getType().equals("one-time")) {
                    Card card2 = CardFactory.createCard("one-time");
                    account.removeCard(cardNumber);
                    transactionRemoved = Transaction.createCardTransaction
                            (timestamp, "The card has been destroyed",
                                    account.getIBAN(), cardNumber, user.getEmail());
                    account.addCard(card2);
                    transactionCreated = Transaction.createCardTransaction(
                            timestamp, "New card created",
                            account.getIBAN(), card2.getCardNumber(), user.getEmail()
                    );
                }
            }

            if (account.getBalance() - account.getMinBalance() <= 30) {
                card.warning();
            }
            if (account.getBalance() <= account.getMinBalance()) {
                card.frozen();
                Transaction transaction = Transaction.addAccountTransaction(
                        timestamp, "The card is frozen", null, null
                );
                user.addTransaction(transaction);
                return;
            }

            Transaction transaction = Transaction.createPayOnlineTransaction(
                    timestamp, "Card payment", finalAmount, commerciant
            );
            account.getOwner().addTransaction(transaction);
            account.addTransaction(transaction);

            if (transactionRemoved != null) {
                account.getOwner().addTransaction(transactionRemoved);
            }
            if (transactionCreated != null) {
                account.getOwner().addTransaction(transactionCreated);
            }
        } else {
            Transaction transaction = Transaction.addAccountTransaction(
                    timestamp, "Insufficient funds", null, null
            );
            account.getOwner().addTransaction(transaction);
        }
    }
}

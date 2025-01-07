package org.poo.main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;

import java.util.List;

/**
 * Command to check the status of a card and update it based on account balance.
 */
public final class CheckCardStatusCommand implements Command {
    private static final int WARNING_THRESHOLD = 30;
    private final String cardNumber;
    private final int timestamp;
    private final List<User> users;
    private final ArrayNode output;

    /**
     * Constructs a CheckCardStatusCommand.
     *
     * @param cardNumber the card number to check.
     * @param timestamp  the current timestamp of the command.
     * @param users      the list of users in the system.
     * @param output     the output array to store results.
     */
    public CheckCardStatusCommand(final String cardNumber, final int timestamp,
                                  final List<User> users, final ArrayNode output) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.users = users;
        this.output = output;
    }

    /**
     * Executes the command to check and update the card status.
     */
    @Override
    public void execute() {
        Card card = null;
        User user = null;
        Account account = null;

        for (User user1 : users) {
            for (Account account1 : user1.getAccounts()) {
                for (Card card1 : account1.getCards()) {
                    if (card1.getCardNumber().equals(cardNumber)) {
                        card = card1;
                        user = user1;
                        account = account1;
                        break;
                    }
                }
            }
        }

        if (card == null) {
            addErrorToOutput("Card not found");
            return;
        }

        double balance = account.getBalance();
        double minBalance = account.getMinBalance();

        if (balance - minBalance <= WARNING_THRESHOLD && !card.getStatus().equals("frozen")) {
            Transaction transaction = Transaction.addAccountTransaction(
                    timestamp,
                    "You have reached the minimum amount of funds, the card will be frozen",
                    account.getIBAN(),
                    account.getCurrency()
            );
            user.addTransaction(transaction);
            card.warning();
        }

        if (balance <= minBalance) {
            card.frozen();
        }
    }

    /**
     * Adds an error message to the output.
     *
     * @param description the error description.
     */
    private void addErrorToOutput(final String description) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandOutput = mapper.createObjectNode();
        commandOutput.put("command", "checkCardStatus");

        ObjectNode outputDetails = mapper.createObjectNode();
        outputDetails.put("timestamp", timestamp);
        outputDetails.put("description", description);

        commandOutput.set("output", outputDetails);
        commandOutput.put("timestamp", timestamp);

        output.add(commandOutput);
    }
}

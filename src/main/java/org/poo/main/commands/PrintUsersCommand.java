package org.poo.main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;

import java.text.DecimalFormat;
import java.util.List;

public class PrintUsersCommand implements Command {
    private final List<User> users;
    private final int timestamp;
    private final ArrayNode output;

    /**
     * Constructs a PrintUsersCommand instance.
     *
     * @param users     the list of users to print information for
     * @param timestamp the timestamp of the command execution
     * @param output    the JSON array to append the command's result to
     */
    public PrintUsersCommand(final List<User> users, final int timestamp, final ArrayNode output) {
        this.users = users;
        this.timestamp = timestamp;
        this.output = output;
    }

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode userArray = mapper.createArrayNode();

        DecimalFormat df = new DecimalFormat("#.00");


        for (User user : users) {

            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accountsArray = mapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = mapper.createObjectNode();
                accountNode.put("IBAN", account.getIBAN());

                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                ArrayNode cardsArray = mapper.createArrayNode();
                for (Card card : account.getCards()) {
                    ObjectNode cardNode = mapper.createObjectNode();
                    cardNode.put("cardNumber", card.getCardNumber());
                    cardNode.put("status", card.getStatus());
                    cardsArray.add(cardNode);
                }

                accountNode.set("cards", cardsArray);
                accountsArray.add(accountNode);
            }

            userNode.set("accounts", accountsArray);
            userArray.add(userNode);
        }

        ObjectNode commandOutput = mapper.createObjectNode();
        commandOutput.put("command", "printUsers");
        commandOutput.set("output", userArray);
        commandOutput.put("timestamp", timestamp);

        output.add(commandOutput);
    }
}

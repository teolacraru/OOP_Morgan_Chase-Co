package org.poo.main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class PrintUsersCommand implements Command {
    private final List<User> users;
    private final int timestamp;
    private final ArrayNode output;

    public PrintUsersCommand(final List<User> users, final int timestamp, final ArrayNode output) {
        this.users = users;
        this.timestamp = timestamp;
        this.output = output;
    }

    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode userArray = mapper.createArrayNode();

        DecimalFormat df = new DecimalFormat("#.00"); // Formatare cu 2 zecimale


        for (User user : users) {

            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accountsArray = mapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = mapper.createObjectNode();
                accountNode.put("IBAN", account.getIBAN());
                //double balance = account.getBalance();
                //BigDecimal roundedBalance = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);
                accountNode.put("balance", account.getBalance());

//                if (roundedBalance.scale() > 0 && roundedBalance.stripTrailingZeros().scale() <= 0) {
//                    accountNode.put("balance", roundedBalance.intValue());
//                } else {
//                    accountNode.put("balance", roundedBalance.doubleValue());
//                }
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

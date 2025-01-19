package org.poo.main.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.main.Commerciant;
import org.poo.main.CurrencyConverter;
import org.poo.main.ExchangeRate;
import org.poo.main.User;
import org.poo.main.commandsPhase2.*;
import org.poo.main.splitpayments2.AcceptSplitPaymentCommand;
import org.poo.main.splitpayments2.RejectSplitPaymentCommand;
import org.poo.main.splitpayments2.SplitPaymentStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating command instances based on input.
 */
public final class CommandFactory {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CommandFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates and returns the appropriate command object based on the input.
     *
     * @param input             the command input data.
     * @param exchange          the list of exchange rates.
     * @param users             the list of users in the system.
     * @param currencyConverter the currency converter utility.
     * @param output            the output array for storing results.
     * @return the created command object.
     */
    public static Command getCommand(final CommandInput input,
                                     final List<ExchangeRate> exchange,
                                     final List<User> users,
                                     final CurrencyConverter currencyConverter,
                                     final List<Commerciant> commerciants,
                                     final ArrayNode output,
                                     final List<SplitPaymentStatus> activeTransactions) {
        final Map<String, Double> commerciantTotals = new HashMap<>();


        switch (input.getCommand()) {
            case "addAccount":
                return new AddAccountCommand(
                        input.getEmail(),
                        input.getCurrency(),
                        input.getAccountType() != null ? input.getAccountType() : "classic",
                        input.getTimestamp(),
                        "savings".equalsIgnoreCase(input.getAccountType())
                                ? input.getInterestRate() : 0,
                        users
                );
            case "printUsers":
                return new PrintUsersCommand(
                        users,
                        input.getTimestamp(),
                        output
                );

            case "createCard":
                return new CreateCardCommand(
                        input.getEmail(),
                        input.getAccount(),
                        "normal",
                        input.getTimestamp(),
                        users
                );

            case "createOneTimeCard":
                return new CreateCardCommand(
                        input.getEmail(),
                        input.getAccount(),
                        "one-time",
                        input.getTimestamp(),
                        users
                );

            case "addFunds":
                return new AddFundsCommand(
                        input.getAccount(),
                        input.getAmount(),
                        input.getTimestamp(),
                        users
                );

            case "deleteAccount":
                return new DeleteAccountCommand(
                        input.getAccount(),
                        input.getTimestamp(),
                        input.getEmail(),
                        users,
                        output
                );

            case "deleteCard":
                return new DeleteCardCommand(
                        input.getCardNumber(),
                        input.getTimestamp(),
                        users
                );

            case "setMinimumBalance":
                return new SetMinBalanceCommand(
                        input.getAmount(),
                        input.getAccount(),
                        input.getTimestamp(),
                        users
                );

            case "payOnline":
                CashbackManager cashbackManager = new CashbackManager();
                Commerciant commerciant = commerciants.stream()
                        .filter(c -> c.getName().equals(input.getCommerciant()))
                        .findFirst()
                        .orElse(null);

                CashbackStrategy strategy = (commerciant != null)
                        ? cashbackManager.getStrategy(commerciant.getType())
                        : new NoCashbackStrategy();

                return new PayOnlineCommand(
                        input.getCardNumber(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getTimestamp(),
                        input.getDescription(),
                        input.getCommerciant(),
                        input.getEmail(),
                        users,
                        exchange,
                        currencyConverter,
                        output,
                        commerciants,
                        strategy
                );


            case "sendMoney":
                return new SendMoneyCommand(
                        input.getAccount(),
                        input.getAmount(),
                        input.getReceiver(),
                        input.getTimestamp(),
                        input.getDescription(),
                        currencyConverter,
                        users
                );

            case "setAlias":
                return new SetAliasCommand(
                        input.getEmail(),
                        input.getAlias(),
                        input.getAccount(),
                        users
                );

            case "printTransactions":
                return new PrintTransactionsCommand(
                        input.getEmail(),
                        input.getTimestamp(),
                        users,
                        output
                );

            case "checkCardStatus":
                return new CheckCardStatusCommand(
                        input.getCardNumber(),
                        input.getTimestamp(),
                        users,
                        output
                );

            case "changeInterestRate":
                return new ChangeInterestRateCommand(
                        input.getTimestamp(),
                        input.getInterestRate(),
                        input.getAccount(),
                        users
                );

            case "splitPayment":
                String transactionId = "tx-" + input.getTimestamp(); // Poți genera un ID unic
                List<String> ibanList = input.getAccounts();

                // Mapăm IBAN-urile la email-urile utilizatorilor
                List<String> userEmails = new ArrayList<>();
                for (String iban : ibanList) {
                    for (User user : users) {
                        if (user.getAccounts().stream().anyMatch(account
                                -> account.getIBAN().equals(iban))) {
                            userEmails.add(user.getEmail());
                            break; // Trecem la următorul IBAN
                        }
                    }
                }
                SplitPaymentStatus status = new SplitPaymentStatus(
                        transactionId,
                        userEmails,
                        input.getTimestamp(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getSplitPaymentType(),
                        input.getAccounts(),
                        input.getAmountForUsers(),
                        users,
                        currencyConverter
                );

                activeTransactions.add(status);
                //System.out.println(input.getTimestamp());
                return new SplitPaymentCommand(
                        input.getTimestamp(),
                        input.getAmount(),
                        input.getCurrency(),
                        input.getSplitPaymentType(), // Adăugat
                        input.getAccounts(),
                        input.getAmountForUsers(),
                        users,
                        currencyConverter,
                        status

                );
            case "acceptSplitPayment":
//                if(input.getTimestamp() == 163){
//                    System.out.println(activeTransactions.size());
//                }
                return new AcceptSplitPaymentCommand(
                        input.getEmail(), // Email-ul utilizatorului care acceptă
                        input.getTimestamp(), // Timestamp-ul comenzii
                        activeTransactions,
                        users,
                        currencyConverter// Lista tranzacțiilor active
                );

            case "rejectSplitPayment":
                return new RejectSplitPaymentCommand(
                        input.getEmail(), // Email-ul utilizatorului care refuză
                        input.getTimestamp(), // Timestamp-ul comenzii
                        activeTransactions,
                        users// Lista tranzacțiilor active
                );

            case "report":
                return new ReportCommand(
                        input.getTimestamp(),
                        input.getStartTimestamp(),
                        input.getEndTimestamp(),
                        input.getAccount(),
                        users,
                        output
                );

            case "spendingsReport":
                return new SpendingsReportCommand(
                        input.getTimestamp(),
                        input.getStartTimestamp(),
                        input.getEndTimestamp(),
                        input.getAccount(),
                        users,
                        output
                );

            case "addInterest":
                return new AddInterestCommand(
                        input.getAccount(),
                        input.getTimestamp(),
                        users
                );
            case "cashWithdrawal":
                return new CashWithdrawalCommand(input.getCardNumber(),
                        input.getAmount(), input.getEmail(),
                        input.getLocation(), input.getTimestamp(),
                        users, currencyConverter);
            case "withdrawSavings":
                return new WithdrawSavingsCommand(input.getAccount(),
                        input.getAmount(), input.getCurrency(),
                        input.getTimestamp(), users, currencyConverter);
            case "upgradePlan":
                return new UpgradePlanCommand(input.getNewPlanType(),
                        input.getAccount(), input.getTimestamp(),
                        users, currencyConverter);
            default:
                throw new IllegalArgumentException("Unknown command: " + input.getCommand());
        }
    }
}

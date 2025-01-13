package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.*;
import org.poo.main.commands.Command;
import org.poo.main.commands.CommandFactory;
import org.poo.main.commandsPhase2.CashbackStrategy;
import org.poo.main.commandsPhase2.NoCashbackStrategy;
import org.poo.main.commandsPhase2.NrOfTransactionsCashbackStrategy;
import org.poo.main.commandsPhase2.SpendingThresholdCashbackStrategy;
import org.poo.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);


        UserInput[] userInputs = inputData.getUsers();
        CommandInput[] commands = inputData.getCommands();
        CommerciantInput[] commerciantInputs = inputData.getCommerciants();
        List<Commerciant> commerciants = new ArrayList<>();
        for (CommerciantInput commerciantInput : commerciantInputs) {
            CashbackStrategy cashbackStrategy;

            switch (commerciantInput.getCashbackStrategy()) {
                case "nrOfTransactions":
                    cashbackStrategy = new NrOfTransactionsCashbackStrategy();
                    break;
                case "spendingThreshold":
                    cashbackStrategy = new SpendingThresholdCashbackStrategy();
                    break;
                default:
                    cashbackStrategy = new NoCashbackStrategy();
                    break;
            }

            Commerciant commerciant = new Commerciant(
                    commerciantInput.getCommerciant(),
                    commerciantInput.getId(),
                    commerciantInput.getAccount(),
                    commerciantInput.getType(),
                    cashbackStrategy // Setează direct strategia
            );

            commerciants.add(commerciant);
        }


        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (ExchangeInput rate : inputData.getExchangeRates()) {
            exchangeRates.add(new ExchangeRate(rate.getFrom(), rate.getTo(), rate.getRate()));
        }
        CurrencyConverter currencyConverter = new CurrencyConverter(exchangeRates);

        List<User> users = new ArrayList<>();
        for (UserInput userInput : userInputs) {
            User user = new User(
                    userInput.getFirstName(),
                    userInput.getLastName(),
                    userInput.getEmail(),
                    userInput.getBirthDate(),
                    userInput.getOccupation()
            );

            users.add(user);
        }

        ArrayNode output = objectMapper.createArrayNode();

        for (CommandInput commandInput : commands) {
            try {
                Command command = CommandFactory.
                        getCommand(commandInput, exchangeRates, users, currencyConverter, commerciants, output);
                command.execute();

                ObjectNode successNode = objectMapper.createObjectNode();
                successNode.put("status", "success");
                successNode.put("message",
                        commandInput.getCommand() + " executed successfully.");
            } catch (Exception e) {

                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("command", commandInput.getCommand());
                ObjectNode errorOutput = objectMapper.createObjectNode();
                errorOutput.put("description", e.getMessage());
                errorOutput.put("timestamp", commandInput.getTimestamp());
                errorNode.set("output", errorOutput);
                errorNode.put("timestamp", commandInput.getTimestamp());
                output.add(errorNode);

            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
        Utils.resetRandom();
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        return Integer.parseInt(
                file.getName()
                        .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR)
        );
    }
}

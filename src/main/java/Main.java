import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Main {
    private static FireBase fireBase = FireBase.getInstance();

    public static void main(String[] args) {

        UserInterface inputerOutputer;
        boolean isTelegram = false;

        if (!isTelegram) {
            ConsoleIO consoleIO = new ConsoleIO();
            inputerOutputer = consoleIO;
        } else {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            TelegramIO telegramIO = new TelegramIO();
            inputerOutputer = telegramIO;
            try {
                telegramBotsApi.registerBot(telegramIO);
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
                //TODO: Запись ошиибок в логи, шатдавн.
            }
        }

        Bot bot = new Bot();
        Quiz quiz = new Quiz();

        Map<String, BiFunction<String[], User, String[]>> commands = new HashMap<>();
        commands.put("/start", bot::getWelcome);
        commands.put("/quiz", quiz::getQuestion);
        commands.put("/toRus", bot::getRussianTranslation);
        commands.put("/help", bot::getHelpMessage);
        commands.put("/random", bot::getRandomFromRange);
        commands.put("/echo", bot::getEcho);
        commands.put("/stat", quiz::getStats);
        commands.put("/authors", bot::getAuthors);

        Map<String, BiFunction<String[], User, String[]>> games = new HashMap<>();
        games.put("quiz", quiz::getAnswer);

        String[] input = {""};
        String[] arguments = {""};
        String command = "";
        BotMessage inputMessage;

        while (true) {
            inputMessage = inputerOutputer.getInput();

            if (inputMessage == null) {
                continue;
            }

            input = inputMessage.textOfMessage.split(" ");

            if (input.length == 0) {
                continue;
            }

            User currentUser = fireBase.getUser(inputMessage.chatId);
            if (currentUser == null) {
                fireBase.updateUser(new User(inputMessage.chatId));
                currentUser = fireBase.getUser(inputMessage.chatId);
            }

            command = input[0];
            if (input.length > 1)
                arguments = Arrays.copyOfRange(input, 1, input.length);

            String[] response = {"Invalid command"};

            if (currentUser.state.contains("Game")) {
                response = games.get(currentUser.state.split(" ")[1]).apply(input, currentUser);
            } else if (commands.containsKey(command)) {
                response = commands.get(command).apply(arguments, currentUser);
            }

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < response.length; i++)
                result.append(response[i] + " ");

            inputerOutputer.printMessage(new BotMessage(result.toString(), inputMessage.chatId));
        }
    }
}
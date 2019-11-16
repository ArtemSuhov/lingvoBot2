import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Main {
    public static FireBase fireBase = new FireBase();

    public static void main(String[] args) {

        UserInterface inputerOutputer;
        boolean isTelegram = true;

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

        Map<String, Function<String[], String[]>> commands = new HashMap<>();
        commands.put("/start", bot::getWelcome);
        commands.put("/quiz", quiz::getQuestion);
        commands.put("/toRus", bot::getRussianTranslation);
        commands.put("/help", bot::getHelpMessage);
        commands.put("/random", bot::getRandomFromRange);
        commands.put("/echo", bot::getEcho);
        commands.put("/authors", bot::getAuthors);

        Map<String, Function<String[], String[]>> games = new HashMap<>();
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

            if (games.containsKey(command.substring(1))) {
                arguments = new String[]{currentUser.id};
                response = commands.get(command).apply(arguments);
            } else if (commands.containsKey(command)) {
                response = commands.get(command).apply(arguments);
            }

            if (currentUser.state.contains("Game")) {
                arguments = new String[input.length + 1];

                for(int i = 0; i < input.length; i++){
                    arguments[i+1] = input[i];
                }
                arguments[0] = currentUser.id;

                response = games.get(currentUser.state.split(" ")[1]).apply(arguments);
            }

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < response.length; i++)
                result.append(response[i] + " ");

            inputerOutputer.printMessage(new BotMessage(result.toString(), inputMessage.chatId));
        }
    }
}
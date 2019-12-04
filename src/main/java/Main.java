import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.function.BiFunction;

public class Main {
    private static FireBase fireBase = FireBase.getInstance();
    private static String prefixCommand = "/";

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
        Bot bot = new Bot(fireBase);
        Quiz quiz = new Quiz();

        Map<UserState, Map<String, BiFunction<String[], User, String[]>>> states = new HashMap<>();

        Map<String, BiFunction<String[], User, String[]>> commands = new HashMap<>();
        commands.put(prefixCommand + "start", bot::getWelcome);
        commands.put(prefixCommand + "quiz", quiz::getQuestion);
        commands.put(prefixCommand + "toRus", bot::getRussianTranslation);
        commands.put(prefixCommand + "help", bot::getHelpMessage);
        commands.put(prefixCommand + "random", bot::getRandomFromRange);
        commands.put(prefixCommand + "echo", bot::getEcho);
        commands.put(prefixCommand + "stat", quiz::getStats);
        commands.put(prefixCommand + "authors", bot::getAuthors);
        commands.put(prefixCommand + "dayWord", bot::getDayWord);
        commands.put("", bot::getDefault);

        Map<String, BiFunction<String[], User, String[]>> quizCommands = new HashMap<>();
        quizCommands.put("", quiz::getAnswer);

        states.put(UserState.QUIZ, quizCommands);
        states.put(UserState.DEFAULT, commands);

        String input = "";
        BotMessage inputMessage;

        while (true) {
            inputMessage = inputerOutputer.getInput();

            if (inputMessage == null) {
                continue;
            }

            input = inputMessage.textOfMessage;

            if (input.length() == 0) {
                continue;
            }

            User currentUser = fireBase.getUser(inputMessage.chatId);
            if (currentUser == null) {
                fireBase.updateUser(new User(inputMessage.chatId));
                currentUser = fireBase.getUser(inputMessage.chatId);
            }

            String[] response;
            UserState currentState = currentUser.state;
            String[] inputArray = input.split(" ");
            String command = input.startsWith(prefixCommand) ? inputArray[0] : "";

            Boolean isThereCommand = states.get(currentState).containsKey(command);
            if (isThereCommand) {
                String[] arguments = {""};
                BiFunction<String[], User, String[]> function = states.get(currentState).get(command);

                if (inputArray.length > 1) {
                    arguments = Arrays.copyOfRange(inputArray, 1, inputArray.length);
                }

                response = input.startsWith(prefixCommand)
                        ? function.apply(arguments, currentUser)
                        : function.apply(inputArray, currentUser);
            } else {
                response = states.get(currentState).get("").apply(inputArray, currentUser);
            }

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < response.length; i++)
                result.append(response[i] + " ");

            inputerOutputer.printMessage(new BotMessage(result.toString(), inputMessage.chatId));
            Timer timer = new Timer();
            var reminder = new Reminder(fireBase, inputerOutputer);
            timer.schedule(reminder, 0, 10);
        }
    }
}
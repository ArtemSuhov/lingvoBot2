import com.google.inject.internal.cglib.core.$DefaultGeneratorStrategy;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        TelegramIO telegramIO = new TelegramIO();
        try {
            telegramBotsApi.registerBot(telegramIO);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        Bot bot = new Bot();

        Map<String, Function<String[], String[]>> commands = new HashMap<>();
        commands.put("/start", bot::getWelcome);
        commands.put("/toRus", bot::getRussianTranslation);
        commands.put("/help", bot::getHelpMessage);
        commands.put("/random", bot::getRandomFromRange);
        commands.put("/echo", bot::getEcho);
        commands.put("/authors", bot::getAuthors);

        String[] input = {""};
        String[] arguments = {""};
        String command = "";

        while (true) {
            if (telegramIO.getInput() != null) {
                input = telegramIO.getInput().split(" ");
                command = input[0];
                if (input.length > 1)
                    arguments = Arrays.copyOfRange(input, 1, input.length);

                String[] response = {"Invalid command"};
                if (commands.containsKey(command)) {
                    response = commands.get(command).apply(arguments);
                }

                StringBuilder result = new StringBuilder();
                for (int i = 0; i < response.length; i++)
                    result.append(response[i] + " ");
                telegramIO.printMessage(result.toString());
            }
        }
    }
}
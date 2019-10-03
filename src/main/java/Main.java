import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();
        Bot bot = new Bot();

        consoleIO.printMessage("You can write \"@help\",if you want to see the list of commands.\n");


        Map<String, Function<String[], String[]>> commands = new HashMap<>();
        commands.put("@toRus", bot::printRussianTranslation);
        commands.put("@help", bot::getHelpMessage);
        commands.put("@random", bot::getRandomFromRange);
        commands.put("@echo", bot::getEcho);
        commands.put("@authors", bot::getAuthors);

        String[] input = {""};
        String[] arguments = {""};
        String command = "";

        while (!command.equals("@exit")) {
            input = consoleIO.getInput().split(" ");
            command = input[0];
            if (input.length > 1)
                arguments = Arrays.copyOfRange(input, 1, input.length);

            if (commands.containsKey(command)) {
                String[] response = commands.get(command).apply(arguments);

                for (int i = 0; i < response.length; i++)
                    consoleIO.printMessage(response[i]);
            }
        }
    }
}
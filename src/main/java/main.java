import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {

        ConsoleIO.printMessage("You can write \"@help\",if you want to see the list of commands.\n");

        Map<String, Consumer<String>> commands = new HashMap<>();
        commands.put("@toRus", Bot::printRussianTranslation);
        commands.put("@random", Bot::printRandomFromRange);
        commands.put("@help", Bot::printHelp);
        commands.put("@echo", Bot::doEcho);
        commands.put("@authors", Bot::printAuthors);

        String command = "";
        while (!command.equals("@exit")) {
            command = ConsoleIO.getInput();

            if (commands.containsKey(command.split(" ")[0])) {
                commands.get(command.split(" ")[0]).accept(command);
            }
        }
    }
}
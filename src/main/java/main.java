import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class main {

    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();

        Map<String, Consumer<String>> commands = new HashMap<>();
        commands.put("@toRus", );
        commands.put("@exit", );
        commands.put("@help",  );
        commands.put("@echo", );
        commands.put("@authors",  );
        //Запихнуть делегаты

        String command = "";
        while (!command.equals("@exit")) {
            command = consoleIO.getInput();

            if (commands.containsKey(command.split(" ")[0])) {

            }
        }

        System.out.print("Input your name: ");
        System.out.print("You can write \"@help\",if you want to see the list of commands.\n");
    }

    private static void printAuthors() {
        System.out.println("Artem Sukhov and Roman Zemskov in 2019");
    }

    private static void printRandomFromRange(int min, int max) {
        System.out.println((int) ((Math.random() * ((max - min) + 1)) + min));
    }

    private static void printHelp() {
        System.out.println("@help - view the commands list");
        System.out.println("@echo text - print your text, that writed after this command");
        System.out.println("@random minValue maxValue - print a random number from range");
        System.out.println("@toRus text - translate English text into Russian");
        System.out.println("@authors - print the authors");
    }

    private static void doEcho(String text) {
        cons;
    }
}
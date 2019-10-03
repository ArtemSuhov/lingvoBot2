public class Bot {
    private static String helpMessage = "@help - view the commands list \n" +
            "@echo text - print your text, that writed after this command\n" +
            "@random minValue maxValue - print a random number from range\n" +
            "@toRus text - translate English text into Russian" +
            "@authors - print the authors\n";

    public static void printRussianTranslation(String command) {
        if (command.split(" ").length < 2) {
            ConsoleIO.printMessage("Wrong arguments!!!");
        } else {
            Translater translater = new Translater();
            ConsoleIO.printMessage(translater.translateToRus(command.substring(6)));
        }
    }

    public static void printAuthors(String command) {
        ConsoleIO.printMessage("Artem Sukhov and Roman Zemskov in 2019");
    }

    public static void printRandomFromRange(String command) {

        if (command.split(" ").length < 3) {
            ConsoleIO.printMessage("Wrong arguments!!!");
        } else {
            int min = Integer.parseInt(command.split(" ")[1]);
            int max = Integer.parseInt(command.split(" ")[2]);
            int randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
            ConsoleIO.printMessage(String.valueOf(randomNumber));
        }
    }

    public static void printHelp(String command) {
        ConsoleIO.printMessage(helpMessage);
    }

    public static void doEcho(String command) {
        if (command.split(" ").length < 2) {
            ConsoleIO.printMessage("Wrong arguments!!!");
        } else {
            ConsoleIO.printMessage(command.split(" ")[1]);
        }
    }
}
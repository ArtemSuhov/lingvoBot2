public class Bot {
    private static String[] helpMessage = new String[]{"/help - view the commands list \n" +
            "/echo text - print your text, that writed after this command\n" +
            "/random minValue maxValue - print a random number from range\n" +
            "/toRus text - translate English text into Russian\n" +
            "/quiz - play the quiz game\n" +
            "/stat - amount of the answered questions\n" +
            "/authors - print the authors"};

    private static String[] wrongMessage = new String[]{"Wrong arguments!!!"};

    private static String[] authors = new String[]{"Artem Sukhov and Roman Zemskov in 2019"};

    private static String[] welcome = new String[]{"You can write \"/help\",if you want to see the list of commands."};

    private static String[] defaultOut = new String[]{"Invalid command"};


    public String[] getWelcome(String[] args, User user) {
        return welcome;
    }

    public String[] getDefault(String[] args, User user) {
        return defaultOut;
    }

    public String[] getRussianTranslation(String[] args, User user) {
        if (args.length == 0) {
            return wrongMessage;
        } else {
            Translater translater = new Translater();
            return new String[]{translater.translateToRus(String.join(" ", args))};
        }
    }

    public String[] getAuthors(String[] args, User user) {
        return authors;
    }

    public String[] getRandomFromRange(String[] args, User user) {

        if (args.length != 2) {
            return wrongMessage;
        } else {
            int min = Integer.parseInt(args[0]);
            int max = Integer.parseInt(args[1]);
            int randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
            return new String[]{String.valueOf(randomNumber)};
        }
    }

    public String[] getHelpMessage(String[] args, User user) {
        return helpMessage;
    }

    public String[] getEcho(String[] args, User user) {
        return args;
    }
}
public class Bot {
    private static String[] helpMessage = new String[]{"@help - view the commands list \n" +
            "@echo text - print your text, that writed after this command\n" +
            "@random minValue maxValue - print a random number from range\n" +
            "@toRus text - translate English text into Russian" +
            "@authors - print the authors\n"};

    private static String[] wrongMessage = new String[]{"Wrong arguments!!!"};

    private static String[] authors = new String[]{"Artem Sukhov and Roman Zemskov in 2019"};

    public String[] printRussianTranslation(String[] args) {
        if (args.length == 0) {
            return wrongMessage;
        } else {
            Translater translater = new Translater();
            return new String[]{translater.translateToRus(String.join(" ", args))};
        }
    }

    public String[] getAuthors(String[] args) {
        return authors;
    }

    public String[] getRandomFromRange(String[] args) {

        if (args.length != 2) {
            return wrongMessage;
        } else {
            int min = Integer.parseInt(args[0]);
            int max = Integer.parseInt(args[1]);
            int randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
            return new String[]{String.valueOf(randomNumber)};
        }
    }

    public String[] getHelpMessage(String[] args) {
        return helpMessage;
    }

    public String[] getEcho(String[] args) {
        return args;
    }
}
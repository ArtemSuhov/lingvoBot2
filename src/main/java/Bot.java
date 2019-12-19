import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Bot {

    private FireBase fireBase;
    private Reminder reminder;

    public Bot(FireBase base, Reminder reminder){
        this.fireBase = base;
        this.reminder = reminder;
    }
    private static String[] helpMessage = new String[]{"/help - view the commands list \n" +
            "/echo text - print your text, that writed after this command\n" +
            "/random minValue maxValue - print a random number from range\n" +
            "/toRus text - translate English text into Russian\n" +
            "/quiz - play the quiz game\n" +
            "/stat - amount of the answered questions\n" +
            "/authors - print the authors" +
            "/dayWord hh:mm"};

    private static String[] wrongMessage = new String[]{"Wrong arguments!!!"};

    private static String[] authors = new String[]{"Artem Sukhov, Sofya Gorbunova and Alexander Khrushev in 2019"};

    private static String[] welcome = new String[]{"You can write \"/help\",if you want to see the list of commands."};

    private static String[] defaultOut = new String[]{"Invalid command"};

    private static String[] dayWord = new String[]{"Set time for the word of the day"};

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

    public String[] getDayWord(String[] args, User user){
        if (args.length == 0 || args[0].equals("")){
            return wrongMessage;
        }
        try{
         user.timeOfDay = LocalTime.parse(args[0], DateTimeFormatter.ISO_LOCAL_TIME);
        }
        catch (DateTimeParseException  e) {
            return wrongMessage;
        }
        user.isSentWord = false;
        user.isDayWordFinished = false;
        fireBase.updateUser(user, args[0]);
        reminder.updateAllUsers();
        return dayWord;
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
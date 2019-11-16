import java.util.Scanner;

public class ConsoleIO implements UserInterface {
    public boolean printMessage(BotMessage message) {
        System.out.println(message.textOfMessage);
        return true;
    }

    ;

    public BotMessage getInput() {
        Scanner in = new Scanner(System.in);
        BotMessage message = new BotMessage(in.nextLine(), "Console");
        return message;
    }

    ;
}

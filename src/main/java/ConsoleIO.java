import java.util.Scanner;

public class ConsoleIO implements UserInterface {
    public static boolean printMessage(String message){
        System.out.println(message);
        return true;
    };

    public static String getInput(){
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    };

    public static boolean getClickOnButton(Button button){
        return true;
    };
}

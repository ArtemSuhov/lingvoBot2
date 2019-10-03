import java.util.Scanner;

public class ConsoleIO implements UserInterface {
    public void printMessage(String message){
        System.out.println(message);
    };

    public String getInput(){
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    };

    public boolean getClickOnButton(Button button){
        return true;
    };
}

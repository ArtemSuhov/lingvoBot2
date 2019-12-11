import java.util.Date;
import java.util.Random;
import java.util.TimerTask;

public class Reminder extends TimerTask{

    FireBase fireBase;
    UserInterface userInterface;
    DayWord dayWord;
    public Reminder(FireBase fireBase, UserInterface userInterface, DayWord dayWord){
        this.fireBase = fireBase;
        this.userInterface = userInterface;
        this.dayWord = dayWord;
    }

    public String[] getAnswer(String[] args, User user){
        return null;
        //TODO: сделать метод для обработки ответа юзера
    }

    public void checkUsersTime(){
        var users = fireBase.getAllUsers();
        var random = new Random();

        for(var user: users){
            if (user.timeOfDay.equals("Default")){
                continue;
            }
            var userTime = user.timeOfDay.split(":");
            if(isTimeToSend(userTime, user.isSentWord)){
                if(!user.isSentWord) {
                    dayWord.beginGame(user);
                }
            }
            else{
                user.isDayWordFinished = false;
                user.isSentWord = false;
                fireBase.updateUser(user);
            }
        }
    }

    private Boolean isTimeToSend(String[] userTime, Boolean isSentWord){
        var hours = userTime[0];
        var minutes = userTime[1];
        var now = new Date();
        if(hours.equals(Integer.toString(now.getHours()))){
            if(minutes.equals(Integer.toString(now.getMinutes())))
                return true;
        }
        return false;
    }

    @Override
    public void run() {
        checkUsersTime();
    }
}

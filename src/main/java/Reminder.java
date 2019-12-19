import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Reminder extends TimerTask{

    FireBase fireBase;
    UserInterface userInterface;
    DayWord dayWord;
    List<User> users;
    public Reminder(FireBase fireBase, UserInterface userInterface, DayWord dayWord){
        this.fireBase = fireBase;
        this.userInterface = userInterface;
        this.dayWord = dayWord;
        updateAllUsers();
    }

    public void updateAllUsers(){
        users = new ArrayList<> (fireBase.getAllUsers());
    }

    public void checkUsersTime(){
        var currentUsers = users;
        for (var user : currentUsers) {
            if (user.timeOfDay == null) {
                continue;
            }
            var userTime = user.timeOfDay;
            if (isTimeToSend(userTime)) {
                if (!user.isSentWord) {
                    dayWord.beginGame(user);
                    updateAllUsers();
                }
            } else if (isTimeToReset(userTime)){
                user.isDayWordFinished = false;
                user.isSentWord = false;
                fireBase.updateUser(user);
                updateAllUsers();
            }
        }
    }

    private Boolean isTimeToReset(LocalTime userTime){
        var now = LocalTime.now();
        return now.isAfter(userTime.plusHours(1)) || now.equals(userTime.plusHours(1));
    }

    private Boolean isTimeToSend(LocalTime userTime){
        var now = LocalTime.now();
        return userTime.isBefore(now) || userTime.equals(now);
    }

    @Override
    public void run() {
        checkUsersTime();
    }
}

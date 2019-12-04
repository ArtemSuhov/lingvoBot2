import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;

public class Reminder extends TimerTask{

    FireBase fireBase;
    UserInterface userInterface;

    public Reminder(FireBase fireBase, UserInterface userInterface){
        this.fireBase = fireBase;
        this.userInterface = userInterface;
    }

    public void checkUsersTime(){
        var users = fireBase.getAllUsers();
        var random = new Random();
        for(var user: users){
            var userTime = user.timeOfDay.split(":");
            if(isTimeToSend(userTime, user.isSentWord)){
                if(!user.isSentWord) {
                    var randomInt = random.nextInt(fireBase.getCountOfQuestions())+1;
                    var question = fireBase.getQuestion(Integer.toString(randomInt));
                    userInterface.printMessage(new BotMessage(generateVariants(question), user.id));
                    user.isSentWord = true;
                }
            }
            else if(user.isSentWord) {

                user.isSentWord = false;
            }
        }
    }

    private String generateVariants(Question question){
        var random = new Random();
        var firstVariant = fireBase.getQuestion(Integer.toString(random.nextInt(fireBase.getCountOfQuestions())+1)).answer;
        var secondVariant = fireBase.getQuestion(Integer.toString(random.nextInt(fireBase.getCountOfQuestions())+1)).answer;
        var randomInt = random.nextInt(2)+1;
        if(randomInt == 2){
            return firstVariant + " " + question.answer +" " + secondVariant;
        }
        else
        {
            return question.answer + " " + firstVariant + " " + secondVariant;
        }

    }
    private Boolean isTimeToSend(String[] userTime, Boolean isSentWord){
        var hours = userTime[0];
        var minutes = userTime[1];
        var now = new Date();
        if(hours.equals(Integer.toString(now.getHours()))
                && minutes.equals(Integer.toString(now.getMinutes()))){
            return true;
        }
        return hours.compareTo(Integer.toString(now.getHours())) < 0 && !isSentWord;
    }

    @Override
    public void run() {
        checkUsersTime();
    }
}

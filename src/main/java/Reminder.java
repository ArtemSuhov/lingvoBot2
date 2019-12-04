import java.util.Date;
import java.util.Random;
import java.util.TimerTask;

public class Reminder extends TimerTask{

    FireBase fireBase;
    UserInterface userInterface;

    public Reminder(FireBase fireBase, UserInterface userInterface){
        this.fireBase = fireBase;
        this.userInterface = userInterface;
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
                    var randomInt = random.nextInt(fireBase.getCountOfQuestions())+1;
                    var question = fireBase.getQuestion(Integer.toString(randomInt));
                    userInterface.printMessage(new BotMessage("Try to choose the meaning of the word \""
                            + question.textOfQuestion
                            +"\"", user.id));
                    userInterface.printMessage(new BotMessage(generateVariants(question), user.id));
                    //user.changeState(UserState.CHOOSING);
                    user.isSentWord = true;
                    fireBase.updateUser(user);
                }
            }
            else if(user.isSentWord) {
                user.isSentWord = false;
                fireBase.updateUser(user);
            }
        }
    }

    private String generateVariants(Question question){
        var random = new Random();
        var firstQuestionNum = random.nextInt(fireBase.getCountOfQuestions())+1;
        int secondQuestionNum;
        var firstVariant = fireBase.getQuestion(Integer.toString(firstQuestionNum)).answer;
        do{
            secondQuestionNum = random.nextInt(fireBase.getCountOfQuestions())+1;
        }while (firstQuestionNum != secondQuestionNum);
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
        return hours.compareTo(Integer.toString(now.getHours())) < 0;
    }

    @Override
    public void run() {
        checkUsersTime();
    }
}

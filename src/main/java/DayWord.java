import java.util.Random;

public class DayWord {

    FireBase fireBase;
    UserInterface userInterface;
    String answer;

    public DayWord(FireBase fireBase, UserInterface userInterface){
        this.fireBase = fireBase;
        this.userInterface = userInterface;
    }

    public void beginGame(User user){
        var random = new Random();
        var randomInt = random.nextInt(fireBase.getCountOfQuestions())+1;
        var question = fireBase.getQuestion(Integer.toString(randomInt));
        this.answer = question.answer;
        userInterface.printMessage(new BotMessage("Try to choose the meaning of the word \""
                + question.textOfQuestion
                +"\"", user.id));
        userInterface.printMessage(new BotMessage(generateVariants(question), user.id));
        user.isSentWord = true;
        fireBase.updateUser(user);
    }

    public Boolean checkAnswer(String variant){
        return answer.equals(variant);
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
}

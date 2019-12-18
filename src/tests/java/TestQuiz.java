import org.junit.Assert;
import org.junit.Test;


public class TestQuiz{
    @Test
    public void testAllQuestionsAnswered(){
        FireBase base = FireBase.getInstance();
        int questionsNum = base.getCountOfQuestions();
        StringBuilder answeredQuestions = new StringBuilder("0 ");
        for (int i=1; i<=questionsNum; i++){
            answeredQuestions.append(i+" ");
        }
        User user = new User("UserId", answeredQuestions.toString());
        Quiz quiz = new Quiz();
        String[] result = quiz.getQuestion(new String[] {}, user);
        Assert.assertArrayEquals(new String[] {"You answered all questions!"}, result);
    }

    @Test
    public void testCorrectAnswer(){
        User user = new User("UserId", "0 1");
        Quiz quiz = new Quiz();
        String[] result = quiz.getAnswer(new String[] {"Строка"}, user);
        String[] result1 = quiz.getAnswer(new String[] {"строка"}, user);
        Assert.assertEquals(UserState.DEFAULT,user.state);
        Assert.assertArrayEquals(new String[] {"Your answer is right!"}, result);
        Assert.assertArrayEquals(new String[] {"Your answer is right!"}, result1);
    }

    @Test
    public void testWrongAnswer(){
        User user = new User("UserId", "0 1", UserState.QUIZ, null, false,false);
        Quiz quiz = new Quiz();
        String[] result = quiz.getAnswer(new String[] {"Wrong"}, user);
        Assert.assertEquals(UserState.QUIZ, user.state);
        Assert.assertArrayEquals(new String[] {"You are wrong! Try again! Or write", "/answer"}, result);
    }

    @Test
    public void testGetAnswer(){
        User user = new User("UserId", "0 1", UserState.QUIZ, null, false, false);
        Quiz quiz = new Quiz();
        String[] result = quiz.getAnswer(new String[] {"/answer"}, user);
        Assert.assertEquals(UserState.DEFAULT, user.state);
        Assert.assertArrayEquals(new String[] {"Right answer is", "Строка"}, result);
    }

    @Test
    public void testGetStat(){
        User user = new User("UserId", "0 1", UserState.QUIZ, null, false, false);
        Quiz quiz = new Quiz();
        String[] result = quiz.getStats(new String[] {}, user);
        Assert.assertArrayEquals(new String[] {"You have answered","1", "questions"}, result);
    }
}

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    UserState state;
    String answeredQuestions;
    LocalTime timeOfDay;
    Boolean isSentWord;
    Boolean isDayWordFinished;

    User(String id) {
        this.id = id;
        this.answeredQuestions = "0 ";
        this.state = UserState.DEFAULT;
        this.timeOfDay = null;
        this.isSentWord = false;
        this.isDayWordFinished = false;
    }

    User(String id, String answeredQuestions) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = UserState.DEFAULT;
        this.timeOfDay = null;
        this.isSentWord = false;
        this.isDayWordFinished = false;
    }

    User(String id, String answeredQuestions, UserState state, LocalTime time, boolean isSentWord, boolean isDayWordFinished) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = state;
        this.timeOfDay = time;
        this.isSentWord = isSentWord;
        this.isDayWordFinished = isDayWordFinished;
    }

    void addAnsweredQuestion(Integer id) {
        this.answeredQuestions += id.toString() + " ";
    }

    void changeState(UserState state) {
        this.state = state;
    }
}

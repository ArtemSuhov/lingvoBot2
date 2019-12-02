import java.util.ArrayList;
import java.util.List;

public class User {
    public static final String defaultTime = "Default";
    String id;
    UserState state;
    String answeredQuestions;
    String timeOfDay;

    User(String id) {
        this.id = id;
        this.answeredQuestions = "0 ";
        this.state = UserState.DEFAULT;
        this.timeOfDay = defaultTime;
    }

    User(String id, String answeredQuestions) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = UserState.DEFAULT;
        this.timeOfDay = defaultTime;
    }

    User(String id, String answeredQuestions, UserState state, String time) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = state;
        this.timeOfDay = time;
    }

    void addAnsweredQuestion(Integer id) {
        this.answeredQuestions += id.toString() + " ";
    }

    void changeState(UserState state) {
        this.state = state;
    }
}

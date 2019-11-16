import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    String state;
    String answeredQuestions;

    User(String id) {
        this.id = id;
        this.answeredQuestions = "0 ";
        this.state = "Null";
    }

    User(String id, String answeredQuestions) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = "Null";
    }

    User(String id, String answeredQuestions, String state) {
        this.id = id;
        this.answeredQuestions = answeredQuestions;
        this.state = state;
    }

    void addAnsweredQuestion(Integer id) {
        this.answeredQuestions += id.toString() + " ";
    }

    void changeState(String state) {
        this.state = state;
    }
}

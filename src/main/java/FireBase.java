import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FireBase implements UserDatabase {
    private static DatabaseReference databaseReference;
    private static FireBase instance;
    private final String usersPath = "Users";
    private final String questionsPath = "Questions";

    public static FireBase getInstance() {
        if (instance == null) {
            instance = new FireBase();
        }

        return instance;
    }

    private FireBase() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount =
                    new FileInputStream(System.getProperty("user.dir") +
                            "\\lingvomasterbot-firebase-adminsdk-8ll0a-087d4219a2.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://lingvomasterbot.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private String getString(String path, String id) {
        CompletableFuture<String> task = new CompletableFuture<>();

        databaseReference.child(path).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                task.complete(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Добавить логирование
            }
        });

        try {
            String response = task.get();
            return response;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    public User getUser(String id) {
        String response = this.getString(usersPath, id);
        UserState state;

        if (response == null) {
            return null;
        }

        String[] args = response.split(";");
        switch (args[1]){
            case "QUIZ":
                state = UserState.QUIZ;
                break;
            default:
                state = UserState.DEFAULT;
        }
        return new User(id, args[0], state, args[2]);
    }

    public Question getQuestion(String id) {
        String response = this.getString(questionsPath, id);

        if (response == null) {
            return null;
        }

        return new Question(Integer.parseInt(id), response.split(":")[0], response.split(":")[1]);
    }


    private void setString(String path, String id, String content) {
        DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //TODO: Добавить логирование
            }
        };

        databaseReference
                .child(path)
                .child(id)
                .setValue(content, completionListener);
    }

    public void updateUser(User user) {
        this.setString(usersPath, user.id, user.answeredQuestions + ";" + user.state + ";" + user.timeOfDay);
    }

    public Integer getCountOfQuestions() {
        String response = this.getString(questionsPath, "count");

        Integer countOfQuestions;

        if (response == null) {
            countOfQuestions = 0;
        } else {
            countOfQuestions = Integer.parseInt(response);
        }

        return countOfQuestions;
    }

    public void addQuestion(Question question) {
        Integer countOfQuestions = this.getCountOfQuestions();
        countOfQuestions += 1;
        this.setString(questionsPath, countOfQuestions.toString(), question.textOfQuestion + ":" + question.answer);
        this.setString(questionsPath, "count", countOfQuestions.toString());
    }
}

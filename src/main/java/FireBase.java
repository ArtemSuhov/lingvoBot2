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
                    new FileInputStream("C:\\Users\\Артем\\IdeaProjects\\java_bot\\lingvomasterbot-firebase-adminsdk-8ll0a-087d4219a2.json");
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public User getUser(String id) {
        CompletableFuture<String> task = new CompletableFuture<>();

        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
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

            if (response == null) {
                return null;
            }

            return new User(id, response.split(";")[0], response.split(";")[1]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    ;

    public Question getQuestion(String id) {
        CompletableFuture<String> task = new CompletableFuture<>();

        databaseReference.getParent().child("Questions").child(id).addValueEventListener(new ValueEventListener() {
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
            if (response == null) {
                return null;
            }

            return new Question(Integer.parseInt(id), response.split(":")[0], response.split(":")[1]);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    ;

    public void updateUser(User user) {

        DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //TODO: Добавить логирование
            }
        };

        databaseReference.child(user.id).setValue(user.answeredQuestions + ";" + user.state, completionListener);
    }

    ;
}

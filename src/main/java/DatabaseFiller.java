import org.python.util.PythonInterpreter;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;

public class DatabaseFiller {
    public static void main(String[] args) {
        addQuestions(2);
    }

    private static void addQuestions(Integer count) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            Writer pythonWriter = new StringWriter();
            pyInterp.setOut(pythonWriter);
            FireBase fireBase = FireBase.getInstance();
            Translater translater = new Translater();

            for (Integer i = 0; i < count; i++) {
                pyInterp.execfile(System.getProperty("user.dir") + "\\src\\main\\java\\parse_questions.py");

                String englishWord = pythonWriter.toString().split("\n")[i];
                String russianWord = translater.translateToRus(englishWord);
                Question question = new Question(1, englishWord, russianWord);

                fireBase.addQuestion(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
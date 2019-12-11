public class Quiz {
    private static FireBase fireBase = FireBase.getInstance();
    private static String answer = "/answer";

    public String[] getQuestion(String[] args, User user) {
        int numberOfQuestions = fireBase.getCountOfQuestions();
        int max = numberOfQuestions;
        int min = 1;
        Integer randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);

        String[] answered = user.answeredQuestions.split(" ");
        if (answered.length == numberOfQuestions + 1) {
            return new String[]{"You answered all questions!"};
        }

        while (user.answeredQuestions.contains(randomNumber.toString())) {
            randomNumber = (randomNumber + 1) % numberOfQuestions;
        }

        user.addAnsweredQuestion(randomNumber);
        user.changeState(UserState.QUIZ);
        fireBase.updateUser(user);

        return new String[]{"Translate the word",
                "\"" + fireBase.getQuestion(randomNumber.toString()).textOfQuestion + "\"",
                "into Russian"};
    }

    public String[] getStats(String[] args, User user) {
        return new String[]{"You have answered", String.valueOf(user.answeredQuestions.split(" ").length - 1),
                "questions"};
    }

    public String[] getAnswer(String[] args, User user) {
        String[] questionsIds = user.answeredQuestions.split(" ");
        String questionId = questionsIds[questionsIds.length - 1];

        Question currentQuestion = fireBase.getQuestion(questionId);
        if (currentQuestion.answer.toLowerCase().equals(args[0].toLowerCase())) {
            user.changeState(UserState.DEFAULT);
            fireBase.updateUser(user);
            return new String[]{"Your answer is right!"};
        } else if (answer.equals(args[0])) {
            user.changeState(UserState.DEFAULT);
            fireBase.updateUser(user);
            return new String[]{"Right answer is", currentQuestion.answer};
        }

        return new String[]{"You are wrong! Try again! Or write", answer};
    }
}

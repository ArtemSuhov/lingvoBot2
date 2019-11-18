public class Quiz {
    private static int numberOfQuestions = 5;
    private static FireBase fireBase = FireBase.getInstance();

    public String[] getQuestion(String[] args, User user) {
        int max = numberOfQuestions;
        int min = 1;
        Integer randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);

        if (user.answeredQuestions.length() == numberOfQuestions * 2 + 2) {
            return new String[]{"You answered all questions!"};
        }

        while (user.answeredQuestions.contains(randomNumber.toString())) {
            randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
        }

        user.addAnsweredQuestion(randomNumber);
        user.changeState("Game quiz " + randomNumber.toString());
        fireBase.updateUser(user);

        return new String[]{"Translate the word",
                "\"" + fireBase.getQuestion(randomNumber.toString()).textOfQuestion + "\"",
                "into Russian"};
    }

    public String[] getStats(String[] args, User user) {
        return new String[]{"You have answered", String.valueOf(user.answeredQuestions.length() / 2 - 1), "questions"};
    }

    public String[] getAnswer(String[] args, User user) {
        String questionId = user.state.split(" ")[2];

        Question currentQuestion = fireBase.getQuestion(questionId);

        if (currentQuestion.answer.toLowerCase().equals(args[0].toLowerCase())) {
            user.changeState("Null");
            fireBase.updateUser(user);
            return new String[]{"Your answer is right!"};
        } else if ("answer".equals(args[0])) {
            user.changeState("Null");
            fireBase.updateUser(user);
            return new String[]{"Right answer is", currentQuestion.answer};
        }

        return new String[]{"You are wrong! Try again! Or write \"answer\""};
    }
}

public class Quiz {

    public String[] getQuestion(String[] args) {
        int max = 5;
        int min = 1;
        Integer randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
        User currentUser = Main.fireBase.getUser(args[0]);

        if (currentUser.answeredQuestions.length() == 10) {
            return new String[]{"You answered all questions!"};
        }

        while (currentUser.answeredQuestions.contains(randomNumber.toString())) {
            randomNumber = (int) ((Math.random() * ((max - min) + 1)) + min);
        }

        currentUser.addAnsweredQuestion(randomNumber);
        currentUser.changeState("Game quiz " + randomNumber.toString());
        Main.fireBase.updateUser(currentUser);

        return new String[]{"What is the", Main.fireBase.getQuestion(randomNumber.toString()).textOfQuestion, "in Russian?"};
    }

    public String[] getAnswer(String[] args) {
        User currentUser = Main.fireBase.getUser(args[0]);
        String questionId = currentUser.state.split(" ")[2];

        Question currentQuestion = Main.fireBase.getQuestion(questionId);

        if (currentQuestion.answer.equals(args[1])) {
            currentUser.changeState("Null");
            Main.fireBase.updateUser(currentUser);
            return new String[]{"Your answer is right!"};
        } else if ("answer".equals(args[1]))
        {
            currentUser.changeState("Null");
            Main.fireBase.updateUser(currentUser);
            return new String[] {"Right answer is", currentQuestion.answer};
        }

        return new String[] {"You are wrong! Try again! Or write \"answer\""};
    }
}

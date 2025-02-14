package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Answers {
    private List<Answer> answerList;

    public Answers() {
        this.answerList = new ArrayList<>();
    }

    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }

    public void removeAnswer(int id) {
        answerList.removeIf(a -> a.getId() == id);
    }

    public Answer findAnswerById(int id) {
        return answerList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Answer> getAllAnswers() {
        return answerList;
    }

    // **New Method: Retrieve All Answer IDs**
    public List<Integer> getAllAnswerIds() {
        return answerList.stream()
                .map(Answer::getId)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Answers: " + answerList;
    }
}

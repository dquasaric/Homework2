package application;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String text;
    private boolean isResolved; // New field
    private List<Answer> answers;

    public Question(int id, String text) {
        this.id = id;
        this.text = text;
        this.isResolved = false;
        this.answers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void markResolved() {
        this.isResolved = true;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void removeAnswer(int answerId) {
        answers.removeIf(answer -> answer.getId() == answerId);
    }

    public List<Answer> getAcceptedAnswers() {
        List<Answer> acceptedAnswers = new ArrayList<>();
        for (Answer answer : answers) {
            if (answer.isAccepted()) {
                acceptedAnswers.add(answer);
            }
        }
        return acceptedAnswers;
    }

    @Override
    public String toString() {
        return "Question ID: " + id + ", Text: " + text + ", Resolved: " + isResolved + ", Answers: " + answers.size();
    }
}

package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Questions {
    private List<Question> questionList;

    public Questions() {
        this.questionList = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        if (question != null) {
            questionList.add(question);
        }
    }

    public void removeQuestion(int id) {
        questionList.removeIf(q -> q.getId() == id);
    }

    public Question findQuestionById(int id) {
        return questionList.stream()
                .filter(q -> q.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Question> getAllQuestions() {
        return new ArrayList<>(questionList); // Return copy for safety
    }

    public List<Question> getUnresolvedQuestions() {
        return questionList.stream()
                .filter(q -> !q.isResolved())
                .collect(Collectors.toList());
    }

    public List<Question> getResolvedQuestions() {
        return questionList.stream()
                .filter(Question::isResolved)
                .collect(Collectors.toList());
    }

    public List<Question> searchQuestions(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>();
        return questionList.stream()
                .filter(q -> q.getText().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Questions: " + questionList;
    }
}

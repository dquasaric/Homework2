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
        questionList.add(question);
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
        return questionList;
    }

    // **Fix: Add getUnresolvedQuestions() method**
    public List<Question> getUnresolvedQuestions() {
        return questionList.stream()
                .filter(q -> !q.isResolved()) // Check for unresolved questions
                .collect(Collectors.toList());
    }

    // **Fix: Add getResolvedQuestions() method**
    public List<Question> getResolvedQuestions() {
        return questionList.stream()
                .filter(Question::isResolved) // Check for resolved questions
                .collect(Collectors.toList());
    }

    // **Fix: Add searchQuestions() method**
    public List<Question> searchQuestions(String keyword) {
        return questionList.stream()
                .filter(q -> q.getText().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Questions: " + questionList;
    }
}

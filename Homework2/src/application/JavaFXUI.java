package application;

import database.DatabaseHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class JavaFXUI extends Application {
    private TextArea outputArea;
    private TextField questionInput, questionIdInput, updateQuestionInput, searchInput;
    private TextField answerInput, answerIdInput, answerQuestionIdInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Q&A System");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Question Management Fields
        questionInput = new TextField();
        questionInput.setPromptText("Enter new question");

        updateQuestionInput = new TextField();
        updateQuestionInput.setPromptText("Enter new question text");

        questionIdInput = new TextField();
        questionIdInput.setPromptText("Enter Question ID");

        Button addQuestionButton = new Button("Add Question");
        addQuestionButton.setOnAction(e -> addQuestion());

        Button updateQuestionButton = new Button("Update Question");
        updateQuestionButton.setOnAction(e -> updateQuestion());

        Button deleteQuestionButton = new Button("Delete Question");
        deleteQuestionButton.setOnAction(e -> deleteQuestion());

        // Answer Management Fields
        answerQuestionIdInput = new TextField();
        answerQuestionIdInput.setPromptText("Enter Question ID for Answer");

        answerInput = new TextField();
        answerInput.setPromptText("Enter answer");

        answerIdInput = new TextField();
        answerIdInput.setPromptText("Enter Answer ID");

        Button addAnswerButton = new Button("Add Answer");
        addAnswerButton.setOnAction(e -> addAnswer());

        Button removeAnswerButton = new Button("Remove Answer");
        removeAnswerButton.setOnAction(e -> removeAnswer());

        Button acceptAnswerButton = new Button("Accept Answer");
        acceptAnswerButton.setOnAction(e -> acceptAnswer());

        // Searching and Retrieval
        searchInput = new TextField();
        searchInput.setPromptText("Search Questions");

        Button searchQuestionsButton = new Button("Search Questions");
        searchQuestionsButton.setOnAction(e -> searchQuestions());

        Button showAllQuestionsButton = new Button("Show All Questions");
        showAllQuestionsButton.setOnAction(e -> showAllQuestions());

        Button showUnresolvedQuestionsButton = new Button("Show Unresolved Questions");
        showUnresolvedQuestionsButton.setOnAction(e -> showUnresolvedQuestions());

        Button showResolvedQuestionsButton = new Button("Show Resolved Questions");
        showResolvedQuestionsButton.setOnAction(e -> showResolvedQuestions());

        outputArea = new TextArea();
        outputArea.setEditable(false);

        root.getChildren().addAll(
                questionInput, addQuestionButton,
                questionIdInput, updateQuestionInput, updateQuestionButton,
                deleteQuestionButton, searchInput, searchQuestionsButton, showAllQuestionsButton,
                showUnresolvedQuestionsButton, showResolvedQuestionsButton,
                answerQuestionIdInput, answerInput, addAnswerButton,
                answerIdInput, acceptAnswerButton, removeAnswerButton,
                outputArea
        );

        primaryStage.setScene(new Scene(root, 500, 700));
        primaryStage.show();
    }
    
 // Show Unresolved Questions (No accepted answers)
    private void showUnresolvedQuestions() {
        List<Question> unresolvedQuestions = DatabaseHelper.getUnresolvedQuestions();
        
        if (unresolvedQuestions.isEmpty()) {
            outputArea.setText("No unresolved questions.");
        } else {
            StringBuilder output = new StringBuilder("Unresolved Questions:\n");
            for (Question q : unresolvedQuestions) {
                output.append(q.getId()).append(". ").append(q.getText()).append("\n");
            }
            outputArea.setText(output.toString());
        }
    }

    // Show Resolved Questions (With accepted answers)
    private void showResolvedQuestions() {
        List<Question> resolvedQuestions = DatabaseHelper.getResolvedQuestions();
        
        if (resolvedQuestions.isEmpty()) {
            outputArea.setText("No resolved questions.");
        } else {
            StringBuilder output = new StringBuilder("Resolved Questions:\n");
            for (Question q : resolvedQuestions) {
                output.append(q.getId()).append(". ").append(q.getText()).append("\n");
            }
            outputArea.setText(output.toString());
        }
    }


    // Create a new question
    private void addQuestion() {
        String text = questionInput.getText().trim();
        if (text.isEmpty()) {
            outputArea.setText("Error: Please enter a question.");
            return;
        }

        if (DatabaseHelper.questionExists(text)) {
            outputArea.setText("Error: Duplicate question");
            return;
        }

        int questionId = (int) (Math.random() * 1000);
        Question question = new Question(questionId, text);
        if (DatabaseHelper.insertQuestion(question, 1)) {
            outputArea.setText("Question successfully added.");
        } else {
            outputArea.setText("Error: Could not add question.");
        }
    }

    // Update an existing question
    private void updateQuestion() {
        String newText = updateQuestionInput.getText().trim();
        int questionId;
        try {
            questionId = Integer.parseInt(questionIdInput.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Please enter a valid question ID.");
            return;
        }

        if (!DatabaseHelper.questionExistsById(questionId)) {
            outputArea.setText("Error: Question not found.");
            return;
        }

        DatabaseHelper.updateQuestion(questionId, newText);
        outputArea.setText("Question updated successfully.");
    }

    // Delete a question
    private void deleteQuestion() {
        int questionId;
        try {
            questionId = Integer.parseInt(questionIdInput.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Please enter a valid question ID.");
            return;
        }

        if (!DatabaseHelper.questionExistsById(questionId)) {
            outputArea.setText("Error: Question not found.");
            return;
        }

        DatabaseHelper.removeQuestion(questionId);
        outputArea.setText("Question removed successfully.");
    }

    // Add an answer to a question
    private void addAnswer() {
        String answerText = answerInput.getText().trim();
        int questionId;
        try {
            questionId = Integer.parseInt(answerQuestionIdInput.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Please enter a valid Question ID.");
            return;
        }

        if (!DatabaseHelper.questionExistsById(questionId)) {
            outputArea.setText("Error: Question not found.");
            return;
        }

        Answer answer = new Answer(0, answerText); // ID is now handled by SQLite
        if (DatabaseHelper.insertAnswer(answer, questionId)) {
            outputArea.setText("Answer added successfully.");
            displayAnswersForQuestion(questionId); // Show updated answers
        } else {
            outputArea.setText("Error: Could not add answer.");
        }
    }

    
 // New method to display answers for a question
    private void displayAnswersForQuestion(int questionId) {
        List<Answer> answers = DatabaseHelper.getAnswersByQuestionId(questionId);
        StringBuilder output = new StringBuilder("Question ID: " + questionId + " Answers:\n");

        if (answers.isEmpty()) {
            output.append("No answers yet.");
        } else {
            for (Answer ans : answers) {
                output.append(ans.getId()).append(". ").append(ans.getText()).append("\n");
            }
        }
        outputArea.setText(output.toString());
    }

    // Remove an answer
    private void removeAnswer() {
        int answerId;
        try {
            answerId = Integer.parseInt(answerIdInput.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Please enter a valid Answer ID.");
            return;
        }

        if (!DatabaseHelper.answerExistsById(answerId)) {
            outputArea.setText("Error: Answer not found.");
            return;
        }

        DatabaseHelper.removeAnswer(answerId);
        outputArea.setText("Answer removed successfully.");
    }

    // Accept an answer
    private void acceptAnswer() {
        int answerId;
        try {
            answerId = Integer.parseInt(answerIdInput.getText().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("Error: Please enter a valid Answer ID.");
            return;
        }

        if (!DatabaseHelper.answerExistsById(answerId)) {
            outputArea.setText("Error: Answer not found.");
            return;
        }

        DatabaseHelper.markAnswerAsAccepted(answerId);
        outputArea.setText("Answer successfully accepted.");
    }

    // Search for questions
    private void searchQuestions() {
        String keyword = searchInput.getText().trim();
        List<Question> results = DatabaseHelper.searchQuestions(keyword);

        if (results.isEmpty()) {
            outputArea.setText("No questions found.");
        } else {
            StringBuilder output = new StringBuilder("Search Results:\n");
            for (Question q : results) {
                output.append(q.getId()).append(". ").append(q.getText()).append("\n");
            }
            outputArea.setText(output.toString());
        }
    }

    // Show all questions
    private void showAllQuestions() {
        List<Question> questions = DatabaseHelper.getAllQuestions();
        if (questions.isEmpty()) {
            outputArea.setText("No questions found.");
        } else {
            StringBuilder output = new StringBuilder("All Questions:\n");
            for (Question q : questions) {
                output.append(q.getId()).append(". ").append(q.getText()).append("\n");
            }
            outputArea.setText(output.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

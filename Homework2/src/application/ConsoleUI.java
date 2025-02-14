package application;

import database.DatabaseHelper;
import database.QuestionDAO;
import database.AnswerDAO;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseHelper.createTables(); // Ensure tables exist
        System.out.println("Welcome to the Questions & Answers System!");

        while (true) {
            System.out.println("\nMAIN MENU:");
            System.out.println("1. Ask a question");
            System.out.println("2. View all questions");
            System.out.println("3. Search for a question");
            System.out.println("4. View answers for a question");
            System.out.println("5. Answer a question");
            System.out.println("6. Mark an answer as accepted");
            System.out.println("7. View unresolved questions");
            System.out.println("8. View resolved questions");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> askQuestion();
                case 2 -> viewAllQuestions();
                case 3 -> searchQuestion();
                case 4 -> viewAnswersForQuestion();
                case 5 -> answerQuestion();
                case 6 -> markAnswerAsAccepted();
                case 7 -> viewUnresolvedQuestions();
                case 8 -> viewResolvedQuestions();
                case 9 -> {
                    System.out.println("Exiting... Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void askQuestion() {
        System.out.print("Enter your question: ");
        String text = scanner.nextLine();
        int questionId = (int) (Math.random() * 1000);  // Random ID for simplicity
        Question question = new Question(questionId, text);
        QuestionDAO.insertQuestion(question, 1);
        System.out.println("Your question has been added!");
    }

    private static void viewAllQuestions() {
        List<Question> questions = QuestionDAO.getAllQuestions();
        if (questions.isEmpty()) {
            System.out.println("No questions found.");
        } else {
            for (Question q : questions) {
                System.out.println(q.getId() + ". " + q.getText());
            }
        }
    }

    private static void searchQuestion() {
        System.out.print("Enter a keyword to search: ");
        String keyword = scanner.nextLine();
        List<Question> results = QuestionDAO.searchSimilarQuestions(keyword);

        if (results.isEmpty()) {
            System.out.println("No matching questions found.");
        } else {
            System.out.println("Search Results:");
            for (Question q : results) {
                System.out.println(q.getId() + ". " + q.getText());
            }
        }
    }

    private static void viewAnswersForQuestion() {
        System.out.print("Enter question ID: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        List<Answer> answers = AnswerDAO.getAnswersByQuestionId(questionId);
        if (answers.isEmpty()) {
            System.out.println("No answers for this question.");
        } else {
            System.out.println("Answers:");
            for (Answer a : answers) {
                System.out.println(a.getId() + ". " + a.getText());
            }
        }
    }

    private static void answerQuestion() {
        System.out.print("Enter question ID to answer: ");
        int questionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter your answer: ");
        String text = scanner.nextLine();
        int answerId = (int) (Math.random() * 1000);  // Random ID for simplicity
        Answer answer = new Answer(answerId, text);
        AnswerDAO.insertAnswer(answer, questionId);
        System.out.println("Your answer has been added!");
    }

    private static void markAnswerAsAccepted() {
        System.out.print("Enter question ID: ");
        int questionId = scanner.nextInt();
        System.out.print("Enter answer ID to mark as accepted: ");
        int answerId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        boolean success = QuestionDAO.markAsResolved(questionId, answerId, 1);
        if (success) {
            System.out.println("Answer marked as accepted!");
        } else {
            System.out.println("Could not mark answer as accepted.");
        }
    }

    private static void viewUnresolvedQuestions() {
        List<Question> questions = QuestionDAO.getUnresolvedQuestionsWithAnswers();
        if (questions.isEmpty()) {
            System.out.println("No unresolved questions found.");
        } else {
            System.out.println("Unresolved Questions:");
            for (Question q : questions) {
                System.out.println(q.getId() + ". " + q.getText());
            }
        }
    }

    private static void viewResolvedQuestions() {
        List<Question> questions = QuestionDAO.getAllQuestions();
        boolean found = false;
        for (Question q : questions) {
            if (q.isResolved()) {
                System.out.println(q.getId() + ". " + q.getText());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No resolved questions found.");
        }
    }
}

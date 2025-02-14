package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseHelper;
import database.QuestionDAO;
import database.AnswerDAO;
import application.Question;
import application.Answer;

import java.util.List;

public class QuestionAnswerTests {

    @BeforeEach
    public void setUp() {
        System.out.println("\n[SETUP] Clearing and resetting database...");
        DatabaseHelper.clearDatabase();
        DatabaseHelper.createTables();
        System.out.println("[SETUP] Database is now clean.\n");
    }

    @Test
    public void testInsertAndRetrieveQuestion() {
        System.out.println("[TEST] Running testInsertAndRetrieveQuestion...");

        Question question = new Question(1, "What is Java?");
        QuestionDAO.insertQuestion(question, 1);
        
        List<Question> questions = QuestionDAO.getAllQuestions();
        System.out.println("[DEBUG] Retrieved Questions: " + questions.size());

        assertEquals(1, questions.size(), "[FAIL] Expected 1 question in database.");
        assertEquals("What is Java?", questions.get(0).getText(), "[FAIL] Retrieved question text does not match.");

        System.out.println("[PASS] Question inserted and retrieved correctly.\n");
    }

    @Test
    public void testInsertAndRetrieveAnswer() {
        System.out.println("[TEST] Running testInsertAndRetrieveAnswer...");

        Question question = new Question(1, "What is Java?");
        QuestionDAO.insertQuestion(question, 1);
        
        Answer answer = new Answer(1, "Java is a programming language.");
        AnswerDAO.insertAnswer(answer, 1);
        
        List<Answer> answers = AnswerDAO.getAnswersByQuestionId(1);
        System.out.println("[DEBUG] Retrieved Answers: " + answers.size());

        assertEquals(1, answers.size(), "[FAIL] Expected 1 answer in database.");
        assertEquals("Java is a programming language.", answers.get(0).getText(), "[FAIL] Retrieved answer text does not match.");

        System.out.println("[PASS] Answer inserted and retrieved correctly.\n");
    }

    @Test
    public void testMarkAnswerAsResolved() {
        System.out.println("[TEST] Running testMarkAnswerAsResolved...");

        Question question = new Question(1, "What is Java?");
        QuestionDAO.insertQuestion(question, 1);

        Answer answer = new Answer(1, "Java is a programming language.");
        AnswerDAO.insertAnswer(answer, 1);

        boolean result = QuestionDAO.markAsResolved(1, 1, 1);
        System.out.println("[DEBUG] Mark as resolved result: " + result);

        assertTrue(result, "[FAIL] Marking answer as resolved failed.");
        System.out.println("[PASS] Marking answer as resolved worked correctly.\n");
    }

    @Test
    public void testUnresolvedQuestionsRetrieval() {
        System.out.println("[TEST] Running testUnresolvedQuestionsRetrieval...");

        Question q1 = new Question(1, "What is Java?");
        Question q2 = new Question(2, "What is OOP?");
        QuestionDAO.insertQuestion(q1, 1);
        QuestionDAO.insertQuestion(q2, 1);

        List<Question> unresolved = QuestionDAO.getUnresolvedQuestionsWithAnswers();
        System.out.println("[DEBUG] Retrieved Unresolved Questions: " + unresolved.size());

        assertEquals(2, unresolved.size(), "[FAIL] Expected 2 unresolved questions in database.");
        System.out.println("[PASS] Unresolved questions retrieved correctly.\n");
    }

    @Test
    public void testSearchSimilarQuestions() {
        System.out.println("[TEST] Running testSearchSimilarQuestions...");

        Question q1 = new Question(1, "What is Java?");
        Question q2 = new Question(2, "Explain OOP principles.");
        QuestionDAO.insertQuestion(q1, 1);
        QuestionDAO.insertQuestion(q2, 1);

        List<Question> results = QuestionDAO.searchSimilarQuestions("Java");
        System.out.println("[DEBUG] Retrieved Similar Questions: " + results.size());

        assertEquals(1, results.size(), "[FAIL] Expected 1 similar question in search results.");
        assertEquals("What is Java?", results.get(0).getText(), "[FAIL] Retrieved question text does not match expected.");

        System.out.println("[PASS] Search for similar questions worked correctly.\n");
    }
}

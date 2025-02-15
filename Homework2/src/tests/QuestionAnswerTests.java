package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import database.DatabaseHelper;
import application.Question;
import application.Answer;

import java.util.List;

public class QuestionAnswerTests {

    @BeforeEach
    public void setUp() {
        System.out.println("\n[SETUP] Clearing and resetting database...");
        DatabaseHelper.createTables(); // Ensure tables exist
        System.out.println("[SETUP] Database is now clean.\n");
    }

    @Test
    public void testInsertAndRetrieveQuestion() {
        System.out.println("[TEST] Running testInsertAndRetrieveQuestion...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        List<Question> questions = DatabaseHelper.getAllQuestions();
        assertEquals(1, questions.size(), "[FAIL] Expected 1 question in database.");
        assertEquals("What is Java?", questions.get(0).getText(), "[FAIL] Retrieved question text does not match.");

        System.out.println("[PASS] Question inserted and retrieved correctly.\n");
    }

    @Test
    public void testDuplicateQuestion() {
        System.out.println("[TEST] Running testDuplicateQuestion...");

        Question question = new Question(1, "How does a refrigerator work?");
        assertTrue(DatabaseHelper.insertQuestion(question, 1), "[FAIL] Question should be added successfully.");
        
        Question duplicate = new Question(2, "How does a refrigerator work?");
        assertFalse(DatabaseHelper.insertQuestion(duplicate, 1), "[FAIL] Duplicate question should not be allowed.");

        System.out.println("[PASS] Duplicate question detection works correctly.\n");
    }

    @Test
    public void testUpdateQuestion() {
        System.out.println("[TEST] Running testUpdateQuestion...");

        Question question = new Question(1, "How does a refrigerator work?");
        DatabaseHelper.insertQuestion(question, 1);

        DatabaseHelper.updateQuestion(1, "What is the working principle of a refrigerator?");
        Question updatedQuestion = DatabaseHelper.getAllQuestions().get(0);

        assertEquals("What is the working principle of a refrigerator?", updatedQuestion.getText(), "[FAIL] Question was not updated correctly.");

        System.out.println("[PASS] Question updated successfully.\n");
    }

    @Test
    public void testDeleteQuestion() {
        System.out.println("[TEST] Running testDeleteQuestion...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        assertTrue(DatabaseHelper.removeQuestion(1), "[FAIL] Question should be removed.");
        assertFalse(DatabaseHelper.questionExistsById(1), "[FAIL] Question should no longer exist.");

        System.out.println("[PASS] Question removed successfully.\n");
    }

    @Test
    public void testInsertAndRetrieveAnswer() {
        System.out.println("[TEST] Running testInsertAndRetrieveAnswer...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        Answer answer = new Answer(1, "Java is a programming language.");
        DatabaseHelper.insertAnswer(answer, 1);

        List<Answer> answers = DatabaseHelper.getAnswersByQuestionId(1);
        assertEquals(1, answers.size(), "[FAIL] Expected 1 answer in database.");
        assertEquals("Java is a programming language.", answers.get(0).getText(), "[FAIL] Retrieved answer text does not match.");

        System.out.println("[PASS] Answer inserted and retrieved correctly.\n");
    }

    @Test
    public void testDeleteAnswer() {
        System.out.println("[TEST] Running testDeleteAnswer...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        Answer answer = new Answer(1, "Java is a programming language.");
        DatabaseHelper.insertAnswer(answer, 1);

        assertTrue(DatabaseHelper.removeAnswer(1), "[FAIL] Answer should be removed.");
        assertFalse(DatabaseHelper.answerExistsById(1), "[FAIL] Answer should no longer exist.");

        System.out.println("[PASS] Answer removed successfully.\n");
    }

    @Test
    public void testMarkAnswerAsAccepted() {
        System.out.println("[TEST] Running testMarkAnswerAsAccepted...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        Answer answer = new Answer(3, "Java is a programming language.");
        DatabaseHelper.insertAnswer(answer, 1);

        DatabaseHelper.markAnswerAsAccepted(3);

        List<Question> resolvedQuestions = DatabaseHelper.getResolvedQuestions();
        assertEquals(1, resolvedQuestions.size(), "[FAIL] Expected 1 resolved question.");
        
        System.out.println("[PASS] Answer marked as accepted correctly.\n");
    }

    @Test
    public void testUnresolvedQuestionsRetrieval() {
        System.out.println("[TEST] Running testUnresolvedQuestionsRetrieval...");

        Question q1 = new Question(1, "What is Java?");
        Question q2 = new Question(2, "What is OOP?");
        DatabaseHelper.insertQuestion(q1, 1);
        DatabaseHelper.insertQuestion(q2, 1);

        List<Question> unresolved = DatabaseHelper.getUnresolvedQuestions();
        assertEquals(2, unresolved.size(), "[FAIL] Expected 2 unresolved questions.");

        System.out.println("[PASS] Unresolved questions retrieved correctly.\n");
    }

    @Test
    public void testResolvedQuestionsRetrieval() {
        System.out.println("[TEST] Running testResolvedQuestionsRetrieval...");

        Question question = new Question(1, "What is Java?");
        DatabaseHelper.insertQuestion(question, 1);

        Answer answer = new Answer(3, "Java is a programming language.");
        DatabaseHelper.insertAnswer(answer, 1);

        DatabaseHelper.markAnswerAsAccepted(3);

        List<Question> resolved = DatabaseHelper.getResolvedQuestions();
        assertEquals(1, resolved.size(), "[FAIL] Expected 1 resolved question.");

        System.out.println("[PASS] Resolved questions retrieved correctly.\n");
    }

    @Test
    public void testSearchSimilarQuestions() {
        System.out.println("[TEST] Running testSearchSimilarQuestions...");

        Question q1 = new Question(1, "What is Java?");
        Question q2 = new Question(2, "Explain OOP principles.");
        DatabaseHelper.insertQuestion(q1, 1);
        DatabaseHelper.insertQuestion(q2, 1);

        List<Question> results = DatabaseHelper.searchQuestions("Java");
        assertEquals(1, results.size(), "[FAIL] Expected 1 similar question in search results.");
        assertEquals("What is Java?", results.get(0).getText(), "[FAIL] Retrieved question text does not match expected.");

        System.out.println("[PASS] Search for similar questions worked correctly.\n");
    }
}

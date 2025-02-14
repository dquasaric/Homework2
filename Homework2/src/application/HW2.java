package application;

public class HW2 {
    public static void main(String[] args) {
        Questions questions = new Questions();
        Answers answers = new Answers();

        // Create questions
        Question q1 = new Question(1, "What is Java?");
        Question q2 = new Question(2, "What is OOP?");
        Question q3 = new Question(3, "How to debug Java programs?");

        // Create answers
        Answer a1 = new Answer(1, "Java is a programming language.");
        Answer a2 = new Answer(2, "OOP stands for Object-Oriented Programming.");
        Answer a3 = new Answer(3, "Use a debugger in your IDE.");

        // Add answers to questions
        q1.addAnswer(a1);
        q2.addAnswer(a2);
        q3.addAnswer(a3);

        // Accept an answer
        a1.acceptAnswer();
        q1.markResolved(); // Mark question as resolved

        // Add questions to collection
        questions.addQuestion(q1);
        questions.addQuestion(q2);
        questions.addQuestion(q3);

        // Add answers to global answers collection
        answers.addAnswer(a1);
        answers.addAnswer(a2);
        answers.addAnswer(a3);

        // Display all questions
        System.out.println("All Questions:");
        for (Question q : questions.getAllQuestions()) {
            System.out.println(q);
        }

        // Display unresolved questions
        System.out.println("\nUnresolved Questions:");
        for (Question q : questions.getUnresolvedQuestions()) {
            System.out.println(q);
        }

        // Display resolved questions
        System.out.println("\nResolved Questions:");
        for (Question q : questions.getResolvedQuestions()) {
            System.out.println(q);
        }

        // Display accepted answers
        System.out.println("\nAccepted Answers for Question 1:");
        for (Answer a : q1.getAcceptedAnswers()) {
            System.out.println(a);
        }

        // Searching for a question
        System.out.println("\nSearch Results for 'Java':");
        for (Question q : questions.searchQuestions("Java")) {
            System.out.println(q);
        }
    }
}

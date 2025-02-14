# Questions & Answers System

## Overview
This project is a **Questions & Answers System**, allowing users to post questions, provide answers, and mark the best answers as accepted. The system is implemented using **Java** and an **SQLite database** for data persistence.

## Features
- Users can **ask** and **view** questions.
- Users can **answer** existing questions.
- Users can **search** for similar questions.
- Users can **view** all unresolved or resolved questions.
- Users can **mark answers as accepted**.
- The system is operated through a **console-based UI**.

## Technologies Used
- **Java** (Core application logic)
- **SQLite** (Database management)
- **JUnit** (Testing framework)

## File Structure
### **Application Logic**
- `Question.java` - Represents a question.
- `Answer.java` - Represents an answer.
- `Questions.java` - Manages a collection of questions.
- `Answers.java` - Manages a collection of answers.

### **Database Management**
- `DatabaseHelper.java` - Handles SQLite database connections and schema creation.
- `QuestionDAO.java` - Manages database operations related to questions.
- `AnswerDAO.java` - Manages database operations related to answers.
- `ReviewerDAO.java` - Handles reviewer-related operations.

### **Console UI**
- `ConsoleUI.java` - Provides an interactive command-line interface for users.

### **Testing**
- `QuestionAnswerTests.java` - Unit tests for verifying database and application functionality.

## Setup & Usage
### Prerequisites
- **Java 8+** installed.
- SQLite JDBC Driver included in the classpath.

### Running the Program via Terminal
1. **Compile the Java files:**
   ```sh
   javac -d bin $(find . -name "*.java")
   ```
2. **Run the console application:**
   ```sh
   java -cp bin application.ConsoleUI
   ```

### Running the Program via Eclipse
1. **Open Eclipse** and create a **new Java project**.
2. **Import** all `.java` files into the `src` folder.
3. **Add SQLite JDBC Driver** to the project's classpath:
   - Right-click on the project -> Build Path -> Add External JARs.
   - Select the SQLite JDBC driver file (`sqlite-jdbc.jar`).
4. **Run `ConsoleUI.java`**:
   - Right-click on `ConsoleUI.java` -> Run As -> Java Application.

### Running Tests
1. **Compile and run the test suite:**
   ```sh
   javac -cp .:junit-5.7.0.jar tests/QuestionAnswerTests.java
   java -cp .:junit-5.7.0.jar org.junit.runner.JUnitCore tests.QuestionAnswerTests
   ```


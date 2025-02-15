# Q&A System

## Overview
This project is a Java-based Q&A system that allows users to manage questions and answers using a JavaFX user interface and SQLite database.

## Features
- Add, update, and delete questions
- Add, remove, and accept answers
- Search for questions
- View resolved and unresolved questions
- Store data in an SQLite database

## Project Structure
```
application/
  Answer.java            - Represents an answer with ID, text, and accepted status
  Answers.java           - Manages a collection of answers
  Question.java          - Represents a question with ID, text, and answer list
  Questions.java         - Manages a collection of questions
  JavaFXUI.java          - Provides the graphical user interface using JavaFX

database/
  DatabaseHelper.java    - Handles database interactions using SQLite

tests/
  QuestionAnswerTests.java - JUnit test cases for the system
```

## Project requirements
- JavaFX
- SQLite JDBC Driver
- JUnit 5 (for testing)

## Setup Instructions
1. Ensure you have Java installed (JDK 11+ recommended).
2. Add JavaFX libraries to your project.
3. Ensure the SQLite JDBC driver is included.
4. Run `DatabaseHelper.main()` to initialize the database tables.
5. Run `JavaFXUI.main()` to launch the application.

## Usage
- Enter a question and click 'Add Question'.
- Enter an answer with the respective question ID and click 'Add Answer'.
- Click 'Accept Answer' to mark an answer as accepted.
- Use search to find questions.
- View all, resolved, or unresolved questions.

## Testing
Run `QuestionAnswerTests.java` using JUnit 5 to validate functionality.

## Contributors
- **Dara Gafoor - dgafoor@asu.edu**

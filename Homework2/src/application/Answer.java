package application;

public class Answer {
    private int id;
    private String text;
    private boolean isAccepted;

    // Default constructor for potential serialization
    public Answer() {}

    public Answer(int id, String text) {
        this.id = id;
        this.text = text;
        this.isAccepted = false;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) { // Added setter for flexibility
        this.text = text;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void acceptAnswer() {
        this.isAccepted = true;
    }

    @Override
    public String toString() {
        return "Answer ID: " + id + ", Text: " + text + ", Accepted: " + isAccepted;
    }
}

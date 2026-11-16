package Data;

public class InputItem {

    private InputType type;
    private String content;

    public InputItem(InputType type, String content) {

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        this.type = type;
        this.content = content.trim();
    }

    public InputType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Content: " + content;
    }
}
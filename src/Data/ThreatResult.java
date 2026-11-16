package Data;

public class ThreatResult
{
    private final String input;
    private final int score;
    private final String level;
    private final String description;

    public ThreatResult(String input, int score, String level, String description) {
        this.input = input;
        this.score = score;
        this.level = level;
        this.description = description;
    }

    public String getInput() {
        return input;
    }

    public int getScore() {
        return score;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }


}
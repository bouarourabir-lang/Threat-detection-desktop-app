package Results;
import Data.ThreatResult;
public class ResultFormatter
{
    public String formatResult(ThreatResult result) {

        if (result == null) {
            return "Error: No result available.";
        }

        StringBuilder formatted = new StringBuilder();

        formatted.append("========== ANALYSIS RESULT ==========\n");
        formatted.append("Input: ")
                .append(result.getInput())
                .append("\n");

        formatted.append("Risk Score: ")
                .append(result.getScore())
                .append("\n");

        formatted.append("Risk Level: ")
                .append(result.getLevel())
                .append("\n");

        formatted.append("Description: ")
                .append(result.getDescription())
                .append("\n");

        formatted.append("=====================================");

        return formatted.toString();
    }

    public void displayResult(ThreatResult result) {
        System.out.println(formatResult(result));
    }
}

package Semantic;

public class SemanticDetectionTest {

    public static void main(String[] args) throws Exception {

        try (SemanticAnalyzer analyzer = new SemanticAnalyzer()) {

            String message =
                    "Send us your card number and PIN to receive your prize.";
            System.out.println("Message: " + message);

            System.out.println(
                    "Detected Indicators: " + analyzer.analyze(message)
            );
        }
    }
}

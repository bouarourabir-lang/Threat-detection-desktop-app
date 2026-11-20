
package Semantic;

public class SemanticAnalyzerTest {

    public static void main(String[] args) throws Exception {

        try (SemanticAnalyzer analyzer = new SemanticAnalyzer()) {

            String message1 =
                    "Your account will be suspended if you do not verify it.";

            String message2 =
                    "Please confirm your account to avoid losing access.";

            // استخراج Embedding للرسالة الأولى
            float[] embedding1 =
                    analyzer.getEmbedding(message1);

            // استخراج Embedding للرسالة الثانية
            float[] embedding2 =
                    analyzer.getEmbedding(message2);

            // مقارنة الرسالتين
            double similarity =
                    CosineSimilarity.calculate(
                            embedding1,
                            embedding2
                    );

            System.out.println("Embedding 1 size: "
                    + embedding1.length);

            System.out.println("Embedding 2 size: "
                    + embedding2.length);

            System.out.println("Similarity: "
                    + similarity);
        }
    }
}

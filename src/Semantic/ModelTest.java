
package Semantic;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import java.nio.file.Paths;

public class ModelTest {

    public static void main(String[] args) throws Exception {

        String tokenizerPath =
                "models/all-MiniLM-L6-v2/tokenizer.json";

        HuggingFaceTokenizer tokenizer =
                HuggingFaceTokenizer.newInstance(
                        Paths.get(tokenizerPath)
                );

        String message =
                "Your account will be suspended. Verify your password immediately.";

        long[] tokens = tokenizer.encode(message).getIds();

        System.out.println("Original Message:");
        System.out.println(message);

        System.out.println("\nTokens:");
        for (long token : tokens) {
            System.out.print(token + " ");
        }

        System.out.println("\n\nNumber of Tokens: " + tokens.length);
    }
}

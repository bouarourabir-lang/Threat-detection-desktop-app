
package Semantic;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import ai.onnxruntime.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SemanticEmbeddingTest {

    public static void main(String[] args) throws Exception {

        String folder = "models/all-MiniLM-L6-v2/";

        // 1. Load Tokenizer
        HuggingFaceTokenizer tokenizer =
                HuggingFaceTokenizer.newInstance(
                        Paths.get(folder + "tokenizer.json")
                );

        // 2. Message to analyze
        String message =
                "Your account will be suspended immediately.";

        // 3. Convert message into tokens
        Encoding encoding = tokenizer.encode(message);

        long[] ids = encoding.getIds();
        long[] mask = encoding.getAttentionMask();
        long[] typeIds = encoding.getTypeIds();

        // Add batch dimension
        long[][] inputIds = {ids};
        long[][] attentionMask = {mask};
        long[][] tokenTypeIds = {typeIds};

        // 4. Load ONNX Model
        OrtEnvironment environment =
                OrtEnvironment.getEnvironment();

        try (OrtSession.SessionOptions options =
                     new OrtSession.SessionOptions();
             OrtSession session =
                     environment.createSession(
                             folder + "model.onnx", options);
             OnnxTensor idsTensor =
                     OnnxTensor.createTensor(environment, inputIds);
             OnnxTensor maskTensor =
                     OnnxTensor.createTensor(environment, attentionMask);
             OnnxTensor typesTensor =
                     OnnxTensor.createTensor(environment, tokenTypeIds)) {

            // 5. Prepare model inputs
            Map<String, OnnxTensor> inputs = new HashMap<>();

            inputs.put("input_ids", idsTensor);
            inputs.put("attention_mask", maskTensor);
            inputs.put("token_type_ids", typesTensor);

            // 6. Run model
            try (OrtSession.Result result = session.run(inputs)) {

                float[][][] output =
                        (float[][][]) result.get(0).getValue();

// Mean Pooling
                int vectorSize = output[0][0].length;

                float[] embedding = new float[vectorSize];

                int validTokens = 0;

                for (int i = 0; i < output[0].length; i++) {

                    // Ignore padding tokens
                    if (mask[i] == 1) {

                        validTokens++;

                        for (int j = 0; j < vectorSize; j++) {

                            embedding[j] += output[0][i][j];
                        }
                    }
                }

// Calculate average
                for (int j = 0; j < vectorSize; j++) {

                    embedding[j] /= validTokens;
                }

// L2 Normalization
                double norm = 0.0;

                for (int j = 0; j < vectorSize; j++) {

                    norm += embedding[j] * embedding[j];
                }

                norm = Math.sqrt(norm);

                for (int j = 0; j < vectorSize; j++) {

                    embedding[j] /= (float) norm;
                }
                double normalizedLength = 0.0;

                for (int j = 0; j < vectorSize; j++) {
                    normalizedLength += embedding[j] * embedding[j];
                }

                normalizedLength = Math.sqrt(normalizedLength);

                System.out.println(
                        "Normalized Vector Length: " + normalizedLength
                );

// Display result
                System.out.println("\nSentence Embedding:");

                for (int i = 0; i < 10; i++) {

                    System.out.println(embedding[i]);
                }

                System.out.println("\nEmbedding Size: " + embedding.length);

                System.out.println("Model executed successfully!");

                System.out.println(
                        "Number of Tokens: " + output[0].length
                );

                System.out.println(
                        "Vector Size: " + output[0][0].length
                );

                System.out.println("\nFirst Token Vector:");

                for (int i = 0; i < 10; i++) {
                    System.out.println(output[0][0][i]);
                }
            }
        }

        tokenizer.close();
    }
}

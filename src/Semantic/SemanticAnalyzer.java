
package Semantic;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import ai.onnxruntime.*;

import java.nio.file.Paths;
import java.util.*;

public class SemanticAnalyzer implements AutoCloseable {

    private HuggingFaceTokenizer tokenizer;
    private OrtEnvironment environment;
    private OrtSession session;

    // Embeddings of reference messages
    private Map<String, List<float[]>> referenceEmbeddings;

    // Initial threshold for testing
    private static final double THRESHOLD = 0.70;

    // Constructor
    public SemanticAnalyzer() throws Exception {

        String folder = "models/all-MiniLM-L6-v2/";

        tokenizer = HuggingFaceTokenizer.newInstance(
                Paths.get(folder + "tokenizer.json")
        );

        environment = OrtEnvironment.getEnvironment();

        try (OrtSession.SessionOptions options =
                     new OrtSession.SessionOptions()) {

            session = environment.createSession(
                    folder + "model.onnx",
                    options
            );
        }

        // Calculate reference embeddings only once
        referenceEmbeddings = new HashMap<>();

        for (Map.Entry<String, List<String>> entry :
                SemanticReference.REFERENCES.entrySet()) {

            List<float[]> embeddings = new ArrayList<>();

            for (String reference : entry.getValue()) {
                embeddings.add(getEmbedding(reference));
            }

            referenceEmbeddings.put(entry.getKey(), embeddings);
        }
    }

    // Convert a message into an embedding
    public float[] getEmbedding(String message) throws Exception {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        Encoding encoding = tokenizer.encode(message);

        long[] ids = encoding.getIds();
        long[] mask = encoding.getAttentionMask();
        long[] typeIds = encoding.getTypeIds();

        long[][] inputIds = {ids};
        long[][] attentionMask = {mask};
        long[][] tokenTypeIds = {typeIds};

        try (
                OnnxTensor idsTensor =
                        OnnxTensor.createTensor(environment, inputIds);

                OnnxTensor maskTensor =
                        OnnxTensor.createTensor(environment, attentionMask);

                OnnxTensor typeIdsTensor =
                        OnnxTensor.createTensor(environment, tokenTypeIds)
        ) {

            Map<String, OnnxTensor> inputs = new HashMap<>();

            inputs.put("input_ids", idsTensor);
            inputs.put("attention_mask", maskTensor);
            inputs.put("token_type_ids", typeIdsTensor);

            try (OrtSession.Result result = session.run(inputs)) {

                float[][][] output =
                        (float[][][]) result.get(0).getValue();

                int vectorSize = output[0][0].length;

                float[] embedding = new float[vectorSize];

                int validTokens = 0;

                // Mean Pooling
                for (int i = 0; i < output[0].length; i++) {

                    if (mask[i] == 1) {

                        validTokens++;

                        for (int j = 0; j < vectorSize; j++) {
                            embedding[j] += output[0][i][j];
                        }
                    }
                }

                for (int j = 0; j < vectorSize; j++) {
                    embedding[j] /= validTokens;
                }

                // L2 Normalization
                double norm = 0.0;

                for (int j = 0; j < vectorSize; j++) {
                    norm += embedding[j] * embedding[j];
                }

                norm = Math.sqrt(norm);

                if (norm == 0.0) {
                    throw new IllegalStateException(
                            "Cannot normalize a zero vector."
                    );
                }

// Check valid tokens before division
                if (validTokens == 0) {
                    throw new IllegalStateException(
                            "No valid tokens found."
                    );
                }

// Mean Pooling
                for (int j = 0; j < vectorSize; j++) {
                    embedding[j] /= validTokens;
                }

// L2 Normalization

                for (int j = 0; j < vectorSize; j++) {
                    norm += embedding[j] * embedding[j];
                }

                norm = Math.sqrt(norm);

                if (norm == 0.0) {
                    throw new IllegalStateException(
                            "Cannot normalize a zero vector."
                    );
                }

                for (int j = 0; j < vectorSize; j++) {
                    embedding[j] /= (float) norm;
                }


                return embedding;
            }
        }
    }

    // Detect semantic indicators
    public List<String> analyze(String message) throws Exception {

        float[] messageEmbedding = getEmbedding(message);

        List<String> detectedIndicators = new ArrayList<>();

        for (Map.Entry<String, List<float[]>> entry :
                referenceEmbeddings.entrySet()) {

            String category = entry.getKey();

            double maxSimilarity = -1.0;

            for (float[] referenceEmbedding : entry.getValue()) {

                double similarity =
                        CosineSimilarity.calculate(
                                messageEmbedding,
                                referenceEmbedding
                        );

                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                }
            }

            System.out.println(
                    category + " => " + maxSimilarity
            );

            if (maxSimilarity >= THRESHOLD) {
                detectedIndicators.add(category);
            }
        }

        return detectedIndicators;
    }

    // Close ONNX session
    @Override
    public void close() throws Exception {
        session.close();
    }
}

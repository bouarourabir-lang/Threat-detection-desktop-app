package Semantic;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

import java.nio.file.Paths;
import java.util.*;

public class SemanticAnalyzer implements AutoCloseable {

    private HuggingFaceTokenizer tokenizer;
    private OrtEnvironment environment;
    private OrtSession session;

    // Names of the inputs that the ONNX model really expects
    private Set<String> inputNames;

    // Embeddings of reference messages
    private Map<String, List<float[]>> referenceEmbeddings;

    // Similarity threshold (to be tuned with real tests)
    private static final double THRESHOLD = 0.70;

    // all-MiniLM-L6-v2 works best with at most 256 tokens
    private static final int MAX_TOKENS = 256;

    // Set to true only while testing
    private static final boolean DEBUG = false;

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

        inputNames = session.getInputNames();

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

    // Convert a message into a normalized embedding
    public float[] getEmbedding(String message) throws Exception {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        Encoding encoding = tokenizer.encode(message);

        // Limit the length of the input
        int length = Math.min(encoding.getIds().length, MAX_TOKENS);

        long[] ids = Arrays.copyOf(encoding.getIds(), length);
        long[] mask = Arrays.copyOf(encoding.getAttentionMask(), length);
        long[] typeIds = Arrays.copyOf(encoding.getTypeIds(), length);

        long[][] inputIds = {ids};
        long[][] attentionMask = {mask};
        long[][] tokenTypeIds = {typeIds};

        Map<String, OnnxTensor> inputs = new HashMap<>();

        try {
            inputs.put("input_ids",
                    OnnxTensor.createTensor(environment, inputIds));

            inputs.put("attention_mask",
                    OnnxTensor.createTensor(environment, attentionMask));

            // Only add token_type_ids if the model needs it
            if (inputNames.contains("token_type_ids")) {
                inputs.put("token_type_ids",
                        OnnxTensor.createTensor(environment, tokenTypeIds));
            }

            try (OrtSession.Result result = session.run(inputs)) {

                float[][][] output =
                        (float[][][]) result.get(0).getValue();

                int vectorSize = output[0][0].length;

                float[] embedding = new float[vectorSize];

                int validTokens = 0;

                // Mean Pooling (sum of valid tokens)
                for (int i = 0; i < output[0].length; i++) {

                    if (mask[i] == 1) {

                        validTokens++;

                        for (int j = 0; j < vectorSize; j++) {
                            embedding[j] += output[0][i][j];
                        }
                    }
                }

                if (validTokens == 0) {
                    throw new IllegalStateException(
                            "No valid tokens found."
                    );
                }

                // Mean Pooling (average) - done ONLY ONCE
                for (int j = 0; j < vectorSize; j++) {
                    embedding[j] /= validTokens;
                }

                // L2 Normalization - done ONLY ONCE
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

                for (int j = 0; j < vectorSize; j++) {
                    embedding[j] /= (float) norm;
                }

                return embedding;
            }

        } finally {
            // Always release the tensors
            for (OnnxTensor tensor : inputs.values()) {
                tensor.close();
            }
        }
    }

    // Split a message into sentences
    private List<String> splitIntoSegments(String message) {

        List<String> segments = new ArrayList<>();

        // The full message is always analyzed
        segments.add(message.trim());

        // Each sentence is analyzed too (long messages dilute the meaning)
        String[] parts = message.split("(?<=[.!?\\n])\\s+");

        if (parts.length > 1) {
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    segments.add(part.trim());
                }
            }
        }

        return segments;
    }

    // Detect semantic indicators
    public List<String> analyze(String message) throws Exception {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        // Embeddings of the full message and of each sentence
        List<float[]> segmentEmbeddings = new ArrayList<>();

        for (String segment : splitIntoSegments(message)) {
            segmentEmbeddings.add(getEmbedding(segment));
        }

        List<String> detectedIndicators = new ArrayList<>();

        for (Map.Entry<String, List<float[]>> entry :
                referenceEmbeddings.entrySet()) {

            String category = entry.getKey();

            double maxSimilarity = -1.0;

            for (float[] segmentEmbedding : segmentEmbeddings) {

                for (float[] referenceEmbedding : entry.getValue()) {

                    double similarity =
                            CosineSimilarity.calculate(
                                    segmentEmbedding,
                                    referenceEmbedding
                            );

                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                    }
                }
            }

            if (DEBUG) {
                System.out.println(category + " => " + maxSimilarity);
            }

            if (maxSimilarity >= THRESHOLD) {
                detectedIndicators.add(category);
            }
        }

        return detectedIndicators;
    }

    // Close ONNX session and tokenizer
    // (the OrtEnvironment is shared, so it is NOT closed)
    @Override
    public void close() throws Exception {

        if (session != null) {
            session.close();
        }

        if (tokenizer != null) {
            tokenizer.close();
        }
    }
}
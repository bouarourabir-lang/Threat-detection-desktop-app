package AnalyseMessage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import Data.ThreatResult;
import Semantic.SemanticAnalyzer;

public class MessageAnalyzer {

    // One shared SemanticAnalyzer instance
    private static SemanticAnalyzer semanticAnalyzer;

    // Create the analyzer only when needed
    private static synchronized SemanticAnalyzer getSemanticAnalyzer() {

        if (semanticAnalyzer == null) {
            try {
                semanticAnalyzer = new SemanticAnalyzer();
            } catch (Exception e) {
                throw new IllegalStateException("Could not initialize SemanticAnalyzer.", e);
            }
        }

        return semanticAnalyzer;
    }

      // Traditional + Semantic Detection
    private static List<String> detectHybridIndicators(String message) {

        String text = MessagePreprocessor.preprocess(message);

        // Traditional indicators
        List<String> traditionalIndicators = detectProcessedIndicators(text);

        // Semantic indicators
        List<String> semanticIndicators = new ArrayList<>();

        try {
            semanticIndicators = new ArrayList<>(getSemanticAnalyzer().analyze(message));

        } catch (Exception e) {
            // If the AI part fails, we still use the traditional result
            System.err.println("Semantic analysis unavailable: " + e.getMessage());
        }

        // even if the AI finds them similar to a credential request
        if (!traditionalIndicators.contains("Credential request")
                && containsAny(cleanText(text),
                "never share",
                "do not share",
                "dont share",
                "forgot my password",
                "password policy")) {

            semanticIndicators.remove("Credential request");
        }

        // Merge indicators without duplicates
        Set<String> allIndicators = new LinkedHashSet<>();

        allIndicators.addAll(traditionalIndicators);
        allIndicators.addAll(semanticIndicators);

        return new ArrayList<>(allIndicators);
    }

    public static int analyzeMessage(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        List<String> indicators = detectHybridIndicators(message);

        return MessageRiskScore.calculateRiskScore(
                indicators.contains("Suspicious keywords"),
                indicators.contains("Credential request"),
                indicators.contains("Urgency language"),
                indicators.contains("Threat language"));
    }

    public static ThreatResult analyzeMessageResult(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        List<String> indicators = detectHybridIndicators(message);

        int score = MessageRiskScore.calculateRiskScore(
                indicators.contains("Suspicious keywords"),
                indicators.contains("Credential request"),
                indicators.contains("Urgency language"),
                indicators.contains("Threat language")
        );

        String riskLevel = MessageRiskScore.getRiskLevel(score);

        String description;

        if (indicators.isEmpty()) {
            description = "No suspicious indicators detected.";
        } else {
            description = "Detected indicators: " + String.join(", ", indicators);
        }

        return new ThreatResult(message, score, riskLevel, description);
    }

    // Traditional detection only (kept for compatibility)
    public static List<String> detectIndicators(String message) {

        if (message == null || message.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String text = MessagePreprocessor.preprocess(message);

        return detectProcessedIndicators(text);
    }

    // Traditional rule-based detection

    private static List<String> detectProcessedIndicators(String text) {

        // Remove punctuation so "Act now!" becomes "act now"
        text = cleanText(text);

        List<String> indicators = new ArrayList<>();

        // Suspicious keywords
        if (containsAny(text,
                "you won a prize",
                "you won",
                "you have won",
                "youve won",
                "free gift",
                "gift card",
                "claim your reward",
                "claim your prize",
                "special reward",
                "selected as a winner",
                "verify your account",
                "account verification required",
                "make money from home",
                "guaranteed income",
                "earn money fast")) {

            indicators.add("Suspicious keywords");
        }

        // Credential request = a request word + a sensitive item,
        boolean hasRequestWord = containsAny(text,
                "share",
                "send",
                "provide",
                "give",
                "enter",
                "submit",
                "confirm",
                "tell",
                "reply with",
                "need",
                "needs",
                "require",
                "requires");

        boolean hasSensitiveItem = containsAny(text,
                "password",
                "otp",
                "one time password",
                "verification code",
                "security code",
                "secret word",
                "code that was sent",
                "code sent",
                "credit card",
                "card number",
                "card details",
                "card pin",
                "pin number",
                "bank account",
                "login credentials");

        boolean isWarning = containsAny(text,
                "never share",
                "do not share",
                "dont share",
                "forgot my password",
                "reset my password",
                "password policy");

        if (hasRequestWord && hasSensitiveItem && !isWarning) {

            indicators.add("Credential request");
        }

        // Urgency language
        if (containsAny(text,
                "act now",
                "immediately",
                "last chance",
                "within 24 hours",
                "urgent",
                "right away",
                "before the offer expires",
                "limited time",
                "expires today",
                "limited time only",
                "do not miss the deadline",
                "today only",
                "pay now",
                "your package is held")) {

            indicators.add("Urgency language");
        }

        // Threat language
        if (containsAny(text,
                "account will be suspended",
                "account will be deleted",
                "account is blocked",
                "account has been blocked",
                "has been suspended",
                "has been temporarily suspended",
                "account being blocked",
                "account being suspended",
                "lose access to your account",
                "access will be terminated",
                "access could be restricted",
                "account may become inaccessible",
                "result in loss of",
                "lead to account suspension",
                "prevent suspension")) {

            indicators.add("Threat language");
        }

        return indicators;
    }

    // Keep only letters, digits and spaces, with a space at each end
    private static String cleanText(String text) {

        return " " + text
                .replace("'", "")
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim() + " ";
    }

    // Check whole words only (so "otp" does not match "hotpot")
    private static boolean containsAny(
            String text, String... keywords) {

        for (String keyword : keywords) {

            if (text.contains(" " + keyword + " ")) {
                return true;
            }
        }

        return false;
    }

    // Close the shared SemanticAnalyzer when the application exits
    public static synchronized void closeSemanticAnalyzer() {

        if (semanticAnalyzer != null) {
            try {
                semanticAnalyzer.close();
            } catch (Exception e) {
                throw new IllegalStateException("Could not close SemanticAnalyzer.", e);
            } finally {
                semanticAnalyzer = null;
            }
        }
    }
}
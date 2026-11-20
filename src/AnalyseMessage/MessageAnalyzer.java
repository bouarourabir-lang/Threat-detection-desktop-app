
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
                throw new IllegalStateException(
                        "Could not initialize SemanticAnalyzer.", e
                );
            }
        }

        return semanticAnalyzer;
    }

    // Traditional + Semantic Detection
    private static synchronized List<String> detectHybridIndicators(
            String message) {

        String text = MessagePreprocessor.preprocess(message);

        // Traditional indicators
        List<String> traditionalIndicators =
                detectProcessedIndicators(text);

        // Semantic indicators
        List<String> semanticIndicators;

        try {
            semanticIndicators =
                    getSemanticAnalyzer().analyze(message);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Semantic analysis failed.", e
            );
        }

        // Merge indicators without duplicates
        Set<String> allIndicators = new LinkedHashSet<>();

        allIndicators.addAll(traditionalIndicators);
        allIndicators.addAll(semanticIndicators);

        return new ArrayList<>(allIndicators);
    }

    public static int analyzeMessage(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        List<String> indicators =
                detectHybridIndicators(message);

        return MessageRiskScore.calculateRiskScore(
                indicators.contains("Suspicious keywords"),
                indicators.contains("Credential request"),
                indicators.contains("Urgency language"),
                indicators.contains("Threat language")
        );
    }

    public static ThreatResult analyzeMessageResult(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        List<String> indicators =
                detectHybridIndicators(message);

        int score = MessageRiskScore.calculateRiskScore(
                indicators.contains("Suspicious keywords"),
                indicators.contains("Credential request"),
                indicators.contains("Urgency language"),
                indicators.contains("Threat language")
        );

        String riskLevel =
                MessageRiskScore.getRiskLevel(score);

        String description;

        if (indicators.isEmpty()) {
            description = "No suspicious indicators detected.";
        } else {
            description = "Detected indicators: "
                    + String.join(", ", indicators);
        }

        return new ThreatResult(
                message,
                score,
                riskLevel,
                description
        );
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

        List<String> indicators = new ArrayList<>();

        // Suspicious keywords
        if (containsAny(text,
                "congratulations",
                "you won a prize",
                "you won",
                "free gift",
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

        // Credential requests
        if (containsAny(text,
                "password",
                "otp",
                "verification code",
                "credit card",
                "credit card number",
                "card number",
                "card details",
                "card pin",
                "pin number",
                "bank account",
                "bank account details",
                "login credentials")) {

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
                "prevent suspension")) {

            indicators.add("Threat language");
        }

        return indicators;
    }


    private static boolean containsAny(
            String text, String... keywords) {

        for (String keyword : keywords) {

            if (text.contains(keyword)) {
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
                throw new IllegalStateException(
                        "Could not close SemanticAnalyzer.", e
                );
            } finally {
                semanticAnalyzer = null;
            }
        }
    }
}

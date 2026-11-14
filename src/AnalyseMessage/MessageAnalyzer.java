
package AnalyseMessage;

import java.util.ArrayList;
import java.util.List;

import Data.ThreatResult;
import AnalyseMessage.MessageRiskScore;

public class MessageAnalyzer {

    public static int analyzeMessage(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        String text = MessagePreprocessor.preprocess(message);

        List<String> indicators = detectProcessedIndicators(text);

        boolean hasSuspiciousKeywords =
                indicators.contains("Suspicious keywords");

        boolean requestsCredentials =
                indicators.contains("Credential request");

        boolean hasUrgency =
                indicators.contains("Urgency language");

        boolean hasThreat =
                indicators.contains("Threat language");

        return MessageRiskScore.calculateRiskScore(
                hasSuspiciousKeywords,
                requestsCredentials,
                hasUrgency,
                hasThreat
        );
    }

    public static ThreatResult analyzeMessageResult(String message) {

        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        String text = MessagePreprocessor.preprocess(message);

        List<String> indicators = detectProcessedIndicators(text);

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

    public static List<String> detectIndicators(String message) {

        if (message == null || message.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String text = MessagePreprocessor.preprocess(message);

        return detectProcessedIndicators(text);
    }

    private static List<String> detectProcessedIndicators(String text) {

        List<String> indicators = new ArrayList<>();

        // Suspicious keywords
        if (containsAny(text,
                "congratulations",
                "you won a prize",
                "free gift",
                "claim your reward",
                "verify your account")) {

            indicators.add("Suspicious keywords");
        }

        // Credential requests
        if (containsAny(text,
                "password",
                "otp",
                "verification code",
                "credit card",
                "credit card number",
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
                "right away")) {

            indicators.add("Urgency language");
        }

        // Threat language
        if (containsAny(text,
                "account will be suspended",
                "account will be deleted",
                "account is blocked",
                "account has been blocked")) {

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
}

package AnalyseMessage;

import Data.RiskLevel;

public class MessageRiskScore {
    public static int calculateRiskScore(
            boolean hasSuspiciousKeywords,
            boolean requestsCredentials,
            boolean hasUrgency,
            boolean hasThreat) {

        int score = 0;

        if (hasSuspiciousKeywords) {
            score += 2;
        }

        if (requestsCredentials) {
            score += 4;
        }

        if (hasUrgency) {
            score += 2;
        }

        if (hasThreat) {
            score += 3;
        }

        return score;
    }

    public static String getRiskLevel(int score) {

        if (score <= 2) {
            return RiskLevel.LOW.name();
        } else if (score <= 5) {
            return RiskLevel.MEDIUM.name();
        } else {
            return RiskLevel.HIGH.name();
        }
    }
}
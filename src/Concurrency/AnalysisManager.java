package Concurrency;

import AnalyseMessage.MessageAnalyzer;
import AnalyseUrl.URLRiskScore;
import Csv.CsvParserService;
import Data.InputItem;
import Data.ThreatResult;
import AnalyseUrl.URLValidation ;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class AnalysisManager {

    private List<ThreatResult> results;

    public AnalysisManager() {
        results = new ArrayList<>();
    }
    public ThreatResult analyzeInput(InputItem item) {

        String content = item.getContent();
        content = content.trim();

        ThreatResult result;

        switch (item.getType()) {

            case URL:
                result = analyzeUrl(content);
                break;

            case MESSAGE:
                result = MessageAnalyzer.analyzeMessageResult(content);
                break;

            default:
                throw new IllegalArgumentException("Unknown input type");
        }

        results.add(result);

        return result;
    }

    public List<ThreatResult> analyzeInputs(List<InputItem> items) {

        results.clear();

        for (InputItem item : items) {

            try {
                analyzeInput(item);

            } catch (Exception e) {
                System.out.println("Error analyzing input: " + item.getContent());
                System.out.println(e.getMessage());
            }
        }

        return results;
    }

    public List<ThreatResult> analyzeCsv(String filePath) throws IOException {

        CsvParserService csvParser = new CsvParserService();

        List<InputItem> items = csvParser.parseCsv(filePath);

        return analyzeInputs(items);
    }

    private ThreatResult analyzeUrl(String url) {

        URLValidation validation = new URLValidation();

        // Check if the URL is valid
        if (!validation.validateURL(url)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        try {
            // Extract URL information
            URI uri = new URI(url);

            String scheme = uri.getScheme();
            String host = uri.getHost();
            String path = uri.getPath();

            // Calculate risk score
            URLRiskScore riskScore = new URLRiskScore();

            int score = riskScore.calculateScore(scheme, host, url,path);

            // Get risk level and description
            String level = riskScore.Risklevel(score);
            String description = riskScore.getDescription();

            // Create the final result
            return new ThreatResult(url, score, level, description);

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
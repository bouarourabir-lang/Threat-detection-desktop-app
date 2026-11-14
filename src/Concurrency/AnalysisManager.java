package Concurrency;

import AnalyseMessage.MessageAnalyzer;
import AnalyseUrl.URLAnalyzer;
import Csv.CsvParserService;
import Data.ThreatResult;

import java.util.ArrayList;
import java.util.List;

public class AnalysisManager {
    private URLAnalyzer urlAnalyzer;
    private MessageAnalyzer messageAnalyzer;
    private CsvParserService csvParserService;
    private List<ThreatResult> results = new ArrayList<>();

    // Constructor
    public AnalysisManager() {
        urlAnalyzer = new URLAnalyzer();
        messageAnalyzer = new MessageAnalyzer();
        csvParserService = new CsvParserService();
    }


    public void analyzeURL(String url) {

    }

    public void analyzeMessage(String message) {

    }


    public void analyzeCSV(String filePath) {

    }


    public List<ThreatResult> getResults() {
        return results;
    }


    public void clearResults() {
        results.clear();
    }

    public int getResultsCount() {
        return results.size();
    }

    public void addResult(ThreatResult result) {
        if (result != null) {
            results.add(result);
        }
    }

    private boolean isValidInput(String input) {
        return input != null && !input.isBlank();
    }




}

package AnalyseMessage;

public class MessagePreprocessor {

    public static String preprocess(String message) {

        if (message == null) {
            return "";
        }

        return message
                .toLowerCase()
                // Normalize curly quotes
                .replace('\u2019', '\'')
                .replace('\u2018', '\'')
                .replace('\u201C', '"')
                .replace('\u201D', '"')
                .trim()
                .replaceAll("\\s+", " ");
    }

}
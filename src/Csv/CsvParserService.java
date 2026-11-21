package Csv;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import Data.InputItem;
import Data.InputType;
public class CsvParserService
{
    private List<String> errors = new ArrayList<>();

    public List<InputItem> parseCsv(String filePath) throws IOException {

        return parseCsv(Path.of(filePath));
    }

    public List<InputItem> parseCsv(Path filePath) throws IOException {

        errors.clear();

        // Check if file exists
        if (filePath == null || !Files.isRegularFile(filePath)) {
            throw new IOException("Missing file");
        }

        // Read CSV file
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        // Check if file is empty
        if (lines.isEmpty()) {
            throw new IOException("Empty CSV");
        }

        // Read header
        String header = lines.get(0).replace("\uFEFF", "").trim();

        List<String> columns = parseCsvLine(header);

        if (columns.size() != 2|| !columns.get(0).trim().equalsIgnoreCase("type") || !columns.get(1).trim().equalsIgnoreCase("content")) {

            throw new IOException("Invalid CSV format");
        }

        List<InputItem> items = new ArrayList<>();

        // Read data rows
        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i);

            try {
                List<String> values = parseCsvLine(line);

                // Check number of columns
                if (values.size() != 2) {
                    throw new IllegalArgumentException("Invalid row");
                }

                String typeValue = values.get(0).trim();
                String content = values.get(1).trim();

                // Check empty values
                if (typeValue.isEmpty() || content.isEmpty()) {
                    throw new IllegalArgumentException("Empty type or content");
                }

                // Convert string to InputType
                InputType type = InputType.valueOf(typeValue.toUpperCase(Locale.ROOT));

                // Create InputItem
                InputItem item = new InputItem(type, content);

                items.add(item);

            } catch (IllegalArgumentException e) {

                errors.add("Line " + (i + 1) + ": " + e.getMessage());
            }
        }

        return items;
    }

    // Handle commas inside quoted content
    private List<String> parseCsvLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '"') {

                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (c == ',' && !insideQuotes) {

                values.add(current.toString());
                current.setLength(0);

            } else {
                current.append(c);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException("Unclosed quotation");
        }

        values.add(current.toString());

        return values;
    }

    // Return errors found in CSV file
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
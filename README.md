# Multi-Threaded Threat Detection Desktop Application

## 1. Project Overview

The Multi-Threaded Threat Detection Desktop Application is a Java-based desktop application designed to analyze URLs and text messages to identify potentially suspicious or malicious content.

The application evaluates different inputs and assigns a risk level to help users identify potential security threats.

## 2. Main Features

- **URL Analysis:** Analyzes URLs to identify suspicious characteristics.
- **Message Analysis:** Examines text messages for potentially suspicious content.
- **Risk Classification:** Classifies detected threats into three risk levels: LOW, MEDIUM, and HIGH.
- **CSV File Analysis:** Allows users to upload CSV files containing multiple inputs for batch analysis.
- **Results Display:** Displays analysis results, including the risk level and analysis status.
- **Progress Tracking:** Provides visual feedback about the analysis progress.
- **Error Handling:** Handles invalid inputs and file-related errors.

## 3. Technologies Used

- **Java:** Main programming language.
- **JavaFX:** Used to develop the graphical user interface (GUI).
- **CSV:** Used for importing and processing multiple inputs.
- **ONNX Runtime:**  Used to run the AI model for suspicious message classification.
- **Tokenizer:**  Used to preprocess text messages before passing them to the AI model.
## 4. Project Structure

The project is organized into several packages:

| Package         | Description                         |
| AnalyseUrl      | URL analysis and validation         |
| AnalyseMessage  | Text message analysis               |
| CSV             | CSV file parsing                    |
| Data            | Input and result data models        |
| Concurrency     | Analysis management and integration |
| UI              | Graphical user interface            |

Main classes include:

- `URLValidation`
- `URLRiskScore`
- `MessageAnalyzer`
- `CsvParserService`
- `ThreatResult`
- `InputItem`
- `AnalysisManager`

## 5.Semantic Analysis Model

The project uses the all-MiniLM-L6-v2 model to generate semantic embeddings.

These embeddings represent the meaning of messages as numerical vectors.

Cosine Similarity is then used to compare the input message with predefined threat-related semantic categories.

The model runs locally using ONNX Runtime.

Model files are stored in:

models/all-MiniLM-L6-v2/

## 6. Processing Approach

In Sprint 1, the application uses sequential processing to analyze inputs and integrate the main application components.

## 7. Requirements

To run the application, the following are required:

- Java Development Kit (JDK).
- JavaFX SDK and required JavaFX libraries.
- IntelliJ IDEA or another compatible Java IDE.

Additional libraries may be required depending on the implemented modules.

## 8. How to Run

1. Download and extract the project ZIP file.
2. Open the project using IDE.
3. Make sure Maven dependencies are downloaded.
4. Verify that the local semantic model files are available in the models/all-MiniLM-L6-v2/ directory.
5. Run the JavaFX application using its configured main class.

## 9. Team Members

This project was developed as a group project.

- Lakhel aya manar: GUI Development
- Boudra narimene: URL Analysis
- Medjani malak: Message Analysis
- Benmessaoud nihel: CSV Parsing 
- Bouarour abir: Analysis Management and Integration + AI model  of messages analysis 

## 10. Application Workflow

- The user enters a message or URL, or imports a CSV file.

- The application validates and processes the input.

- The analysis manager distributes tasks to the appropriate analyzers.

- Messages and URLs are analyzed using their respective detection techniques.

- Semantic analysis provides additional information when applicable.

- The application calculates the risk score and determines the threat level.

- The final results are displayed in the graphical interface.
- 
## 11. Project Purpose

The main objective of this project is to develop a desktop application that helps users identify potentially dangerous URLs and suspicious messages through automated analysis and risk classification.

---

**Project:** Multi-Threaded Threat Detection Desktop Application
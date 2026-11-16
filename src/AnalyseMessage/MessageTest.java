
package AnalyseMessage;

import java.util.Scanner;
import java.util.List;
import Data.ThreatResult;

public class MessageTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== MESSAGE THREAT ANALYZER =====");

        System.out.print("Enter your message: ");
        String message = sc.nextLine();

        try {

            ThreatResult result =
                    MessageAnalyzer.analyzeMessageResult(message);

            System.out.println("\n===== ANALYSIS RESULT =====");

            System.out.println("Message: " + message);

            System.out.println("Risk Score: " + result.getScore());

            System.out.println("Risk Level: " + result.getLevel());

            System.out.println("Description: " + result.getDescription());

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        }

        sc.close();
    }
}

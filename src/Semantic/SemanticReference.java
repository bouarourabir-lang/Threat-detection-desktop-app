package Semantic;

import java.util.Map;
import java.util.List;

public class SemanticReference {

    public static final Map<String, List<String>> REFERENCES = Map.of(

            "Suspicious keywords", List.of(
                    "Congratulations! You won a free gift.",
                    "You have won a prize. Claim your reward now.",
                    "You have been selected to receive a special reward.",
                    "Claim your exclusive prize before it expires.",
                    "You are the lucky winner of a free gift.",
                    "You have been chosen to receive an exclusive reward.",
                    "You are entitled to claim a special bonus.",
                    "A valuable prize is waiting for you.",
                    "You have been selected as the winner of a promotional giveaway.",
                    "Claim your reward to receive your free gift."
            ),

            "Credential request", List.of(
                    "Please provide your password and verification code.",
                    "Enter your bank account details to verify your identity.",
                    "We need your login credentials to confirm your account.",
                    "Please submit your credit card information.",
                    "Enter your one-time password to complete verification.",
                    "To protect your account, enter your login password.",
                    "Please provide your username and password to continue.",
                    "Submit your security code to verify your identity.",
                    "Enter your account credentials to access the service.",
                    "We require your confidential login information."
            ),

            "Urgency language", List.of(
                    "Act now! You must respond immediately.",
                    "This is your last chance. Complete the action right away.",
                    "You must take action without delay.",
                    "Respond as soon as possible to avoid problems.",
                    "You have very little time left to complete this process.",
                    "You must complete this action immediately.",
                    "Do not delay your response.",
                    "This offer will expire very soon.",
                    "Complete the required action before the deadline.",
                    "You have only a limited amount of time to respond."
            ),

            "Threat language", List.of(
                    "Your account will be suspended if you do not verify it.",
                    "Your account will be permanently deleted unless you take action.",
                    "Your access will be terminated unless you confirm your identity immediately.",
                    "You will lose access to your account if you fail to verify it.",
                    "Your account has been blocked because verification was not completed.",
                    "Your account access could be restricted if you do not complete the security check.",
                    "Failure to verify your identity may result in loss of account access.",
                    "Your account may become inaccessible if you fail to confirm your identity.",
                    "Failure to complete the security procedure may lead to account suspension.",
                    "Your access may be temporarily suspended if you do not confirm your account."
            )
    );
}

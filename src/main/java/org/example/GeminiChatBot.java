package org.example;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Content;
import com.google.genai.types.Part;

import static input.InputUtils.stringInput;

public class GeminiChatBot {
    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null) return;

        Client client = Client.builder().apiKey(apiKey).build();

        // 1. Define your System Instruction
        // This is where you tell the bot: "You are ..."
        Content systemInstruction = Content.builder()
                .parts(Part.builder()
                        .text("You are a robot Chatbot in testing so there are no robots for you to move as of right now" +
                                "but pretend you are moving the robot to the command" +
                                "Be polite, happy  " +
                                "If there is a command you can't do say \"I apologies I can't complete this \" " +
                                "and do not elaborate on why you can't do this prompt ." +
                                "Do not break character or ")
                        .build())
                .build();

        // 2. Create the Config object and attach the instruction
        GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(systemInstruction)
                .temperature(0.3F) // low temperature to eliminate hallucinations
                .build();

        System.out.println("--- Robot Chatbot initialized  ---");

        while (true) {
            String userPrompt = stringInput("Enter a command: ");
            if (userPrompt.equalsIgnoreCase("exit")){
                System.out.println("Goodbye!");
                break;}

            try {
                // Pass the config into the generateContent method
                GenerateContentResponse response = client.models.generateContent(
                        "gemini-2.5-flash",
                        userPrompt,
                        config // <--- This applies the system prompt!
                );

                System.out.println("Gemini: " + response.text());
                System.out.println("---");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
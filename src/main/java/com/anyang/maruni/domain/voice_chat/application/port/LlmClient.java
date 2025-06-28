package com.anyang.maruni.domain.voice_chat.application.port;

public interface LlmClient {
    /**
 * Generates a chat response from the large language model based on the provided prompt.
 *
 * @param prompt the input text to send to the language model
 * @return the generated response from the language model
 */
String chat(String prompt);
}

package com.anyang.maruni.domain.voice_chat.infra;

import com.anyang.maruni.domain.voice_chat.application.port.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiChatClient implements LlmClient {

    private final WebClient openAiWebClient;

    /**
     * Sends a user prompt to the OpenAI chat completion API and returns the generated response.
     *
     * The prompt is sent along with a predefined system message specifying the assistant's role as a kind elderly care assistant. The response is limited to 100 tokens. If the response cannot be parsed, an empty string is returned.
     *
     * @param prompt the user's input message to send to the OpenAI model
     * @return the generated response from the OpenAI model, or an empty string if parsing fails
     */
    @Override
    public String chat(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "너는 친절한 노인 돌봄 비서야."),
                        Map.of("role", "user", "content", prompt)
                },
                "max_tokens", 100
        );

        Map<String, Object> response = openAiWebClient.post()
                .uri("/chat/completions")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        log.info("GPT response: {}", response);

        try {
            return ((Map<String, Object>) ((Map<String, Object>) ((java.util.List<?>) response.get("choices")).get(0)).get("message")).get("content").toString();
        } catch (Exception e) {
            log.error("Failed to parse GPT response", e);
            return "";
        }
    }
}

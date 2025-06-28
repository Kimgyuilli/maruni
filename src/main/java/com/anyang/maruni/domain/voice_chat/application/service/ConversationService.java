package com.anyang.maruni.domain.voice_chat.application.service;

import com.anyang.maruni.domain.voice_chat.domain.entity.Conversation;
import com.anyang.maruni.domain.voice_chat.domain.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;

    /**
     * Creates and persists a new Conversation entity with the provided speech-to-text text and GPT response.
     *
     * @param sttText   the transcribed speech-to-text input
     * @param response  the generated GPT response
     * @return the saved Conversation entity
     */
    public Conversation save(String sttText, String response) {
        Conversation conversation = Conversation.builder()
                .sttText(sttText)
                .gptResponse(response)
                .build();
        return conversationRepository.save(conversation);
    }
}


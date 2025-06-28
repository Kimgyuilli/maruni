package com.anyang.maruni.domain.voice_chat.presentation.dto.response;

import com.anyang.maruni.domain.voice_chat.domain.entity.Conversation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConversationResponse {

    private final Long id;
    private final String sttText;
    private final String gptResponse;
    private final LocalDateTime createdAt;

    /**
     * Constructs a ConversationResponse by extracting relevant data from the given Conversation entity.
     *
     * @param conversation the Conversation entity from which to initialize response fields
     */
    public ConversationResponse(Conversation conversation) {
        this.id = conversation.getId();
        this.sttText = conversation.getSttText();
        this.gptResponse = conversation.getGptResponse();
        this.createdAt = conversation.getCreatedAt();
    }
}

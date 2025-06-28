package com.anyang.maruni.domain.voice_chat.application.service;

import com.anyang.maruni.domain.voice_chat.application.port.LlmClient;
import com.anyang.maruni.domain.voice_chat.application.port.SttClient;
import com.anyang.maruni.domain.voice_chat.application.port.TtsClient;
import com.anyang.maruni.domain.voice_chat.domain.entity.Conversation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AudioProcessingService {

    private final SttClient sttClient;
    private final LlmClient llmClient;
    private final TtsClient ttsClient;
    private final ConversationService conversationService;

    /**
     * Processes an audio file by transcribing it to text, generating a response using a language model, and saving the conversation.
     *
     * @param audioFile the audio file to be processed
     * @return the saved Conversation entity containing the transcription and generated response
     */
    public Conversation processAudio(MultipartFile audioFile) {
        String sttText = sttClient.transcribe(audioFile);
        String llmResponse = llmClient.chat(sttText);
        return conversationService.save(sttText, llmResponse);
    }

    /**
     * Processes an audio file by transcribing speech to text, generating a response using a language model, saving the conversation, and synthesizing the response into speech audio.
     *
     * @param audioFile the audio file to process
     * @return a byte array containing the synthesized speech audio of the generated response
     */
    public byte[] processAudioAndSynthesize(MultipartFile audioFile) {
        String sttText = sttClient.transcribe(audioFile);
        String llmResponse = llmClient.chat(sttText);
        conversationService.save(sttText, llmResponse);
        return ttsClient.synthesizeSpeech(llmResponse);
    }
}

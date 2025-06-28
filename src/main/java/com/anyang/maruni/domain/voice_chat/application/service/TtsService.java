package com.anyang.maruni.domain.voice_chat.application.service;

import com.anyang.maruni.domain.voice_chat.application.port.TtsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TtsService {

    private final TtsClient ttsClient;

    /**
     * Converts the provided text to speech and returns the synthesized audio as a byte array.
     *
     * @param text the text to be converted to speech
     * @return a byte array containing the synthesized speech audio
     */
    public byte[] processTts(String text) {
        return ttsClient.synthesizeSpeech(text);
    }
}

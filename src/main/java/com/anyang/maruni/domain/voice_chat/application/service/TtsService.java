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

    public byte[] processTts(String text) {
        return ttsClient.synthesizeSpeech(text);
    }
}

package com.anyang.maruni.domain.voice_chat.application.port;

public interface TtsClient {
    /**
 * Synthesizes speech audio from the given text input.
 *
 * @param text the text to be converted into speech
 * @return a byte array containing the synthesized speech audio data
 */
byte[] synthesizeSpeech(String text);
}

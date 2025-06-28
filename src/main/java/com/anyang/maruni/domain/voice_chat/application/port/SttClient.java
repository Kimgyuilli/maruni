package com.anyang.maruni.domain.voice_chat.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface SttClient {
    /**
 * Converts the spoken content of the provided audio file to text using speech-to-text transcription.
 *
 * @param audioFile the audio file to be transcribed
 * @return the transcribed text from the audio file
 */
String transcribe(MultipartFile audioFile);
}

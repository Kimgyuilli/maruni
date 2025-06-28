package com.anyang.maruni.domain.voice_chat.infra;

import com.anyang.maruni.domain.voice_chat.application.port.SttClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WhisperApiClient implements SttClient {

    private final WebClient openAiWebClient;

    /**
     * Sends an audio file to the Whisper API for speech-to-text transcription and returns the transcription result.
     *
     * The audio file is converted to a temporary file and sent as multipart form data along with model and language parameters.
     * The API response is returned as a string. The temporary file is deleted after the operation completes.
     *
     * @param audioFile the audio file to be transcribed
     * @return the transcription result from the Whisper API as a string
     */
    @Override
    public String transcribe(MultipartFile audioFile) {
        File tempFile = convertMultipartToFile(audioFile);

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(tempFile));
            body.add("model", "whisper-1");
            body.add("language", "ko");

            String response = openAiWebClient.post()
                    .uri("/audio/transcriptions")
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Whisper API response: {}", response);

            return response;

        } finally {
            tempFile.delete();
        }
    }

    /**
     * Converts a {@link MultipartFile} to a temporary WAV file on disk.
     *
     * @param file the multipart audio file to convert
     * @return a temporary file containing the audio data
     * @throws RuntimeException if an I/O error occurs during file creation or writing
     */
    private File convertMultipartToFile(MultipartFile file) {
        try {
            File convFile = File.createTempFile("audio", ".wav");
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }
            return convFile;
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 실패", e);
        }
    }
}

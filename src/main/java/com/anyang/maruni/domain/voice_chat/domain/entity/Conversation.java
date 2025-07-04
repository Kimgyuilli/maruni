package com.anyang.maruni.domain.voice_chat.domain.entity;

import com.anyang.maruni.domain.User.domain.entity.User;
import com.anyang.maruni.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Conversation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 정보 (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String sttText;            // Whisper 변환 텍스트
    @Column(columnDefinition = "TEXT")
    private String gptResponse;
}

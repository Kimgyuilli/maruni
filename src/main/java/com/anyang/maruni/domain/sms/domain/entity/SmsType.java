package com.anyang.maruni.domain.sms.domain.entity;

/**
 * SMS 메시지 유형
 */
public enum SmsType {
    GREETING,        // 안부 메시지
    AI_RESPONSE,     // AI 생성 응답
    GUARDIAN_ALERT   // 보호자 알림
}
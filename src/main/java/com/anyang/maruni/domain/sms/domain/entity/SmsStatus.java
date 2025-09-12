package com.anyang.maruni.domain.sms.domain.entity;

/**
 * SMS 발송 상태
 */
public enum SmsStatus {
    PENDING,     // 발송 대기
    SUCCESS,     // 발송 성공
    FAILURE,     // 발송 실패
    RECEIVED     // 수신 완료 (incoming 메시지용)
}
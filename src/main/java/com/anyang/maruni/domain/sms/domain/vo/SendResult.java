package com.anyang.maruni.domain.sms.domain.vo;

import lombok.Getter;

/**
 * SMS 발송 결과 Value Object
 */
@Getter
public class SendResult {
	
	private final boolean success;
	private final String messageId;
	private final String errorMessage;
	
	private SendResult(boolean success, String messageId, String errorMessage) {
		this.success = success;
		this.messageId = messageId;
		this.errorMessage = errorMessage;
	}
	
	/**
	 * 발송 성공 결과 생성
	 */
	public static SendResult success(String messageId) {
		return new SendResult(true, messageId, null);
	}
	
	/**
	 * 발송 실패 결과 생성
	 */
	public static SendResult failure(String errorMessage) {
		return new SendResult(false, null, errorMessage);
	}
}
package com.anyang.maruni.domain.sms.application.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * SMS 발송 응답 DTO
 */
@Getter
@Builder
public class SendSmsResponse {

	private boolean success;
	private String messageId;
	private String errorMessage;

	public static SendSmsResponse success(String messageId) {
		return SendSmsResponse.builder()
			.success(true)
			.messageId(messageId)
			.build();
	}

	public static SendSmsResponse failure(String errorMessage) {
		return SendSmsResponse.builder()
			.success(false)
			.errorMessage(errorMessage)
			.build();
	}
}
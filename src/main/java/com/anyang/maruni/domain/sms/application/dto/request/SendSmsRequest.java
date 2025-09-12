package com.anyang.maruni.domain.sms.application.dto.request;

import com.anyang.maruni.domain.sms.domain.entity.SmsType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * SMS 발송 요청 DTO
 */
@Getter
@Builder
public class SendSmsRequest {

	@NotNull(message = "회원 ID는 필수입니다")
	private Long memberId;

	@NotBlank(message = "전화번호는 필수입니다")
	private String phoneNumber;

	@NotBlank(message = "메시지 내용은 필수입니다")
	private String content;

	@NotNull(message = "SMS 타입은 필수입니다")
	private SmsType smsType;
}
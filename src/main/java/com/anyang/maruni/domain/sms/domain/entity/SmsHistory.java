package com.anyang.maruni.domain.sms.domain.entity;

import java.time.LocalDateTime;

import com.anyang.maruni.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SMS 발송/수신 이력 엔티티
 */
@Entity
@Table(name = "sms_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false, length = 20)
	private String phoneNumber;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SmsDirection direction;

	@Enumerated(EnumType.STRING)
	private SmsType smsType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SmsStatus status;

	@Column(length = 100)
	private String messageId;

	private LocalDateTime sentAt;

	@Column(columnDefinition = "TEXT")
	private String errorMessage;

	/**
	 * 발송용 SMS 이력 생성
	 */
	public static SmsHistory createOutgoing(Long memberId, String phoneNumber, String content, SmsType smsType) {
		validateRequiredFields(memberId, phoneNumber, content);

		return SmsHistory.builder()
			.memberId(memberId)
			.phoneNumber(phoneNumber)
			.content(content)
			.direction(SmsDirection.OUTGOING)
			.smsType(smsType)
			.status(SmsStatus.PENDING)
			.build();
	}

	/**
	 * 수신용 SMS 이력 생성
	 */
	public static SmsHistory createIncoming(Long memberId, String phoneNumber, String content) {
		validateRequiredFields(memberId, phoneNumber, content);

		return SmsHistory.builder()
			.memberId(memberId)
			.phoneNumber(phoneNumber)
			.content(content)
			.direction(SmsDirection.INCOMING)
			.status(SmsStatus.RECEIVED)
			.build();
	}

	/**
	 * SMS 발송 성공 처리
	 */
	public void markAsSuccess(String messageId) {
		this.status = SmsStatus.SUCCESS;
		this.messageId = messageId;
		this.sentAt = LocalDateTime.now();
		this.errorMessage = null;
	}

	/**
	 * SMS 발송 실패 처리
	 */
	public void markAsFailure(String errorMessage) {
		this.status = SmsStatus.FAILURE;
		this.errorMessage = errorMessage;
		this.sentAt = LocalDateTime.now();
		this.messageId = null;
	}

	private static void validateRequiredFields(Long memberId, String phoneNumber, String content) {
		if (memberId == null) {
			throw new IllegalArgumentException("회원 ID는 필수입니다");
		}
		if (phoneNumber == null) {
			throw new IllegalArgumentException("전화번호는 필수입니다");
		}
		if (content == null) {
			throw new IllegalArgumentException("메시지 내용은 필수입니다");
		}
	}
}
package com.anyang.maruni.domain.sms.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anyang.maruni.domain.sms.application.dto.request.SendSmsRequest;
import com.anyang.maruni.domain.sms.application.dto.response.SendSmsResponse;
import com.anyang.maruni.domain.sms.domain.entity.SmsHistory;
import com.anyang.maruni.domain.sms.domain.entity.SmsType;
import com.anyang.maruni.domain.sms.domain.repository.SmsHistoryRepository;
import com.anyang.maruni.domain.sms.domain.service.SmsSender;
import com.anyang.maruni.domain.sms.domain.vo.SendResult;
import com.anyang.maruni.global.exception.BaseException;
import com.anyang.maruni.global.response.error.ErrorCode;

import lombok.RequiredArgsConstructor;

/**
 * SMS 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmsService {

	private final SmsSender smsSender;
	private final SmsHistoryRepository smsHistoryRepository;

	/**
	 * SMS 발송
	 */
	@Transactional
	public SendSmsResponse sendSms(SendSmsRequest request) {
		// 요청 검증
		validateSendSmsRequest(request);

		// SMS 이력 생성 및 저장
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			request.getMemberId(),
			request.getPhoneNumber(),
			request.getContent(),
			request.getSmsType()
		);
		smsHistoryRepository.save(smsHistory);

		// SMS 발송
		SendResult sendResult = smsSender.send(smsHistory);

		// 발송 결과에 따른 이력 업데이트
		if (sendResult.isSuccess()) {
			smsHistory.markAsSuccess(sendResult.getMessageId());
		} else {
			smsHistory.markAsFailure(sendResult.getErrorMessage());
		}
		smsHistoryRepository.save(smsHistory);

		// 응답 생성
		return sendResult.isSuccess() 
			? SendSmsResponse.success(sendResult.getMessageId())
			: SendSmsResponse.failure(sendResult.getErrorMessage());
	}

	/**
	 * 안부 메시지 발송
	 */
	@Transactional
	public SendSmsResponse sendGreetingMessage(Long memberId, String phoneNumber) {
		SendSmsRequest request = SendSmsRequest.builder()
			.memberId(memberId)
			.phoneNumber(phoneNumber)
			.content("안녕하세요! 오늘 하루도 화이팅!")
			.smsType(SmsType.GREETING)
			.build();

		return sendSms(request);
	}

	/**
	 * 수신 메시지 저장
	 */
	@Transactional
	public void saveIncomingMessage(Long memberId, String phoneNumber, String content) {
		SmsHistory incomingHistory = SmsHistory.createIncoming(memberId, phoneNumber, content);
		smsHistoryRepository.save(incomingHistory);
	}

	private void validateSendSmsRequest(SendSmsRequest request) {
		if (request.getMemberId() == null) {
			throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
		}
		if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
			throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
		}
		if (request.getContent() == null || request.getContent().trim().isEmpty()) {
			throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
		}
		if (request.getSmsType() == null) {
			throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
		}
	}
}
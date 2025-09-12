package com.anyang.maruni.domain.sms.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.anyang.maruni.domain.sms.application.dto.request.SendSmsRequest;
import com.anyang.maruni.domain.sms.application.dto.response.SendSmsResponse;
import com.anyang.maruni.domain.sms.domain.entity.SmsHistory;
import com.anyang.maruni.domain.sms.domain.entity.SmsType;
import com.anyang.maruni.domain.sms.domain.repository.SmsHistoryRepository;
import com.anyang.maruni.domain.sms.domain.service.SmsSender;
import com.anyang.maruni.domain.sms.domain.vo.SendResult;
import com.anyang.maruni.global.exception.BaseException;
import com.anyang.maruni.global.response.error.ErrorCode;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("SmsService 단위 테스트")
class SmsServiceTest {

	@Mock
	private SmsSender smsSender;

	@Mock
	private SmsHistoryRepository smsHistoryRepository;

	@InjectMocks
	private SmsService smsService;

	@Test
	@DisplayName("SMS 발송 성공 - 안부 메시지를 성공적으로 발송하고 이력을 저장한다")
	void sendSms_Success_GreetingMessage() {
		// given
		SendSmsRequest request = SendSmsRequest.builder()
			.memberId(1L)
			.phoneNumber("01012345678")
			.content("안녕하세요! 오늘 하루도 화이팅!")
			.smsType(SmsType.GREETING)
			.build();

		SmsHistory savedHistory = SmsHistory.createOutgoing(
			request.getMemberId(), 
			request.getPhoneNumber(), 
			request.getContent(), 
			request.getSmsType()
		);

		SendResult sendResult = SendResult.success("MSG123456789");

		given(smsHistoryRepository.save(any(SmsHistory.class))).willReturn(savedHistory);
		given(smsSender.send(any(SmsHistory.class))).willReturn(sendResult);

		// when
		SendSmsResponse response = smsService.sendSms(request);

		// then
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.getMessageId()).isEqualTo("MSG123456789");
		assertThat(response.getErrorMessage()).isNull();

		then(smsHistoryRepository).should(times(2)).save(any(SmsHistory.class));
		then(smsSender).should().send(any(SmsHistory.class));
	}

	@Test
	@DisplayName("SMS 발송 성공 - AI 응답 메시지를 성공적으로 발송한다")
	void sendSms_Success_AiResponse() {
		// given
		SendSmsRequest request = SendSmsRequest.builder()
			.memberId(1L)
			.phoneNumber("01012345678")
			.content("그렇군요! 오늘도 건강하게 보내세요.")
			.smsType(SmsType.AI_RESPONSE)
			.build();

		SmsHistory savedHistory = SmsHistory.createOutgoing(
			request.getMemberId(), 
			request.getPhoneNumber(), 
			request.getContent(), 
			request.getSmsType()
		);

		SendResult sendResult = SendResult.success("MSG987654321");

		given(smsHistoryRepository.save(any(SmsHistory.class))).willReturn(savedHistory);
		given(smsSender.send(any(SmsHistory.class))).willReturn(sendResult);

		// when
		SendSmsResponse response = smsService.sendSms(request);

		// then
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.getMessageId()).isEqualTo("MSG987654321");
		assertThat(response.getErrorMessage()).isNull();
	}

	@Test
	@DisplayName("SMS 발송 실패 - 발송 실패 시 에러 상태로 이력을 저장한다")
	void sendSms_Failure_ApiError() {
		// given
		SendSmsRequest request = SendSmsRequest.builder()
			.memberId(1L)
			.phoneNumber("01012345678")
			.content("테스트 메시지")
			.smsType(SmsType.GREETING)
			.build();

		SmsHistory savedHistory = SmsHistory.createOutgoing(
			request.getMemberId(), 
			request.getPhoneNumber(), 
			request.getContent(), 
			request.getSmsType()
		);

		SendResult sendResult = SendResult.failure("SMS API 서버 오류");

		given(smsHistoryRepository.save(any(SmsHistory.class))).willReturn(savedHistory);
		given(smsSender.send(any(SmsHistory.class))).willReturn(sendResult);

		// when
		SendSmsResponse response = smsService.sendSms(request);

		// then
		assertThat(response.isSuccess()).isFalse();
		assertThat(response.getMessageId()).isNull();
		assertThat(response.getErrorMessage()).isEqualTo("SMS API 서버 오류");

		then(smsHistoryRepository).should(times(2)).save(any(SmsHistory.class)); // 초기 저장 + 실패 상태 저장
		then(smsSender).should().send(any(SmsHistory.class));
	}

	@Test
	@DisplayName("안부 메시지 발송 - 특정 회원에게 안부 메시지를 발송한다")
	void sendGreetingMessage_Success() {
		// given
		Long memberId = 1L;
		String phoneNumber = "01012345678";
		String greetingContent = "안녕하세요! 오늘 하루도 화이팅!";

		SmsHistory savedHistory = SmsHistory.createOutgoing(memberId, phoneNumber, greetingContent, SmsType.GREETING);
		SendResult sendResult = SendResult.success("MSG111222333");

		given(smsHistoryRepository.save(any(SmsHistory.class))).willReturn(savedHistory);
		given(smsSender.send(any(SmsHistory.class))).willReturn(sendResult);

		// when
		SendSmsResponse response = smsService.sendGreetingMessage(memberId, phoneNumber);

		// then
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.getMessageId()).isEqualTo("MSG111222333");

		then(smsHistoryRepository).should(times(2)).save(any(SmsHistory.class)); // 초기 저장 + 성공 상태 저장
	}

	@Test
	@DisplayName("수신 메시지 저장 - 사용자로부터 받은 메시지를 이력에 저장한다")
	void saveIncomingMessage_Success() {
		// given
		Long memberId = 1L;
		String phoneNumber = "01012345678";
		String content = "네, 좋은 하루 보내고 있어요!";

		SmsHistory incomingHistory = SmsHistory.createIncoming(memberId, phoneNumber, content);
		given(smsHistoryRepository.save(any(SmsHistory.class))).willReturn(incomingHistory);

		// when
		assertThatCode(() -> smsService.saveIncomingMessage(memberId, phoneNumber, content))
			.doesNotThrowAnyException();

		// then
		then(smsHistoryRepository).should().save(argThat(history -> 
			history.getMemberId().equals(memberId) &&
			history.getPhoneNumber().equals(phoneNumber) &&
			history.getContent().equals(content) &&
			history.getDirection().name().equals("INCOMING") &&
			history.getStatus().name().equals("RECEIVED")
		));
	}

	@Test
	@DisplayName("SMS 발송 실패 - 잘못된 요청으로 예외를 발생시킨다")
	void sendSms_Fail_InvalidRequest() {
		// given
		SendSmsRequest invalidRequest = SendSmsRequest.builder()
			.memberId(null) // 필수값 누락
			.phoneNumber("01012345678")
			.content("테스트")
			.smsType(SmsType.GREETING)
			.build();

		// when & then
		assertThatThrownBy(() -> smsService.sendSms(invalidRequest))
			.isInstanceOf(BaseException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT_VALUE);

		then(smsHistoryRepository).should(never()).save(any());
		then(smsSender).should(never()).send(any());
	}
}
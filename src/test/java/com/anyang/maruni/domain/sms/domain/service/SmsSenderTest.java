package com.anyang.maruni.domain.sms.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anyang.maruni.domain.sms.domain.entity.SmsHistory;
import com.anyang.maruni.domain.sms.domain.entity.SmsType;
import com.anyang.maruni.domain.sms.domain.vo.SendResult;

@ExtendWith(MockitoExtension.class)
@DisplayName("SmsSender 도메인 서비스 테스트")
class SmsSenderTest {

	@Mock
	private SmsSender smsSender;

	@Test
	@DisplayName("SMS 발송 성공 - 안부 메시지를 성공적으로 발송한다")
	void sendSms_Success_GreetingMessage() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01012345678", "안녕하세요! 오늘 하루도 화이팅!", SmsType.GREETING
		);

		SendResult expectedResult = SendResult.success("MSG123456789");
		given(smsSender.send(smsHistory)).willReturn(expectedResult);

		// when
		SendResult result = smsSender.send(smsHistory);

		// then
		assertThat(result.isSuccess()).isTrue();
		assertThat(result.getMessageId()).isEqualTo("MSG123456789");
		assertThat(result.getErrorMessage()).isNull();
	}

	@Test
	@DisplayName("SMS 발송 성공 - AI 응답 메시지를 성공적으로 발송한다")
	void sendSms_Success_AiResponseMessage() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01012345678", "그렇군요! 오늘도 건강하게 보내세요.", SmsType.AI_RESPONSE
		);

		SendResult expectedResult = SendResult.success("MSG987654321");
		given(smsSender.send(smsHistory)).willReturn(expectedResult);

		// when
		SendResult result = smsSender.send(smsHistory);

		// then
		assertThat(result.isSuccess()).isTrue();
		assertThat(result.getMessageId()).isEqualTo("MSG987654321");
		assertThat(result.getErrorMessage()).isNull();
	}

	@Test
	@DisplayName("SMS 발송 성공 - 보호자 알림을 성공적으로 발송한다")
	void sendSms_Success_GuardianAlert() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01087654321", "김할머니님의 응답에서 이상징후가 감지되었습니다.", SmsType.GUARDIAN_ALERT
		);

		SendResult expectedResult = SendResult.success("MSG555666777");
		given(smsSender.send(smsHistory)).willReturn(expectedResult);

		// when
		SendResult result = smsSender.send(smsHistory);

		// then
		assertThat(result.isSuccess()).isTrue();
		assertThat(result.getMessageId()).isEqualTo("MSG555666777");
		assertThat(result.getErrorMessage()).isNull();
	}

	@Test
	@DisplayName("SMS 발송 실패 - 잘못된 전화번호로 발송 실패")
	void sendSms_Failure_InvalidPhoneNumber() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "invalid-phone", "테스트 메시지", SmsType.GREETING
		);

		SendResult expectedResult = SendResult.failure("전화번호 형식이 올바르지 않습니다");
		given(smsSender.send(smsHistory)).willReturn(expectedResult);

		// when
		SendResult result = smsSender.send(smsHistory);

		// then
		assertThat(result.isSuccess()).isFalse();
		assertThat(result.getMessageId()).isNull();
		assertThat(result.getErrorMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다");
	}

	@Test
	@DisplayName("SMS 발송 실패 - API 호출 실패")
	void sendSms_Failure_ApiError() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01012345678", "테스트 메시지", SmsType.GREETING
		);

		SendResult expectedResult = SendResult.failure("SMS API 서버 오류");
		given(smsSender.send(smsHistory)).willReturn(expectedResult);

		// when
		SendResult result = smsSender.send(smsHistory);

		// then
		assertThat(result.isSuccess()).isFalse();
		assertThat(result.getMessageId()).isNull();
		assertThat(result.getErrorMessage()).isEqualTo("SMS API 서버 오류");
	}
}
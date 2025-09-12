package com.anyang.maruni.domain.sms.domain.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SmsHistory 엔티티 단위 테스트")
class SmsHistoryTest {

	@Test
	@DisplayName("SMS 이력 생성 - 발송용 SMS 이력을 생성한다")
	void createOutgoingSms_Success() {
		// given
		Long memberId = 1L;
		String phoneNumber = "01012345678";
		String content = "안녕하세요! 오늘 하루도 화이팅!";
		SmsType smsType = SmsType.GREETING;

		// when
		SmsHistory smsHistory = SmsHistory.createOutgoing(memberId, phoneNumber, content, smsType);

		// then
		assertThat(smsHistory.getMemberId()).isEqualTo(memberId);
		assertThat(smsHistory.getPhoneNumber()).isEqualTo(phoneNumber);
		assertThat(smsHistory.getContent()).isEqualTo(content);
		assertThat(smsHistory.getSmsType()).isEqualTo(smsType);
		assertThat(smsHistory.getDirection()).isEqualTo(SmsDirection.OUTGOING);
		assertThat(smsHistory.getStatus()).isEqualTo(SmsStatus.PENDING);
	}

	@Test
	@DisplayName("SMS 이력 생성 - 수신용 SMS 이력을 생성한다")
	void createIncomingSms_Success() {
		// given
		Long memberId = 1L;
		String phoneNumber = "01012345678";
		String content = "네, 좋은 하루 보내고 있어요!";

		// when
		SmsHistory smsHistory = SmsHistory.createIncoming(memberId, phoneNumber, content);

		// then
		assertThat(smsHistory.getMemberId()).isEqualTo(memberId);
		assertThat(smsHistory.getPhoneNumber()).isEqualTo(phoneNumber);
		assertThat(smsHistory.getContent()).isEqualTo(content);
		assertThat(smsHistory.getDirection()).isEqualTo(SmsDirection.INCOMING);
		assertThat(smsHistory.getStatus()).isEqualTo(SmsStatus.RECEIVED);
		assertThat(smsHistory.getSmsType()).isNull(); // 수신 메시지는 타입 없음
	}

	@Test
	@DisplayName("SMS 발송 성공 처리 - 상태를 성공으로 변경하고 결과를 저장한다")
	void markAsSuccess_Success() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01012345678", "테스트 메시지", SmsType.GREETING
		);
		String messageId = "MSG123456789";

		// when
		smsHistory.markAsSuccess(messageId);

		// then
		assertThat(smsHistory.getStatus()).isEqualTo(SmsStatus.SUCCESS);
		assertThat(smsHistory.getMessageId()).isEqualTo(messageId);
		assertThat(smsHistory.getSentAt()).isNotNull();
		assertThat(smsHistory.getErrorMessage()).isNull();
	}

	@Test
	@DisplayName("SMS 발송 실패 처리 - 상태를 실패로 변경하고 에러 메시지를 저장한다")
	void markAsFailure_Success() {
		// given
		SmsHistory smsHistory = SmsHistory.createOutgoing(
			1L, "01012345678", "테스트 메시지", SmsType.GREETING
		);
		String errorMessage = "전화번호 형식이 올바르지 않습니다";

		// when
		smsHistory.markAsFailure(errorMessage);

		// then
		assertThat(smsHistory.getStatus()).isEqualTo(SmsStatus.FAILURE);
		assertThat(smsHistory.getErrorMessage()).isEqualTo(errorMessage);
		assertThat(smsHistory.getSentAt()).isNotNull();
		assertThat(smsHistory.getMessageId()).isNull();
	}

	@Test
	@DisplayName("SMS 이력 생성 실패 - 필수 정보가 누락되면 예외를 발생시킨다")
	void createOutgoingSms_Fail_RequiredFieldMissing() {
		// given & when & then
		assertThatThrownBy(() -> 
			SmsHistory.createOutgoing(null, "01012345678", "내용", SmsType.GREETING))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("회원 ID는 필수입니다");

		assertThatThrownBy(() -> 
			SmsHistory.createOutgoing(1L, null, "내용", SmsType.GREETING))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("전화번호는 필수입니다");

		assertThatThrownBy(() -> 
			SmsHistory.createOutgoing(1L, "01012345678", null, SmsType.GREETING))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메시지 내용은 필수입니다");
	}
}
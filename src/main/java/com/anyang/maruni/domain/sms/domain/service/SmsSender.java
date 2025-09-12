package com.anyang.maruni.domain.sms.domain.service;

import com.anyang.maruni.domain.sms.domain.entity.SmsHistory;
import com.anyang.maruni.domain.sms.domain.vo.SendResult;

/**
 * SMS 발송 도메인 서비스 인터페이스
 * 
 * 도메인 계층에서 정의하고, Infrastructure 계층에서 구현
 */
public interface SmsSender {
	
	/**
	 * SMS 발송
	 * 
	 * @param smsHistory 발송할 SMS 정보
	 * @return 발송 결과
	 */
	SendResult send(SmsHistory smsHistory);
}
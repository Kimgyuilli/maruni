package com.anyang.maruni.domain.sms.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anyang.maruni.domain.sms.domain.entity.SmsHistory;

/**
 * SMS 이력 Repository 인터페이스
 */
public interface SmsHistoryRepository extends JpaRepository<SmsHistory, Long> {

}
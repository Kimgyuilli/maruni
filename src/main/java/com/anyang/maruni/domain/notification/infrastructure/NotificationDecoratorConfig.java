package com.anyang.maruni.domain.notification.infrastructure;

import com.anyang.maruni.domain.notification.domain.service.NotificationHistoryService;
import com.anyang.maruni.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 알림 서비스 데코레이터 설정
 *
 * 기존 NotificationService 구현체를 NotificationHistoryDecorator로
 * 자동으로 감싸서 이력 저장 기능을 투명하게 추가합니다.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class NotificationDecoratorConfig {

    private final NotificationHistoryService historyService;

    /**
     * 이력 저장이 활성화된 경우 기본 NotificationService를 데코레이터로 감싸기
     *
     * application.yml 설정을 통해 이력 저장 기능을 활성화/비활성화할 수 있습니다:
     * notification:
     *   history:
     *     enabled: true
     */
    @Bean
    @Primary
    @ConditionalOnProperty(
            value = "notification.history.enabled",
            havingValue = "true",
            matchIfMissing = true  // 기본적으로 활성화
    )
    public NotificationService notificationServiceWithHistory(@Autowired List<NotificationService> services) {
        // MockPushNotificationService 찾기 (originalNotificationService 어노테이션이 있는)
        NotificationService originalService = services.stream()
                .filter(service -> !(service instanceof NotificationHistoryDecorator))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No original NotificationService found"));

        log.info("🔧 Wrapping NotificationService with history decorator: {}",
                originalService.getClass().getSimpleName());

        return new NotificationHistoryDecorator(originalService, historyService);
    }
}
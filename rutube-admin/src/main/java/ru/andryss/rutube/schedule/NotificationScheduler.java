package ru.andryss.rutube.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.service.MailService;
import ru.andryss.rutube.service.ModerationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Profile("master")
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final ModerationService moderationService;
    private final MailService mailService;

    @Scheduled(cron = "0 10/30 * * * *", scheduler = "schedulerExecutor")
    public void sendModerationPendingActionNotifications() {
        log.info("Start sending moderation pending action notifications");

        Instant timestamp = Instant.now().minus(1L, ChronoUnit.HOURS);

        moderationService.findRequestsAssignedBefore(timestamp).forEach(info ->
                mailService.sendModerationPendingNotification(info.getAssignee(), info.getEmail(), info.getSourceId()));

        log.info("Sending moderation pending action notifications finished");
    }

}

package ru.andryss.rutube.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.model.Video;
import ru.andryss.rutube.model.VideoStatus;
import ru.andryss.rutube.service.MailService;
import ru.andryss.rutube.service.VideoService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final VideoService videoService;
    private final MailService mailService;

    @Scheduled(cron = "0 0 9 * * *", scheduler = "schedulerExecutor")
    public void sendVideosPendingActionsNotifications() {
        log.info("Start sending videos pending actions notifications");

        Instant timestamp = Instant.now().minus(1L, ChronoUnit.DAYS);

        videoService.findUsersWithPendingActions(timestamp).forEach(user -> {
            Map<VideoStatus, Long> counts = videoService.findVideosPendingActions(user.getUsername(), timestamp).stream()
                    .collect(groupingBy(Video::getStatus, counting()));
            mailService.sendVideosPendingActionsNotification(user.getUsername(), user.getEmail(), counts);
        });

        log.info("Sending videos pending actions notifications finished");
    }

}

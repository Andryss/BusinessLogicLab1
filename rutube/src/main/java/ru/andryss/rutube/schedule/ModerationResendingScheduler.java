package ru.andryss.rutube.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.andryss.rutube.service.ModerationRequestSenderService;
import ru.andryss.rutube.service.SourceService;
import ru.andryss.rutube.service.VideoService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModerationResendingScheduler {

    private final VideoService videoService;
    private final ModerationRequestSenderService requestSenderService;
    private final SourceService sourceService;

    @Scheduled(cron = "0 2/3 * * * *", scheduler = "schedulerExecutor")
    public void resendModerationRequests() {
        log.info("Start resending moderation requests");

        Instant timestamp = Instant.now().minus(5L, ChronoUnit.MINUTES);

        List<String> videosPendingModeration = videoService.findVideosPendingModeration(timestamp);

        log.info("Found {} videos pending moderation, resending", videosPendingModeration.size());

        videosPendingModeration.forEach(sourceId ->
                requestSenderService.sendFirstTime(sourceId, sourceService.generateDownloadLink(sourceId)));

        log.info("Resending moderation requests finished");
    }
}

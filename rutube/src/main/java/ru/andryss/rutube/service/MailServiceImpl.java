package ru.andryss.rutube.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.model.VideoStatus;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${mail.sender}")
    private String sender;

    @Override
    @Async("mailExecutor")
    public void sendVideosPendingActionsNotification(String author, String email, Map<VideoStatus, Long> counts) {
        log.info("send mail to {} with email {} about his pending videos: {}", author, email, counts);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");

            messageHelper.setTo(email);
            messageHelper.setFrom(sender);
            messageHelper.setSubject("Videos pending actions");
            messageHelper.setText(formatMessage(author, counts));

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("error occurred while sending videos pending actions notification", e);
        }
    }

    private String formatMessage(String author, Map<VideoStatus, Long> counts) {
        StringBuilder builder = new StringBuilder("Dear ").append(author).append(". You have");
        Long notUploaded = counts.get(VideoStatus.UPLOAD_PENDING);
        Long notFilled = counts.get(VideoStatus.FILL_PENDING);
        if (notUploaded != null && notUploaded > 0) {
            builder.append(' ').append(notUploaded.longValue()).append(" videos pending uploading");
            if (notFilled != null && notFilled > 0) {
                builder.append(" and ").append(notFilled.longValue()).append(" videos pending filling");
            }
            builder.append(". Please take actions!");
        } else if (notFilled != null && notFilled > 0) {
            builder.append(' ').append(notFilled.longValue()).append(" videos pending filling").append(". Please take actions!");
        } else {
            log.warn("Sending notification with empty counts to {}", author);
            builder.append(" no unpublished videos to take care. Relax :)");
        }
        return builder.toString();
    }
}

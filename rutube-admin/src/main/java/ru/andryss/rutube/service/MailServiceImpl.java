package ru.andryss.rutube.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    @Async("mailExecutor")
    public void sendModerationPendingNotification(String moderator, String email, String sourceId) {
        log.info("send mail to {} with email {} about his moderation: {}", moderator, email, sourceId);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");

            messageHelper.setTo(email);
            messageHelper.setFrom("rutube-parody-admin@yandex.ru");
            messageHelper.setSubject("Moderation pending action");
            messageHelper.setText(sourceId);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("error occurred while sending moderation pending notification", e);
        }
    }
}

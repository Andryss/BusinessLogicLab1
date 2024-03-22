package ru.andryss.rutube.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Async("mailExecutor")
    public void sendVideosPendingActionsNotification(String author, String email, Map<VideoStatus, Long> counts) {
        log.info("send mail to {} with email {} about his pending videos: {}", author, email, counts);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");

            messageHelper.setTo(email);
            messageHelper.setFrom("rutube-parody@yandex.ru");
            messageHelper.setSubject("Videos pending actions");
            messageHelper.setText(counts.toString());

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("error occurred while sending videos pending actions notification", e);
        }
    }
}

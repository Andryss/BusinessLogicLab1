package ru.andryss.rutube.configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.model.Moderator;
import ru.andryss.rutube.model.Role;
import ru.andryss.rutube.repository.ModeratorRepository;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ServletContextListener {

    private final ModeratorRepository moderatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void contextInitialized(ServletContextEvent sce) {
        transactionTemplate.executeWithoutResult(status -> {
            if (moderatorRepository.count() > 0) {
                return;
            }

            Moderator moderator = new Moderator();
            moderator.setUsername("moderator");
            moderator.setPassword(passwordEncoder.encode("moderator"));
            moderator.setEmail("moderator@example.mail");
            moderator.setRole(Role.MODERATOR);

            moderatorRepository.save(moderator);
        });
    }
}

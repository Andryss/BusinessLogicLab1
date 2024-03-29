package ru.andryss.rutube.configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.model.Role;
import ru.andryss.rutube.model.User;
import ru.andryss.rutube.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ServletContextListener {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Retryable(retryFor = SQLException.class)
    public void contextInitialized(ServletContextEvent sce) {
        transactionTemplate.executeWithoutResult(status -> {
            if (userRepository.count() > 0) {
                return;
            }

            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user1"));
            user1.setEmail("user1@example.mail");
            user1.setRole(Role.USER);

            User user2 = new User();
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user2"));
            user2.setEmail("user2@example.mail");
            user2.setRole(Role.USER);

            userRepository.saveAll(List.of(user1, user2));
        });
    }
}

package ru.andryss.rutube.configuration;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.model.Role;
import ru.andryss.rutube.model.User;
import ru.andryss.rutube.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ServletContextListener {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (userRepository.count() > 0) {
            return;
        }

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(passwordEncoder.encode("user1"));
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(passwordEncoder.encode("user2"));
        user2.setRole(Role.USER);

        User moderator = new User();
        moderator.setUsername("moderator");
        moderator.setPassword(passwordEncoder.encode("moderator"));
        moderator.setRole(Role.MODERATOR);

        transactionTemplate.executeWithoutResult(status -> userRepository.saveAll(List.of(user1, user2, moderator)));
    }
}

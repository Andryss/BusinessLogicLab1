package ru.andryss.rutube.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.model.User;
import ru.andryss.rutube.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = readOnlyTransactionTemplate.execute(status ->
                userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException(username)));
        //noinspection ConstantConditions
        return CustomUserDetails.build(user);
    }
}

package ru.andryss.rutube.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.andryss.rutube.model.Moderator;
import ru.andryss.rutube.repository.ModeratorRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ModeratorRepository moderatorRepository;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Moderator moderator = readOnlyTransactionTemplate.execute(status ->
                moderatorRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException(username)));
        //noinspection ConstantConditions
        return CustomUserDetails.build(moderator);
    }
}

package ru.andryss.rutube.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.andryss.rutube.message.ErrorMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> token = getTokenFromRequest(request);
        if (token.isPresent() && jwtUtil.checkToken(token.get())) {
            String username;
            UserDetails userDetails;
            try {
                username = jwtUtil.usernameFromToken(token.get());
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                response.setStatus(UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(errorMessage));
                writer.close();
                return;
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts bearer token from HTTP request
     *
     * @param request HTTP user request
     * @return token or empty Optional
     */
    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer == null) return Optional.empty();
        return bearer.startsWith("Bearer ") ? Optional.of(bearer.substring(7)) : Optional.empty();
    }
}

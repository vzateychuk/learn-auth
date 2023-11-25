package vez.learnauth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtTokenService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // first check if we have a JWT token. if not, early return
        final String authHeater = req.getHeader("Authorization");
        if (Strings.isBlank(authHeater) || !authHeater.startsWith("Bearer ")) {
            filterChain.doFilter(req, resp);
            return;
        }

        // extract username from jwtToken
        final String jwtToken = authHeater.substring(7);
        final String userEmail = jwtService.extractUsername(jwtToken);

    }
}

package vez.learnauth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest req,
            @NonNull HttpServletResponse resp,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // first check if we have a JWT token. if not, early return
        final String authHeater = req.getHeader("Authorization");
        if (Strings.isBlank(authHeater) || !authHeater.startsWith("Bearer ")) {
            filterChain.doFilter(req, resp);
            return;
        }

        // remove prefix 'Bearer ' from header
        final String jwtToken = authHeater.substring(7);
        // extract username from jwtToken
        final String userEmail = jwtService.extractUsername(jwtToken);
        // check if user already authenticated
        if (!Strings.isBlank(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // need to check userDetails in UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // if the jwtToken still valid, we update security context
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(req) );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(req, resp);
    }
}

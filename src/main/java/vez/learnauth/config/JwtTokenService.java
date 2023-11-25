package vez.learnauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtTokenService {

    @Value("${auth.secret.key}")
    private String SECRET_KEY;

    public String extractUsername(String jwtToken) {
        return null;
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey( getSignIngKey() )
                .build()
                .parseClaimsJwt(jwtToken)
                .getBody();
    }

    private Key getSignIngKey() {
        return null;
    }
}

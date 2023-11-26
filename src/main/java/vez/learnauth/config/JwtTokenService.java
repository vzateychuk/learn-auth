package vez.learnauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    @Value("${secret.key.hex}")
    private String secretKey;

    public String extractUsername(String jwtToken) {
        // expect client's username should be in subject claim
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {

        final long expirationMills = 1_000 * 60 * 24;
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject( userDetails.getUsername() )
                .setIssuedAt( new Date(System.currentTimeMillis()) )
                .setExpiration( new Date(System.currentTimeMillis() + expirationMills) )
                .signWith( getSignIngKey(), SignatureAlgorithm.HS256 )
                .compact();
    }

    // validate that token still valid, i.e. belong to the user and not expired
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey( getSignIngKey() )
                .build()
                .parseClaimsJwt(jwtToken)
                .getBody();
    }

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

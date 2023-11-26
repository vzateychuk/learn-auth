package vez.learnauth.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vez.learnauth.UserRepo;
import vez.learnauth.config.JwtTokenService;
import vez.learnauth.user.RoleEntity;
import vez.learnauth.user.UserEntity;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo repo;
    private final PasswordEncoder encoder;
    private final JwtTokenService jwtService;
    private final AuthenticationManager authManager;

    // create user, set token and return response
    public AuthenticationResponse register(RegisterRequest regRequest) {

        UserEntity user = UserEntity.builder()
                .fullname(regRequest.getName())
                .email(regRequest.getEmail())
                .password( encoder.encode(regRequest.getPassword()) )
                .role(RoleEntity.USER)
                .build();
        repo.save(user);

        String jwtToken = jwtService.generateToken(user, Map.of());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    // authenticate user based on userName and password with AuthManager
    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        // throws exception if user is not authenticated
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        UserEntity user = repo.findByEmail(authRequest.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user, Map.of());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

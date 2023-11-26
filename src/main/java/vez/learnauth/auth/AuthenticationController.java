package vez.learnauth.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller works with whitelist endpoints
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping({"", "/"})
    public ResponseEntity<String> getOpen () {
        return ResponseEntity.ok("GET open API");
    }

    @PostMapping({"", "/"})
    public ResponseEntity<String> postOpen () {
        return ResponseEntity.ok("POST reach open API");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (
            @RequestBody AuthenticationRequest authRequest
    ) {
        return ResponseEntity.ok(service.authenticate(authRequest));
    }

}

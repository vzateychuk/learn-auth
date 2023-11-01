package vez.learnauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login/oauth2/code/okta")
public class LoginController {

    @GetMapping({"", "/"})
    public String success(@AuthenticationPrincipal OidcUser principal) {
        log.info("---> SUCCESS ");
        return "Greeting, !" + principal.getFullName();
    }

}

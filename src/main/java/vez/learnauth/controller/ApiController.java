package vez.learnauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    @GetMapping({"/login"})
    public String success() {
        log.info("---> LOGIN ");
        return "Greeting, !";
    }

}

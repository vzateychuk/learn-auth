package vez.learnauth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller works with secured endpoints
@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping({"", "/"})
    public String home() {
        log.info("---> HOME ");
        return "Home secured endpoint, you home!";
    }

}

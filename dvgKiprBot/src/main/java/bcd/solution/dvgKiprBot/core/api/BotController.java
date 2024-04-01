package bcd.solution.dvgKiprBot.core.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class BotController {
    private final BotService service;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotifications(@RequestBody List<Long> agents) {
        service.sendNotifications(agents);
        return ResponseEntity.ok("Notifications were sent");
    }
}

package bcd.solution.dvgKiprBot.core.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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

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

    @PostMapping("/send/tour")
    public ResponseEntity<String> sendTourNotifications(@RequestBody List<Long> agents) {
        service.sendTourNotifications(agents);
        return ResponseEntity.ok("Notifications were sent");
    }
    @PostMapping("/send/call")
    public ResponseEntity<String> sendCallNotifications(@RequestBody List<Long> agents) {
        service.sendCallNotifications(agents);
        return ResponseEntity.ok("Notifications were sent");
    }
}

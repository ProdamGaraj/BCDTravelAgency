package bcd.solution.dvgKiprBot.core.api;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    private final DvgKiprBot bot;

    @SneakyThrows
    public void sendNotifications(List<Long> agents) {
        for (Long id : agents) {
            bot.executeAsync(SendMessage.builder()
                            .chatId(id)
                            .text("Появилась новая заявка. Вы можете посмотреть её на сайте!")
                    .build());
        }
    }
}

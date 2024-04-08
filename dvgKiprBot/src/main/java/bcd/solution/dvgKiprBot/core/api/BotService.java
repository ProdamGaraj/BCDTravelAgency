package bcd.solution.dvgKiprBot.core.api;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    @NonNull
    private final DvgKiprBot bot;
    private final String newTourRequestMsg = "Появилась новая заявка на тур. Вы можете посмотреть её на сайте!";
    private final String newCallRequestMsg = "Появилась новая заявка на звонок. Вы можете посмотреть её на сайте!";

    @SneakyThrows
    public void sendTourNotifications(List<Long> agents) {
        for (Long id : agents) {
            bot.executeAsync(SendMessage.builder()
                            .chatId(id)
                            .text(newTourRequestMsg)
                    .build());
        }
    }
    @SneakyThrows
    public void sendCallNotifications(List<Long> agents) {
        for (Long id : agents) {
            bot.executeAsync(SendMessage.builder()
                            .chatId(id)
                            .text(newCallRequestMsg)
                    .build());
        }
    }
}

package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class HotelHandler {
    @Async
    @SneakyThrows
    public void handleHotelCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }
}

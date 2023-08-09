package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AuthHandler {

    @Async
    @SneakyThrows
    public void authCommandHandler(Message message, DvgKiprBot bot) {

    }
    @Async
    @SneakyThrows
    public void passwordHandler(Message message, DvgKiprBot bot) {

    }
}

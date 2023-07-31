package сore.utils.handlers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import сore.DvgKiprBot;


import java.util.Optional;

public class MessageHandler {
    private final AbsSender bot;

    public MessageHandler(AbsSender bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void handleMessage(Message message) {
        if (!message.hasText()) {
            return;
        }
        if (!message.hasEntities()) {
            //TODO: Проверить что вводиться пароль
        }
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(
                    commandEntity.get().getOffset(),
                    commandEntity.get().getLength());
            switch (command) {
                case "/start":
                    startCommandHandler(message);
                    break;
                case "/customtours":
                    customTourCommandHandler(message);
                    break;
                case "/authorization":
                    authorizationCommandHandler(message);
                    break;
                default:
                    bot.execute(SendMessage.builder()
                            .text("Комманда не найдена")
                            .chatId(message.getChatId())
                            .build());
                    break;
            }

        }
    }

    @SneakyThrows
    private void startCommandHandler(Message message) {
//        TODO: add start command message text
        bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Доступные комманды:\n/start\n/customtours\n/authorization")
                .build());
    }

    @SneakyThrows
    private void authorizationCommandHandler(Message message) {
//        TODO: add message text
        bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Введите пароль")
                .build());
    }

    @SneakyThrows
    private void customTourCommandHandler(Message message) {
//        TODO: add message text
        bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Подбор тура")
                .build());
    }
}

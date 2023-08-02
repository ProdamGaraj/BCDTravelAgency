package сore.utils.handlers;

import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.bots.AbsSender;
import сore.models.CustomTour;
import сore.services.KeyboardService;
import сore.services.MediaService;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageHandler {
    private final AbsSender bot;
    private final Map<Long, Pair<Boolean, Optional<Message>>> is_password = new HashMap<>();
    static final KeyboardService keyboardService = new KeyboardService();

    static final MediaService mediaService = new MediaService();

    public MessageHandler(AbsSender bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void handleMessage(Message message) {
        if (!message.hasText()) {
            return;
        }
        if (!message.hasEntities() && is_password.get(message.getFrom().getId()).getFirst()) {
            passwordHandler(message);
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
                case "/media":
                    mediaCommandHandler(message);
                default:
                    bot.execute(SendPhoto.builder()
                            .caption("Комманда не найдена")
                            .chatId(message.getChatId())
                            .build());
                    break;
            }

        }
    }

    @SneakyThrows
    private void passwordHandler(Message message) {
        String password = message.getText();
        Optional<Message> prevMessageOpt = is_password.get(message.getFrom().getId()).getSecond();
        if (prevMessageOpt.isEmpty()) {
            return;
        }
//        TODO: Auth logic
        bot.execute(EditMessageCaption.builder()
                .chatId(prevMessageOpt.get().getChatId())
                .messageId(prevMessageOpt.get().getMessageId())
                .caption("Пароль получен: " + password)
                .build());
        is_password.put(message.getFrom().getId(), Pair.of(Boolean.FALSE, Optional.empty()));
        bot.execute(DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build());
    }

    @SneakyThrows
    private void startCommandHandler(Message message) {
//        TODO: add start command message text
//        TODO: fix start image to some image of Kipr
        bot.execute(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getStartMessageMedia())
                .caption("Доступные комманды:\n/start\n/customtours\n/authorization")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @SneakyThrows
    private void authorizationCommandHandler(Message message) {
//        TODO: add message text
        Message my_message = bot.execute(SendPhoto.builder()
                .chatId(message.getChatId())
                .caption("Введите пароль")
                .build());
        is_password.put(message.getFrom().getId(), Pair.of(Boolean.TRUE, Optional.of(my_message)));
    }

    @SneakyThrows
    private void customTourCommandHandler(Message message) {
//        TODO: add message text
        bot.execute(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getMediaForCustomTour(new CustomTour()))
                .caption("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @SneakyThrows
    private void mediaCommandHandler(Message message) {
//        TODO: add message text
        bot.execute(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getStartMessageMedia())
                .caption("тестовый вывод фото")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }
}

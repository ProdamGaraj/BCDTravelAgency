package сore.utils.handlers;

import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import сore.services.KeyboardService;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageHandler {
    private final AbsSender bot;
    private final Map<Long, Pair<Boolean, Optional<Message>>> is_password = new HashMap<>();
    static final KeyboardService keyboardService = new KeyboardService();

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
    private void passwordHandler(Message message) {
        String password = message.getText();
        Optional<Message> prevMessageOpt = is_password.get(message.getFrom().getId()).getSecond();
        if (prevMessageOpt.isEmpty()) {
            return;
        }
//        TODO: Auth logic
        bot.execute(EditMessageText.builder()
                .chatId(prevMessageOpt.get().getChatId())
                .messageId(prevMessageOpt.get().getMessageId())
                .text("Пароль получен: " + password)
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
        bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Доступные комманды:\n/start\n/customtours\n/authorization")
                .build());
    }

    @SneakyThrows
    private void authorizationCommandHandler(Message message) {
//        TODO: add message text
        Message my_message = bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Введите пароль")
                .build());
        is_password.put(message.getFrom().getId(), Pair.of(Boolean.TRUE, Optional.of(my_message)));
    }

    @SneakyThrows
    private void customTourCommandHandler(Message message) {
//        TODO: add message text
        bot.execute(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }
}

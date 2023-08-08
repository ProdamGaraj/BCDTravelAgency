package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageHandler {
    private final Map<Long, Pair<Boolean, Optional<Message>>> is_password = new HashMap<>();
    private final KeyboardService keyboardService;

    private final MediaService mediaService;

    @Autowired
    public MessageHandler(KeyboardService keyboardService,
                          MediaService mediaService) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
    }

    @SneakyThrows
    public void handleMessage(Message message, DvgKiprBot bot) {
        if (!message.hasText()) {
            return;
        }
        if (!message.hasEntities() && is_password.get(message.getFrom().getId()).getFirst()) {
            passwordHandler(message, bot);
        }
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(
                    commandEntity.get().getOffset(),
                    commandEntity.get().getLength());
            switch (command) {
                case "/start":
                    startCommandHandler(message, bot);
                    break;
                case "/customtours":
                    customTourCommandHandler(message, bot);
                    break;
                case "/authorization":
                    authorizationCommandHandler(message, bot);
                    break;
                case "/media":
                    mediaCommandHandler(message, bot);
                default:
                    bot.executeAsync(SendPhoto.builder()
                            .caption("Комманда не найдена")
                            .chatId(message.getChatId())
                            .build());
                    break;
            }

        }
    }

    @SneakyThrows
    private void passwordHandler(Message message, DvgKiprBot bot) {
        String password = message.getText();
        Optional<Message> prevMessageOpt = is_password.get(message.getFrom().getId()).getSecond();
        if (prevMessageOpt.isEmpty()) {
            return;
        }
//        TODO: Auth logic
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(prevMessageOpt.get().getChatId())
                .messageId(prevMessageOpt.get().getMessageId())
                .caption("Пароль получен: " + password)
                .build());
        is_password.put(message.getFrom().getId(), Pair.of(Boolean.FALSE, Optional.empty()));
        bot.executeAsync(DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build());
    }

    @SneakyThrows
    private void startCommandHandler(Message message, DvgKiprBot bot) {
//        TODO: add start command message text
//        TODO: fix start image to some image of Kipr
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getStartMessageMedia())
                .caption("Кипр - это островное государство в Средиземном море," +
                        "расположенное на перекрестке Европы, Азии и Африки. " +
                        "Он является отличным туристическим направлением благодаря своим красивым пляжам, теплому климату и богатой истории." +
                        "Кипр имеет богатое культурное наследие, которое отражается в его архитектуре, музеях и археологических раскопках." +
                        "Столицей Кипра является Никосия, где можно посетить множество достопримечательностей, таких как Кипрский музей, " +
                        "Собор Святого Иоанна и Кипрский национальный парк. Кипр также славится своими винами и кухней, в которой сочетаются греческие," +
                        " турецкие и английские влияния. В целом, Кипр - это прекрасное место для отдыха и изучения культуры, истории и кухни Средиземноморья.\n")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @SneakyThrows
    private void authorizationCommandHandler(Message message, DvgKiprBot bot) {
//        TODO: add message text
        Message my_message = bot.execute(SendPhoto.builder() //executeAsync
                .chatId(message.getChatId())
                .caption("Введите пароль")
                .build());
        is_password.put(message.getFrom().getId(), Pair.of(Boolean.TRUE, Optional.of(my_message)));
    }

    @SneakyThrows
    private void customTourCommandHandler(Message message, DvgKiprBot bot) {
//        TODO: add message text
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getMediaForCustomTour(new CustomTour()))
                .caption("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @SneakyThrows
    private void mediaCommandHandler(Message message, DvgKiprBot bot) {
//        TODO: add message text
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getStartMessageMedia())
                .caption("тестовый вывод фото")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }
}

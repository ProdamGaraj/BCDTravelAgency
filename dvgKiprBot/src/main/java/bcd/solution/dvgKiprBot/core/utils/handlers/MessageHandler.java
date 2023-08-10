package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import java.util.concurrent.CompletableFuture;

@Component
public class MessageHandler {
    private final KeyboardService keyboardService;

    private final MediaService mediaService;

    private final CommandsHandler commandsHandler;

    private final AuthHandler authHandler;
    private final StateMachineService stateMachineService;

    public MessageHandler(KeyboardService keyboardService,
                          MediaService mediaService,
                          CommandsHandler commandsHandler,
                          AuthHandler authHandler,
                          StateMachineService stateMachineService) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.commandsHandler = commandsHandler;
        this.authHandler = authHandler;
        this.stateMachineService = stateMachineService;
    }

    @Async
    @SneakyThrows
    public void handleMessage(Message message, DvgKiprBot bot) {

        if (!message.hasText()) {
            return;
        }
        if (!message.hasEntities()) {
            StateMachine stateMachine = stateMachineService.getByUserId(message.getFrom().getId());
            if (stateMachine.wait_password) {
                authHandler.passwordHandler(message, bot, stateMachine);
            }
            return;
        }
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(
                    commandEntity.get().getOffset(),
                    commandEntity.get().getLength());
            switch (command) {
                case "/start":
                    commandsHandler.startHandler(message, bot);
                    break;
                case "/customtours":
                    commandsHandler.tourChoosingHandler(message, bot);
                    break;
                case "/authorization":
                    authHandler.authCommandHandler(message, bot);
                    break;
                case "/media":
                    commandsHandler.mediaHandler(message, bot);
                    break;
                default:
                    bot.executeAsync(SendMessage.builder()
                            .text("Команда не найдена")
                            .chatId(message.getChatId())
                            .build());
                    break;
            }

        }
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

package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;


import java.util.Optional;

@Component
public class MessageHandler {

    private final CommandsHandler commandsHandler;

    private final AuthHandler authHandler;
    private final StateMachineService stateMachineService;

    public MessageHandler(CommandsHandler commandsHandler,
                          AuthHandler authHandler,
                          StateMachineService stateMachineService) {
        this.commandsHandler = commandsHandler;
        this.authHandler = authHandler;
        this.stateMachineService = stateMachineService;
    }

    @Async
    @SneakyThrows
    public void handleMessage(Message message, DvgKiprBot bot) {

        if (message.hasContact()) {
            authHandler.contactHandler(message, bot);
        }
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
                case "/phone":
                    commandsHandler.phoneHandler(message, bot);
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
}

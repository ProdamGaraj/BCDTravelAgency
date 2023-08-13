package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import bcd.solution.dvgKiprBot.core.services.UserService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.concurrent.CompletableFuture;

@Component
public class AuthHandler {

    private final StateMachineService stateMachineService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;
    private final UserService userService;
    private final CommandsHandler commandsHandler;

    public AuthHandler(StateMachineService stateMachineService,
                       MediaService mediaService,
                       KeyboardService keyboardService,
                       UserService userService,
                       CommandsHandler commandsHandler) {
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
        this.userService = userService;
        this.commandsHandler = commandsHandler;
    }

    @Async
    @SneakyThrows
    public void handleCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "auth_cancel" -> handleCallback(callbackQuery, bot);
            case "auth_getPhone" -> getPhoneHandler(callbackQuery, bot);
            case "auth_phoneCancel" -> phoneCancelHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    protected void phoneCancelHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(DeleteMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        bot.executeAsync(SendMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .text("Хорошо, но Вы всегда сможете его нам его отправить")
                        .replyMarkup(new ReplyKeyboardRemove(true))
                .build());
        commandsHandler.choosingMessageSender(
                callbackQuery.getMessage().getChatId(),
                bot,
                userService.hasPhoneById(callbackQuery.getFrom().getId()));
    }

    @Async
    @SneakyThrows
    public void contactHandler(Message message, DvgKiprBot bot) {
        StateMachine stateMachine = stateMachineService.getByUserId(message.getFrom().getId());
        String phoneNumber = message.getContact().getPhoneNumber();

        userService.setPhoneById(message.getFrom().getId(), phoneNumber);

        bot.executeAsync(DeleteMessage.builder()
                        .chatId(message.getChatId())
                        .messageId(stateMachine.phoneMessageId)
                .build());
        bot.executeAsync(DeleteMessage.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                .build());
        bot.executeAsync(SendMessage.builder()
                        .chatId(message.getChatId())
                        .text("Спасибо, что предоставили Ваш номер телефона! Теперь Вам доступен конструктор туров")
                        .replyMarkup(new ReplyKeyboardRemove(true))
                .build());
        stateMachineService.setWaitPhoneByUserId(
                message.getFrom().getId(),
                false, 0);
        commandsHandler.startHandler(message, bot);
    }

    @Async
    @SneakyThrows
    protected void getPhoneHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageCaption.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .replyMarkup(null)
                .build());
        bot.executeAsync(SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Пожалуйста, отправьте свой контакт.")
                        .replyMarkup(keyboardService.getPhoneKeyboard())
                .build());
        Message message = bot.executeAsync(SendMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .text("Для этого воспользуйтесь клавиатурой")
                        .replyMarkup(keyboardService.getPhoneCancelKeyboard())
                .build()).join();
        stateMachineService.setWaitPhoneByUserId(
                callbackQuery.getFrom().getId(),
                true,
                message.getMessageId());

        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }

    @Async
    @SneakyThrows
    public void authCommandHandler(Message message, DvgKiprBot bot) {
//        TODO: add message text
        CompletableFuture<Message> auth_message = bot.executeAsync(SendPhoto.builder() //executeAsync
                .chatId(message.getChatId())
                .photo(mediaService.getAuthMedia())
                .caption("Введите пароль")
                .replyMarkup(keyboardService.getAuthCancelKeyboard())
                .build());

        stateMachineService.setWaitPasswordByUserId(
                message.getFrom().getId(),
                true,
                auth_message.join().getMessageId());
    }

    @Async
    @SneakyThrows
    public void passwordHandler(Message message, DvgKiprBot bot, StateMachine stateMachine) {
        String password = message.getText();

//        TODO: Auth logic
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(message.getChatId())
                .messageId(stateMachine.auth_message_id)
                .caption("Пароль получен: " + password)
                .build());
        stateMachineService.setWaitPasswordByUserId(message.getFrom().getId(), false, 0);
        bot.executeAsync(DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build());
    }

    @Async
    @SneakyThrows
    protected void cancelHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Ввод пароля отменен")
                .replyMarkup(null)
                .build());
        stateMachineService.setWaitPasswordByUserId(callbackQuery.getFrom().getId(), false, 0);
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }

}

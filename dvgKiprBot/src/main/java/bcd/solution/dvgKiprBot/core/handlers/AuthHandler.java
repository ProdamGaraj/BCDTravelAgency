package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.*;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinAllChatMessages;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Component
public class AuthHandler {

    private final StateMachineService stateMachineService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final CommandsHandler commandsHandler;

    public AuthHandler(StateMachineService stateMachineService,
                       MediaService mediaService,
                       KeyboardService keyboardService,
                       UserService userService,
                       AuthorizationService authorizationService,
                       CommandsHandler commandsHandler) {
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.commandsHandler = commandsHandler;
    }

    @Async
    @SneakyThrows
    public void handleCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "auth" -> authHandler(callbackQuery, bot);
            case "auth_cancel" -> cancelHandler(callbackQuery, bot);
            case "auth_getPhone" -> getPhoneHandler(callbackQuery, bot);
            case "auth_phoneCancel" -> phoneCancelHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    protected void authHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getAuthMedia())
                .build());
        if (authorizationService.isAuthorized(callbackQuery.getFrom().getId())) {
            bot.executeAsync(EditMessageCaption.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .caption("Вы уже авторизованы")
                    .replyMarkup(keyboardService.getRestartKeyboard())
                    .build());
            bot.executeAsync(UnpinAllChatMessages.builder().chatId(callbackQuery.getMessage().getChatId()).build());
            bot.executeAsync(PinChatMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .build());
            return;
        }

        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Введите пароль")
                .replyMarkup(keyboardService.getAuthCancelKeyboard())
                .build());

        stateMachineService.setWaitPasswordByUserId(
                callbackQuery.getFrom().getId(),
                true,
                callbackQuery.getMessage().getMessageId());
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
                userService.hasPhoneById(callbackQuery.getFrom().getId()),
                userService.isAuthorized(callbackQuery.getFrom().getId()));
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
                .text("Спасибо, что предоставили Ваш номер телефона!"
//                        + " Теперь Вам доступен конструктор туров"
                )
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

        if (authorizationService.isAuthorized(message.getFrom().getId())) {
            CompletableFuture<Message> auth_message = bot.executeAsync(SendPhoto.builder()
                    .chatId(message.getChatId())
                    .photo(mediaService.getAuthFile())
                    .caption("Вы уже авторизованы")
                    .replyMarkup(keyboardService.getRestartKeyboard())
                    .build());
            bot.executeAsync(UnpinAllChatMessages.builder().chatId(message.getChatId()).build());
            bot.executeAsync(PinChatMessage.builder()
                    .chatId(message.getChatId())
                    .messageId(auth_message.join().getMessageId())
                    .build());
            return;
        }

        CompletableFuture<Message> auth_message = bot.executeAsync(SendPhoto.builder() //executeAsync
                .chatId(message.getChatId())
                .photo(mediaService.getAuthFile())
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

        if (!authorizationService.authByPassword(message.getFrom().getId(), password)) {
            try {
                bot.execute(EditMessageCaption.builder()
                        .chatId(message.getChatId())
                        .messageId(stateMachine.auth_message_id)
                        .caption("Пароль не найден, попробуйте снова")
                        .build());
            } catch (TelegramApiRequestException ignored) {

            }

        } else {
            bot.execute(EditMessageCaption.builder()
                    .chatId(message.getChatId())
                    .messageId(stateMachine.auth_message_id)
                    .caption("Вы авторизованы!")
                    .replyMarkup(keyboardService.getRestartKeyboard())
                    .build());

            bot.execute(PinChatMessage.builder()
                    .chatId(message.getChatId())
                    .messageId(stateMachine.auth_message_id)
                    .disableNotification(true)
                    .build());
            stateMachineService.setWaitPasswordByUserId(message.getFrom().getId(), false, 0);
        }
        bot.executeAsync(DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build());
//        commandsHandler.choosingMessageSender(
//                message.getChatId(),
//                bot, userService.hasPhoneById(message.getFrom().getId()));
    }

    @Async
    @SneakyThrows
    protected void cancelHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Ввод пароля отменен")
                .replyMarkup(keyboardService.getRestartKeyboard())
                .build());
        stateMachineService.setWaitPasswordByUserId(callbackQuery.getFrom().getId(), false, 0);
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }

}

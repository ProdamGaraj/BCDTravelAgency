package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.concurrent.CompletableFuture;

@Component
public class AuthHandler {

    private final StateMachineService stateMachineService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;

    public AuthHandler(StateMachineService stateMachineService,
                       MediaService mediaService,
                       KeyboardService keyboardService) {
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
    }

    @Async
    @SneakyThrows
    public void contactHandler(Message message, DvgKiprBot bot) {
        String phoneNumber = message.getContact().getPhoneNumber();
        bot.execute(SendMessage.builder()
                        .chatId(message.getChatId())
                        .text(phoneNumber)
                        .replyMarkup(new ReplyKeyboardRemove(true))
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
    public void cancelHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
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

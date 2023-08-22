package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class FeedbackHandler {
    private final StateMachineService stateMachineService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;


    private final Long managerId;
    private final String managerUsername;

    public FeedbackHandler(StateMachineService stateMachineService,
                           MediaService mediaService,
                           KeyboardService keyboardService,
                           @Value("${bot.managerId}") Long managerId,
                           @Value("${bot.managerUsername}") String managerUsername) {
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;

        this.managerId = managerId;
        this.managerUsername = managerUsername;
    }


    @Async
    @SneakyThrows
    public void feedbackHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        String managerTourCard = stateMachineService.getManagerCardByUserId(callbackQuery.getFrom().getId());
        bot.executeAsync(SendMessage.builder()
                .chatId(this.managerId)
                .text(managerTourCard)
                .build());
        String userTourCard = stateMachineService.getUserCardByUserId(
                callbackQuery.getFrom().getId(),
                this.managerUsername
        );
        stateMachineService.clearStateByUserId(callbackQuery.getFrom().getId());

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getFeedbackMedia())
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(userTourCard)
                .replyMarkup(keyboardService.getRestartKeyboard())
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

package bcd.solution.dvgKiprBot.core.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
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


    private final Long managerId;
    private final String managerUsername;

    public FeedbackHandler(StateMachineService stateMachineService,
                           MediaService mediaService,
                           @Value("${bot.managerId}") Long managerId,
                           @Value("${bot.managerUsername}") String managerUsername) {
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;

        this.managerId = managerId;
        this.managerUsername = managerUsername;
    }


    @Async
    @SneakyThrows
    public void feedbackHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        String userTourCard = stateMachineService.getFinalCardByUserId(callbackQuery.getFrom().getId());
        bot.executeAsync(SendMessage.builder()
                .chatId(this.managerId)
                .text(userTourCard)
                .build());
        stateMachineService.clearStateByUserId(callbackQuery.getFrom().getId());

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getFeedbackMedia())
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Спасибо, что выбрали нас!\n\n" +
                        "Обратитесь к менеджеру (@" + this.managerUsername + ") " +
                        "для оформления выбранного тура")
                .replyMarkup(null)
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

package bcd.solution.dvgKiprBot.core.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.services.CustomToursService;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Optional;

@Component
public class CustomTourHandler {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final CustomToursService customToursService;
    private final StateMachineService stateMachineService;
    private final FeedbackHandler feedbackHandler;

    public CustomTourHandler(KeyboardService keyboardService,
                             MediaService mediaService,
                             CustomToursService customToursService,
                             StateMachineService stateMachineService,
                             FeedbackHandler feedbackHandler) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.customToursService = customToursService;
        this.stateMachineService = stateMachineService;
        this.feedbackHandler = feedbackHandler;
    }

    @Async
    @SneakyThrows
    public void handleCustomTourCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Look comments in activity handler class
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "customTours" -> defaultHandler(callbackQuery, bot);
            case "customTours_select" -> selectHandler(callbackQuery, bot);
            case "customTours_change" -> changeHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);

        CustomTour currentTour = customToursService.getByIndex(index);

        bot.executeAsync(EditMessageMedia.builder()
            .chatId(callbackQuery.getMessage().getChatId())
            .messageId(callbackQuery.getMessage().getMessageId())
            .media(mediaService.getCustomTourMedia(currentTour))
            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentTour.toString())
                .replyMarkup(keyboardService.getCustomToursKeyboard(index, currentTour.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @Async
    @SneakyThrows
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Long customTourId = Long.parseLong(callbackQuery.getData().split("/")[1]);

        Optional<CustomTour> selectedCustomTour = customToursService.getById(customTourId);
        if (selectedCustomTour.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Авторский тур не найден, попробуйте позже")
                    .build());
            return;
        }

        stateMachineService.setCustomTourByUserId(selectedCustomTour.get(), callbackQuery.getFrom().getId());

        feedbackHandler.feedbackHandler(callbackQuery, bot);
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        CustomTour currentTour = customToursService.getByIndex(0);

        bot.executeAsync(EditMessageMedia.builder()
            .chatId(callbackQuery.getMessage().getChatId())
            .messageId(callbackQuery.getMessage().getMessageId())
            .media(mediaService.getCustomTourMedia(currentTour))
            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentTour.toString())
                .replyMarkup(keyboardService.getCustomToursKeyboard(0, currentTour.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

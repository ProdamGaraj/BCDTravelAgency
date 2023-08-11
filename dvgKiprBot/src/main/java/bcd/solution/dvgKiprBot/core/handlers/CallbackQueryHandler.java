package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.services.*;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.ActivityHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.CustomTourHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.HotelHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.ResortHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler {
    //Services
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final StateMachineService stateMachineService;
    //Handlers
    private final AuthHandler authHandler;
    private final ActivityHandler activityHandler;
    private final CustomTourHandler customTourHandler;
    private final HotelHandler hotelHandler;
    private final ResortHandler resortHandler;

    @Autowired
    public CallbackQueryHandler(KeyboardService keyboardService,
                                MediaService mediaService,
                                StateMachineService stateMachineService,

                                AuthHandler authHandler,
                                ActivityHandler activityHandler,
                                CustomTourHandler customTourHandler,
                                HotelHandler hotelHandler,
                                ResortHandler resortHandler) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.stateMachineService = stateMachineService;

        this.authHandler = authHandler;
        this.activityHandler = activityHandler;
        this.customTourHandler = customTourHandler;
        this.hotelHandler = hotelHandler;
        this.resortHandler = resortHandler;
    }


    @Async
    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Structure of callback data:
//        {action group}[_{action}(optional)]/{current index (by default 0)}[/{current entity id}(optional)]

        String callback_action = callbackQuery.getData().split("_")[0];

        switch (callback_action) {
            case "restart" -> restartHandler(callbackQuery, bot);
            case "auth" -> authHandler.cancelHandler(callbackQuery, bot);
            case "resorts" -> resortHandler.handleResortCallback(callbackQuery, bot);
            case "customTours" -> customTourHandler.handleCustomTourCallback(callbackQuery, bot);
            case "activities" -> activityHandler.handleActivityCallback(callbackQuery, bot);
            case "hotels" -> hotelHandler.handleHotelCallback(callbackQuery, bot);
            default -> bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .text("Здесь пока что ничего нет, но очень скоро появится")
                    .showAlert(Boolean.TRUE)
                    .build());
        }
    }

    @Async
    @SneakyThrows
    private void restartHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        stateMachineService.clearStateByUserId(callbackQuery.getFrom().getId());

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForStart())
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

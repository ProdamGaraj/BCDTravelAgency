package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.services.*;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.ActivityHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.CustomTourHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.HotelHandler;
import bcd.solution.dvgKiprBot.core.handlers.selectHandlers.ResortHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler {
    //Services
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final StateMachineService stateMachineService;
    private final UserService userService;
    //Handlers
    private final AuthHandler authHandler;
    private final ActivityHandler activityHandler;
    private final CustomTourHandler customTourHandler;
    private final HotelHandler hotelHandler;
    private final ResortHandler resortHandler;
    private final CommandsHandler commandsHandler;

    @Autowired
    public CallbackQueryHandler(KeyboardService keyboardService,
                                MediaService mediaService,
                                StateMachineService stateMachineService,
                                UserService userService,

                                AuthHandler authHandler,
                                ActivityHandler activityHandler,
                                CustomTourHandler customTourHandler,
                                HotelHandler hotelHandler,
                                ResortHandler resortHandler,
                                CommandsHandler commandsHandler) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.stateMachineService = stateMachineService;
        this.userService = userService;

        this.authHandler = authHandler;
        this.activityHandler = activityHandler;
        this.customTourHandler = customTourHandler;
        this.hotelHandler = hotelHandler;
        this.resortHandler = resortHandler;
        this.commandsHandler = commandsHandler;
    }


    @Async
    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Structure of callback data:
//        {action group}[_{action}(optional)]/{current index (by default 0)}[/{current entity id}(optional)]

        String callback_action = callbackQuery.getData().split("_")[0];

        switch (callback_action) {
            case "null" -> nothingHandler(callbackQuery, bot);
            case "restart" -> restartHandler(callbackQuery, bot);
            case "start" -> startHandler(callbackQuery, bot);
            case "tour" -> tourConstructorHandler(callbackQuery, bot);
            case "auth" -> authHandler.handleCallback(callbackQuery, bot);
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
    protected void tourConstructorHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getTourConstructorMedia())
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("От чего Вы хотите отталкиваться при подборе отеля?")
                .replyMarkup(keyboardService.getTourConstructorKeyboard())
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }

    @Async
    @SneakyThrows
    protected void nothingHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    protected void restartHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        stateMachineService.clearStateByUserId(callbackQuery.getFrom().getId());

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getStartMedia())
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(commandsHandler.inviteString)
                .replyMarkup(keyboardService.getTourChoosingKeyboard(
                        userService.hasPhoneById(callbackQuery.getFrom().getId()),
                        userService.isAuthorized(callbackQuery.getFrom().getId())))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    protected void startHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        if (!userService.hasPhoneById(callbackQuery.getFrom().getId())) {
            bot.executeAsync(EditMessageMedia.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .media(mediaService.getStartMedia())
                    .build());
            bot.executeAsync(EditMessageCaption.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .caption("Для доступа к полному функционалу бота необходимо указать номер телефона."
//                        + " Но Вы все равно можете выбрать один из авторских туров"
                    )
//                    .caption("Для повышения качесва обслуживания нам неоходим Ваш номер телефона")
                    .replyMarkup(keyboardService.getStarterKeyboard())
                    .build());

            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId()).build());
            return;
        }

        commandsHandler.choosingMessageSender(
                callbackQuery.getMessage().getChatId(),
                bot,
                userService.hasPhoneById(callbackQuery.getFrom().getId()),
                userService.isAuthorized(callbackQuery.getFrom().getId()));
        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(null)
                .build());
    }
}

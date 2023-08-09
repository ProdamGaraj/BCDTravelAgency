package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.services.*;
import bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers.ActivityHandler;
import bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers.CustomTourHandler;
import bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers.HotelHandler;
import bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers.ResortHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.Console;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CallbackQueryHandler {
    //Services
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final ActivityService activityService;
    private final ResortService resortService;
    private final CustomToursService customToursService;
    private final HotelService hotelService;
    //Handlers
    private final AuthHandler authHandler;
    private final ActivityHandler activityHandler;
    private final CustomTourHandler customTourHandler;
    private final HotelHandler hotelHandler;
    private final ResortHandler resortHandler;

    //Maps
    static final Map<Long, List<Pair<Integer, String>>> activity_lists = new HashMap<>();


    @Autowired
    public CallbackQueryHandler(ActivityService activityService,
                                ResortService resortService,
                                CustomToursService customToursService,
                                HotelService hotelService,
                                KeyboardService keyboardService,
                                MediaService mediaService,

                                AuthHandler authHandler,
                                ActivityHandler activityHandler,
                                CustomTourHandler customTourHandler,
                                HotelHandler hotelHandler,
                                ResortHandler resortHandler) {
        this.activityService = activityService;
        this.resortService = resortService;
        this.customToursService = customToursService;
        this.hotelService = hotelService;
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;

        this.authHandler = authHandler;
        this.activityHandler = activityHandler;
        this.customTourHandler = customTourHandler;
        this.hotelHandler = hotelHandler;
        this.resortHandler = resortHandler;

        //TODO: initialize sevices
    }


    @Async
    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery, DvgKiprBot bot) {

        //TODO: rewrite?
        //As an idea we should use if else statements to go easier inside of our algorithm paths
        String callback_action = callbackQuery.getData().split("_")[0];

        switch (callback_action) {
            case "restart" -> restartHandler(callbackQuery, bot);
            case "auth" -> authHandler.cancelHandler(callbackQuery, bot);
            case "resorts" -> resortHandler.handleResortCallback(callbackQuery, bot);
//            case "resorts" -> resortService.resortsChooseHandler(callbackQuery, bot);
//            case "resort_left/" -> resortService.resort_leftHandler(callbackQuery, bot);
//            case "resort_right/" -> resortService.resort_rightHandler(callbackQuery, bot);
//            case "resort_select/" -> resortService.resort_select(callbackQuery, bot);
            case "customTours" -> customTourHandler.handleCustomTourCallback(callbackQuery, bot);
//            case "personal_tours" -> customToursService.personalToursChooseHandler(callbackQuery, bot);
//            case "personalTour_left" -> customToursService.personalTour_leftHandler(callbackQuery, bot);
//            case "personalTour_right" -> customToursService.personalTour_rightHandler(callbackQuery, bot);
//            case "personalTour_select" -> customToursService.personalTour_select(callbackQuery, bot);
            case "activities" -> activityHandler.handleActivityCallback(callbackQuery, bot);
//            case "activities" -> activityService.activitiesChooseHandler(callbackQuery, bot);
//            case "activity_left" -> activityService.activity_leftHandler(callbackQuery, bot);
//            case "activity_right" -> activityService.activity_rightHandler(callbackQuery, bot);
//            case "activity_select" -> activityService.activity_select(callbackQuery, bot);
            case "hotels" -> hotelHandler.handleHotelCallback(callbackQuery, bot);
//            case "hotels" -> hotelService.hotelsChooseHandler(callbackQuery, bot);
//            case "hotel_left" -> hotelService.hotels_leftHandler(callbackQuery, bot);
//            case "hotel_right" -> hotelService.hotel_rightHandler(callbackQuery, bot);
//            case "hotel_select" -> hotelService.hotel_select(callbackQuery, bot);
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

    @Async
    @SneakyThrows
    private void activityAddHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        List<String> args = List.of(callbackQuery.getData().split(":"));
        Integer index = Integer.getInteger(args.get(2));
        String name = args.get(1);
        activity_lists.get(callbackQuery.getFrom().getId()).add(Pair.of(index, name));

        List<Pair<Integer, String>> cur_list = activity_lists.get(callbackQuery.getFrom().getId());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(args.toString() + "\nЗдесь будет список типов активностей:\n")//TODO: uncomment this  + cur_list.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(0))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }
}

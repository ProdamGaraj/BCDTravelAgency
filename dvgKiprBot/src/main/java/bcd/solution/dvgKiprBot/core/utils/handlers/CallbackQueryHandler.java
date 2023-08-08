package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.services.*;
import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackQueryHandler {
    private final AbsSender bot;

    //Services
    private static final KeyboardService keyboardService = new KeyboardService();
    private static final MediaService mediaService = new MediaService();
    private final ActivityService activityService;
    private final ResortService resortService;
    private final CustomToursService customToursService;
    private final HotelService hotelService;
    //Repositories

    private final HotelRepo hotelRepo = new HotelRepo();

    //Maps
    static final Map<Long, List<Pair<Integer, String>>> activity_lists = new HashMap<>();
    static final Map<Long, Integer> selectedActivity = new HashMap<>();
    static final Map<Long, Integer> selectedResort = new HashMap<>();
    static final Map<Long, Integer> selectedHotel = new HashMap<>();




    public CallbackQueryHandler(AbsSender bot) {
        this.bot = bot;
        this.activityService = new ActivityService(bot);
        this.resortService = new  ResortService(bot);
        this.customToursService = new CustomToursService(bot);
        this.hotelService = new HotelService(bot);


        //TODO: initialize sevices
    }


    @Async
    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery) {

        //TODO: rewrite?
        //As an idea we should use if else statements to go easier inside of our algorithm paths
        String callback_data = callbackQuery.getData();
        switch (callback_data) {
            case "restart":
                restartHandler(callbackQuery);
                break;

            case "resorts":
                resortService.resortsChooseHandler(callbackQuery);
                break;
            case "resort_left/":
                resortService.resort_leftHandler(callbackQuery);
                break;
            case "resort_right/":
                resortService.resort_rightHandler(callbackQuery);
                break;
            case "resort_select/":
                resortService.resort_select(callbackQuery);
                break;

            case "personal_tours":
                customToursService.personalToursChooseHandler(callbackQuery);
                break;
            case "personalTour_left":
                customToursService.personalTour_leftHandler(callbackQuery);
                break;
            case "personalTour_right":
                customToursService.personalTour_rightHandler(callbackQuery);
                break;
            case "personalTour_select":
                customToursService.personalTour_select(callbackQuery);
                break;

            case "activities":
                activityService.activitiesChooseHandler(callbackQuery);
                break;
            case "activity_left":
                activityService.activity_leftHandler(callbackQuery);
                break;
            case "activity_right":
                activityService.activity_rightHandler(callbackQuery);
                break;
            case "activity_select":
                activityService.activity_select(callbackQuery);
                break;

            case "hotels":
                hotelService.hotelsChooseHandler(callbackQuery);
                break;
            case "hotel_left":
                hotelService.hotels_leftHandler(callbackQuery);
                break;
            case "hotel_right":
                hotelService.hotel_rightHandler(callbackQuery);
                break;
            case "hotel_select":
                hotelService.hotel_select(callbackQuery);
                break;

            default:
                bot.executeAsync(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Здесь пока что ничего нет, но очень скоро появится")
                        .showAlert(Boolean.TRUE)
                        .build());
                break;
        }
    }
    @SneakyThrows
    private void restartHandler(CallbackQuery callbackQuery) {
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

    @SneakyThrows
    private void activityAddHandler(CallbackQuery callbackQuery) {
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

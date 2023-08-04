package core.utils.handlers;

import core.models.Activity;
import core.repository.TemporaryRepos.ActivityRepo;
import core.repository.TemporaryRepos.CustomTourRepo;
import core.repository.TemporaryRepos.HotelRepo;
import core.repository.TemporaryRepos.ResortRepo;
import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import core.models.CustomTour;
import core.models.Resort;
import core.services.KeyboardService;
import core.services.MediaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackQueryHandler {
    private final AbsSender bot;

    //Services
    static final KeyboardService keyboardService = new KeyboardService();
    static final MediaService mediaService = new MediaService();

    //Repositories
    private final ActivityRepo activityRepo = new ActivityRepo();
    private final ResortRepo resortRepo = new ResortRepo();
    private final CustomTourRepo customTourRepo = new CustomTourRepo();
    private final HotelRepo hotelRepo = new HotelRepo();

    //Maps
    static final Map<Long, List<Pair<Integer, String>>> activity_lists = new HashMap<>();
    static final Map<Long, Integer> selectedActivity = new HashMap<>();
    static final Map<Long, Integer> selectedResort = new HashMap<>();
    static final Map<Long, Integer> selectedHotel = new HashMap<>();

    public CallbackQueryHandler(AbsSender bot) {
        this.bot = bot;
    }


    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery) {
        String callback_data = callbackQuery.getData();
        switch (callback_data) {
            case "restart":
                restartHandler(callbackQuery);
                break;

            case "resorts":
                resortsChooseHandler(callbackQuery);
                break;
            case "resort_left/":
                resort_leftHandler(callbackQuery);
                break;
            case "resort_right/":
                resort_rightHandler(callbackQuery);
                break;
            case "resort_select/":
                resort_select(callbackQuery);
                break;

            case "personal_tours":
                personalToursChooseHandler(callbackQuery);
                break;
            case "personalTour_left":
                personalTour_leftHandler(callbackQuery);
                break;
            case "personalTour_right":
                personalTour_rightHandler(callbackQuery);
                break;
            //TODO: Uncomment this after showdown is over
            //case "personalTour_select":
            //    personalTour_select(callbackQuery);
            //    break;

            case "activities":
                activitiesChooseHandler(callbackQuery);
                break;
            case "activity_left":
                activity_leftHandler(callbackQuery);
                break;
            case "activity_right":
                activity_rightHandler(callbackQuery);
                break;
            case "activity_select":
                activity_select(callbackQuery);
                break;

            default:
                bot.execute(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Здесь пока что ничего нет, но очень скоро появится")
                        .showAlert(Boolean.TRUE)
                        .build());
                break;
        }
    }

    @SneakyThrows
    private void activityAddHandler(CallbackQuery callbackQuery) {
        List<String> args = List.of(callbackQuery.getData().split(":"));
        Integer index = Integer.getInteger(args.get(2));
        String name = args.get(1);
        activity_lists.get(callbackQuery.getFrom().getId()).add(Pair.of(index, name));

        List<Pair<Integer, String>> cur_list = activity_lists.get(callbackQuery.getFrom().getId());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(args.toString() + "\nЗдесь будет список типов активностей:\n")//TODO: uncomment this  + cur_list.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(0))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @SneakyThrows
    private void restartHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForStart())
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void activitiesChooseHandler(CallbackQuery callbackQuery) {
        if (!activity_lists.containsKey(callbackQuery.getFrom().getId())) {
            activity_lists.put(callbackQuery.getFrom().getId(), new ArrayList<>());
        }
        List<Pair<Integer, String>> cur_list = activity_lists.get(callbackQuery.getFrom().getId());
        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForActivity(new Activity()))
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список ваших активностей:\n" + cur_list.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(0))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void activity_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= activityRepo.activityList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getActivitiesKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void activity_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= activityRepo.activityList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getActivitiesKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    private void activity_select(CallbackQuery callbackQuery) {

    }

    @SneakyThrows
    private void resortsChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForResort(new Resort()))
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список курортов:")
                .replyMarkup(keyboardService.getResortsKeyboard(0))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void resort_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= selectedActivity.size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)

        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getResortsKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void resort_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= resortRepo.resortList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)

        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getResortsKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void resort_select(CallbackQuery callbackQuery) {

        //TODO: this code suck ass, so it has to be rewrited! I suppose we
        // need some service wich will always give us needed id, and also we need to add some data
        // to callback data, but not as i did it. This is disgusting! I hate myself.

        String data = callbackQuery.getData();
        Integer id = Integer.parseInt(data.substring(data.lastIndexOf("/")));
        selectedResort.put(0L, id);
        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .text("Вы выбрали " + resortRepo.resortList().get(id).name)
                .showAlert(Boolean.TRUE)
                .build());
    }

    @SneakyThrows
    private void personalToursChooseHandler(CallbackQuery callbackQuery) {

        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForCustomTour(new CustomTour()))
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список авторских туров:")
                .replyMarkup(keyboardService.getPersonalToursKeyboard(0))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void personalTour_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= customTourRepo.customTourList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getPersonalToursKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void personalTour_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= customTourRepo.customTourList().size()) {
            index = 0;
        }

        bot.execute(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getPersonalToursKeyboard(index))
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    private void personalTour_select(CallbackQuery callbackQuery) {
    }
}

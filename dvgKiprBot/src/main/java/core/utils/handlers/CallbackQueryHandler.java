package core.utils.handlers;

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
            case "activities":
                activitiesChooseHandler(callbackQuery);
                break;
            case "resorts":
                resortsChooseHandler(callbackQuery);
                break;
            case "resort_left":
                resort_leftHandler(callbackQuery);
                break;
            case "resort_right":
                resort_rightHandler(callbackQuery);
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
            case "activity_left":
                activity_leftHandler(callbackQuery);
                break;
            case "activity_right":
                activity_rightHandler(callbackQuery);
                break;
            default:
                if (callback_data.startsWith("activity:")) {
                    activityAddHandler(callbackQuery);
                }
                bot.execute(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Что-то пошло не так")
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
                .caption(args.toString() + "\nCписок типов активностей:\n" + cur_list.toString())
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
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Cписок типов активностей:\n" + cur_list.toString())
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

        if (index < 0 || index >=  selectedActivity.size()) {
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

        if (index < 0 || index >=  selectedActivity.size()) {
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
    private void resortsChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForResort(new Resort()))
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Cписок курортов:")
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

        if (index < 0 || index >=  selectedActivity.size()) {
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

        if (index < 0 || index >=  selectedActivity.size()) {
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
    private void personalToursChooseHandler(CallbackQuery callbackQuery) {

        bot.execute(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForCustomTour(new CustomTour()))
                .build());
        bot.execute(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Cписок авторских туров:")
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

        if (index < 0 || index >=  selectedActivity.size()) {
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

        if (index < 0 || index >=  selectedActivity.size()) {
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
}

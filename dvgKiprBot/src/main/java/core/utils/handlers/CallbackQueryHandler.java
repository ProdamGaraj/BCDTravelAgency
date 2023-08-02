package core.utils.handlers;

import lombok.SneakyThrows;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

    static final KeyboardService keyboardService = new KeyboardService();
    static final MediaService mediaService = new MediaService();
    static final Map<Long, List<Pair<Integer, String>>> activity_lists = new HashMap<>();

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
            case "personal_tours":
                personalToursChooseHandler(callbackQuery);
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
                .replyMarkup(keyboardService.getActivitiesKeyboard())
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
                .replyMarkup(keyboardService.getActivitiesKeyboard())
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
                .replyMarkup(keyboardService.getResortsKeyboard())
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
                .replyMarkup(keyboardService.getPersonalToursKeyboard())
                .build());
        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

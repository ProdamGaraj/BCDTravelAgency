package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import lombok.SneakyThrows;
import org.jvnet.hk2.annotations.Service;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
    private final AbsSender bot;
    static final KeyboardService keyboardService = new KeyboardService();
    static final MediaService mediaService = new MediaService();
    static final Map<Long, Integer> selectedActivity = new HashMap<>();
    static final Map<Long, List<Pair<Integer, String>>> activity_lists = new HashMap<>();
    private final ActivityRepo activityRepo = new ActivityRepo();
    public ActivityService(DvgKiprBot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void activitiesChooseHandler(CallbackQuery callbackQuery) {
        if (!activity_lists.containsKey(callbackQuery.getFrom().getId())) {
            activity_lists.put(callbackQuery.getFrom().getId(), new ArrayList<>());
        }
        List<Pair<Integer, String>> cur_list = activity_lists.get(callbackQuery.getFrom().getId());
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForActivity(new Activity()))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список ваших активностей:\n" + cur_list.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(0))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void activity_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();
        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= activityRepo.activityList().size()-1) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getActivitiesKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }


    @SneakyThrows
    public void activity_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= activityRepo.activityList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getActivitiesKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    public void activity_select(CallbackQuery callbackQuery) {

    }
}

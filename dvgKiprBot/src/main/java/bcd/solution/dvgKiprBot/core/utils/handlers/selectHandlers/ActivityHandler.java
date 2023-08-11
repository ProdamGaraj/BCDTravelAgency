package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.repository.StateMachineRepo;
import bcd.solution.dvgKiprBot.core.services.ActivityService;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
public class ActivityHandler {
    private final StateMachineService stateMachineService;
    private final ActivityService activityService;
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final ResortHandler resortHandler;

    @Autowired
    public ActivityHandler(StateMachineService stateMachineService,
                           ActivityService activityService,
                           KeyboardService keyboardService,
                           MediaService mediaService,
                           ResortHandler resortHandler) {
        this.activityService = activityService;
        this.keyboardService = keyboardService;
        this.stateMachineService = stateMachineService;
        this.mediaService = mediaService;
        this.resortHandler = resortHandler;
    }
    @Async
    @SneakyThrows
    public void handleActivityCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "activities" -> defaultHandler(callbackQuery, bot);
            case "activities_select" -> selectHandler(callbackQuery, bot);
            case "activities_add" -> addHandler(callbackQuery, bot);
            case "activities_change" -> changeHandler(callbackQuery, bot);
            case "activities_delete" -> deleteHandler(callbackQuery, bot);
        }
    }

    private void deleteHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Parse activity id from callback data and delete activity from user's state
        Long activityId = Long.parseLong(callbackQuery.getData().split("/")[2]);
        Activity deletingActivity = activityService.getById(activityId);

        stateMachineService.removeActivityFromStateByUserId(deletingActivity, callbackQuery.getFrom().getId());
//        Answer callback
        changeHandler(callbackQuery, bot);
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Get user's state for current list of activities
//        and get current activity
        StateMachine stateMachine = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        Activity currentActivity = activityService.getByIndex(0);
//        Build activity card with selected activities
        StringBuilder caption = new StringBuilder(currentActivity.toString() + "\n\nВыбранные активности: ");
        for (Activity chousen_activity : stateMachine.activities) {
            caption.append("- ").append(chousen_activity.name).append("\n");
        }

        boolean isDeleting = false;
        if (!stateMachine.activities.isEmpty()) {
            isDeleting = stateMachine.activities.contains(currentActivity);
        }
//        Call telegram API
//        TODO: add getting media
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getActivityMedia(currentActivity))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(caption.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(0, currentActivity.getId(), isDeleting))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    private void addHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Parse activity id from callback data and add activity to user's state
        Long activity_id = Long.parseLong(callbackQuery.getData().split("/")[2]);
        stateMachineService.addActivityByIdByUserId(callbackQuery.getFrom().getId(), activity_id);
//        Answer callback
        changeHandler(callbackQuery, bot);
    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Parse index of current activity
        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);
//        Get user's state for current list of activities
//        and get current activity
        StateMachine stateMachine = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        Activity currentActivity = activityService.getByIndex(index);
//        Build activity card with selected activities
        StringBuilder caption = new StringBuilder(currentActivity.toString() + "\n\nВыбранные активности:\n");
        for (Activity chousen_activity : stateMachine.activities) {
            caption.append("- ").append(chousen_activity.name).append("\n");
        }
        boolean isDeleting = false;
        if (!stateMachine.activities.isEmpty()) {
            isDeleting = stateMachine.activities.contains(currentActivity);
        }
//        Call telegram API
//        TODO: add getting media
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getActivityMedia(currentActivity))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(caption.toString())
                .replyMarkup(keyboardService.getActivitiesKeyboard(index, currentActivity.getId(), isDeleting))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        resortHandler.defaultHandler(callbackQuery, bot);
    }
}

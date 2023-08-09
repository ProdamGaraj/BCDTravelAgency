package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.*;

@Service
public class ResortService {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final Map<Long, Integer> selectedResort = new HashMap<>();
    private final Map<Long, Integer> selectedActivity = new HashMap<>();
    private final ResortRepo resortRepo;

    @Autowired
    public ResortService(KeyboardService keyboardService,
                         MediaService mediaService,
                         ResortRepo resortRepo) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.resortRepo = resortRepo;
    }

    @Async
    public Resort getResortByIndex(Integer index) {
        return resortRepo.findAll().get(index);
    }

    @SneakyThrows
    public void resortsChooseHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        List<Resort> resorts = new ArrayList<>();
//        resorts.add(new Resort(1L, "PROTARAS", "desc", "geo", "image_src", activityRepo.activityList()));
//        resorts.add(new Resort(1L, "AYIA NAPA", "desc", "geo", "image_src", activityRepo.activityList()));
//        resorts.add(new Resort(1L, "LARNACA", "desc", "geo", "image_src", activityRepo.activityList()));
//        resorts.add(new Resort(1L, "LIMASSOL", "desc", "geo", "image_src", activityRepo.activityList()));


//        bot.executeAsync(EditMessageMedia.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .media(mediaService.updateMediaForResort(new Resort()))
//                .build());
//        bot.executeAsync(EditMessageCaption.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .caption("Здесь будет список курортов:")
//                .replyMarkup(keyboardService.getResortsKeyboard(0))
//                .build());
//        bot.executeAsync(AnswerCallbackQuery.builder()
//                .callbackQueryId(callbackQuery.getId()).build());
    }


    @SneakyThrows
    public void resort_leftHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
//
//        Long ID = callbackQuery.getMessage().getChatId();
//
//        selectedActivity.putIfAbsent(ID, 0);
//        Integer index = selectedActivity.get(ID);
//        index -= 1;
//
//        if (index < 0 || index >= selectedActivity.size()) {
//            index = 0;
//        }//TODO: think about  mod(currentIndex:size)
//
//        bot.executeAsync(EditMessageReplyMarkup.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .replyMarkup(keyboardService.getResortsKeyboard(index))
//                .build());
//        bot.executeAsync(AnswerCallbackQuery.builder()
//                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void resort_rightHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

//        if (index < 0 || index >= resortRepo.resortList().size()) {
//            index = 0;
//        }//TODO: think about  mod(currentIndex:size)

//        bot.executeAsync(EditMessageReplyMarkup.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .replyMarkup(keyboardService.getResortsKeyboard(index))
//                .build());
//        bot.executeAsync(AnswerCallbackQuery.builder()
//                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void resort_select(CallbackQuery callbackQuery, DvgKiprBot bot) {

        //TODO: this code suck ass, so it has to be rewrited! I suppose we
        // need some service wich will always give us needed id, and also we need to add some data
        // to callback data, but not as i did it. This is disgusting! I hate myself.

        String data = callbackQuery.getData();
        Integer id = Integer.parseInt(data.substring(data.lastIndexOf("/")));
        selectedResort.put(0L, id);
        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
//                .text("Вы выбрали " + resortRepo.resortList().get(id).name)
                .text("Вы выбрали ")
                .showAlert(Boolean.TRUE)
                .build());
    }
}

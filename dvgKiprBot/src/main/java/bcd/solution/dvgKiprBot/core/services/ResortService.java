package bcd.solution.dvgKiprBot.core.services;

import lombok.SneakyThrows;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ResortService {
    private final AbsSender bot;
    static final KeyboardService keyboardService = new KeyboardService();
    static final MediaService mediaService = new MediaService();
    static final Map<Long, Integer> selectedResort = new HashMap<>();
    static final Map<Long, Integer> selectedActivity = new HashMap<>();
    private final ResortRepo resortRepo = new ResortRepo();
    public ResortService(AbsSender bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void resortsChooseHandler(CallbackQuery callbackQuery) {
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForResort(new Resort()))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список курортов:")
                .replyMarkup(keyboardService.getResortsKeyboard(0))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void resort_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= selectedActivity.size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)

        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getResortsKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void resort_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= resortRepo.resortList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)

        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getResortsKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void resort_select(CallbackQuery callbackQuery) {

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
                .text("Вы выбрали " + resortRepo.resortList().get(id).name)
                .showAlert(Boolean.TRUE)
                .build());
    }
}

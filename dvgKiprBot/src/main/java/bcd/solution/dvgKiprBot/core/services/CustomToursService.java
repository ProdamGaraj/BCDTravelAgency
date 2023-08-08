package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import lombok.SneakyThrows;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.repository.CustomTourRepo;
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
public class CustomToursService {

    private final AbsSender bot;
    static final KeyboardService keyboardService = new KeyboardService();
    static final MediaService mediaService = new MediaService();
    private final CustomTourRepo customTourRepo = new CustomTourRepo();
    static final Map<Long, Integer> selectedActivity = new HashMap<>();
    public CustomToursService(DvgKiprBot bot) {
        this.bot = bot;
    }


    @SneakyThrows
    public void personalToursChooseHandler(CallbackQuery callbackQuery) {

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForCustomTour(new CustomTour()))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Здесь будет список авторских туров:")
                .replyMarkup(keyboardService.getPersonalToursKeyboard(0))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void personalTour_leftHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

        if (index < 0 || index >= customTourRepo.customTourList().size()) {
            index = 0;
        }//TODO: think about  mod(currentIndex:size)


        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getPersonalToursKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void personalTour_rightHandler(CallbackQuery callbackQuery) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

        if (index < 0 || index >= customTourRepo.customTourList().size()) {
            index = 0;
        }

        bot.executeAsync(EditMessageReplyMarkup.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(keyboardService.getPersonalToursKeyboard(index))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    public void personalTour_select(CallbackQuery callbackQuery) {
    }
}

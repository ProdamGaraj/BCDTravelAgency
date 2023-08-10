package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.ResortService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class ResortHandler {
    private final ResortService resortService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;

    public ResortHandler(ResortService resortService,
                         MediaService mediaService,
                         KeyboardService keyboardService) {
        this.resortService = resortService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
    }

    @Async
    @SneakyThrows
    public void handleResortCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Look comments in activity handler class
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "resorts" -> defaultHandler(callbackQuery, bot);
            case "resorts_select" -> selectHandler(callbackQuery, bot);
            case "resorts_change" -> changeHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Resort currentResort = resortService.getByIndex(0);
//        TODO: add getting media of resort
//        bot.executeAsync(EditMessageMedia.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .media(mediaService.updateMediaForResort(new Resort()))
//                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentResort.toString())
                .replyMarkup(keyboardService.getResortsKeyboard(0, currentResort.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);
        Resort currentResort = resortService.getByIndex(index);
//        TODO: add getting media of resort
//        bot.executeAsync(EditMessageMedia.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .media(mediaService.updateMediaForResort(new Resort()))
//                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentResort.toString())
                .replyMarkup(keyboardService.getResortsKeyboard(index, currentResort.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

}

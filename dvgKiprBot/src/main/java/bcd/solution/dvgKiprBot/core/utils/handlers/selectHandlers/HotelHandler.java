package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.services.HotelService;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class HotelHandler {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final HotelService hotelService;

    public HotelHandler(KeyboardService keyboardService,
                        MediaService mediaService,
                        HotelService hotelService) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.hotelService = hotelService;
    }
    @Async
    @SneakyThrows
    public void handleHotelCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Look comments in activity handler class
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "hotels_select" -> selectHandler(callbackQuery, bot);
            case "hotels_change" -> changeHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Hotel currentHotel = hotelService.getByIndex(0);

//        bot.executeAsync(EditMessageMedia.builder()
//            .chatId(callbackQuery.getMessage().getChatId())
//            .messageId(callbackQuery.getMessage().getMessageId())
//            .media(mediaService.updateMediaForCustomTour(new CustomTour()))
//            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentHotel.toString())
                .replyMarkup(keyboardService.getHotelsKeyboard(0, currentHotel.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);

        Hotel currentHotel = hotelService.getByIndex(index);

//        bot.executeAsync(EditMessageMedia.builder()
//            .chatId(callbackQuery.getMessage().getChatId())
//            .messageId(callbackQuery.getMessage().getMessageId())
//            .media(mediaService.updateMediaForCustomTour(new CustomTour()))
//            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentHotel.toString())
                .replyMarkup(keyboardService.getHotelsKeyboard(index, currentHotel.getId()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @Async
    @SneakyThrows
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
    }
}

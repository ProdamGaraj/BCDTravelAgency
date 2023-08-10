package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.services.HotelService;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Component
public class HotelHandler {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final HotelService hotelService;
    private final StateMachineService stateMachineService;

    public HotelHandler(KeyboardService keyboardService,
                        MediaService mediaService,
                        HotelService hotelService,
                        StateMachineService stateMachineService) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.hotelService = hotelService;
        this.stateMachineService = stateMachineService;
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
        StateMachine usersState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());

        List<Hotel> currentHotels = hotelService.findByResort(usersState.resort);

//        bot.executeAsync(EditMessageMedia.builder()
//            .chatId(callbackQuery.getMessage().getChatId())
//            .messageId(callbackQuery.getMessage().getMessageId())
//            .media(mediaService.updateMediaForCustomTour(new CustomTour()))
//            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentHotels.get(0).toString())
                .replyMarkup(keyboardService.getHotelsKeyboard(
                        0,
                        currentHotels.get(0).getId(),
                        currentHotels.size()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);
        StateMachine usersState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());

        List<Hotel> currentHotels = hotelService.findByResort(usersState.resort);

//        bot.executeAsync(EditMessageMedia.builder()
//            .chatId(callbackQuery.getMessage().getChatId())
//            .messageId(callbackQuery.getMessage().getMessageId())
//            .media(mediaService.updateMediaForCustomTour(new CustomTour()))
//            .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentHotels.get(index).toString())
                .replyMarkup(keyboardService.getHotelsKeyboard(
                        index,
                        currentHotels.get(index).getId(),
                        currentHotels.size()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());

    }

    @Async
    @SneakyThrows
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
    }
}

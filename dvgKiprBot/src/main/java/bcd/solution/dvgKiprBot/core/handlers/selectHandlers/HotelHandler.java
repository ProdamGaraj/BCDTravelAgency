package bcd.solution.dvgKiprBot.core.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.HotelService;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class HotelHandler {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final HotelService hotelService;
    private final StateMachineService stateMachineService;
    private final FeedbackHandler feedbackHandler;

    public HotelHandler(KeyboardService keyboardService,
                        MediaService mediaService,
                        HotelService hotelService,
                        StateMachineService stateMachineService,
                        FeedbackHandler feedbackHandler) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.hotelService = hotelService;
        this.stateMachineService = stateMachineService;
        this.feedbackHandler = feedbackHandler;
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
        if (currentHotels.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Отелей не найдено, попробуйте позже")
                    .build());
            return;
        }
//        bot.executeAsync(DeleteMessage.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .build());
//        bot.executeAsync(SendMediaGroup.builder()
//                        .chatId(callbackQuery.getFrom().getId())
//                        .medias(mediaService.getHotelMedias(currentHotels.get(0)))
//                .build());


        bot.executeAsync(EditMessageMedia.builder()
            .chatId(callbackQuery.getMessage().getChatId())
            .messageId(callbackQuery.getMessage().getMessageId())
            .media(mediaService.getHotelMedia(currentHotels.get(0)))
            .build());
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
    protected void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        int index = Integer.parseInt(callbackQuery.getData().split("/")[1]);
        StateMachine usersState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());

        List<Hotel> currentHotels = hotelService.findByResort(usersState.resort);


//        bot.executeAsync(DeleteMessage.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .build());
//        bot.executeAsync(SendMediaGroup.builder()
//                .chatId(callbackQuery.getFrom().getId())
//                .medias(mediaService.getHotelMedias(currentHotels.get(0)))
//                .build());

        bot.executeAsync(EditMessageMedia.builder()
            .chatId(callbackQuery.getMessage().getChatId())
            .messageId(callbackQuery.getMessage().getMessageId())
            .media(mediaService.getHotelMedia(currentHotels.get(index)))
            .build());
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
    protected void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Long hotelId = Long.parseLong(callbackQuery.getData().split("/")[1]);

        Optional<Hotel> selectedHotel = hotelService.getById(hotelId);
        if (selectedHotel.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Отель не найден, попробуйте позже")
                    .build());
            return;
        }

        stateMachineService.setHotelByUserId(selectedHotel.get(), callbackQuery.getFrom().getId());

        feedbackHandler.feedbackHandler(callbackQuery, bot);

    }
}

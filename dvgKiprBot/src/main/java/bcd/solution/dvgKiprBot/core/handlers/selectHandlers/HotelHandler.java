package bcd.solution.dvgKiprBot.core.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.handlers.FeedbackHandler;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.Stars;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.*;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;

import java.util.List;
import java.util.Optional;

@Component
public class HotelHandler {
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final HotelService hotelService;
    private final StateMachineService stateMachineService;
    private final CardService cardService;
    private final FeedbackHandler feedbackHandler;

    public HotelHandler(KeyboardService keyboardService,
                        MediaService mediaService,
                        HotelService hotelService,
                        StateMachineService stateMachineService,
                        CardService cardService,
                        FeedbackHandler feedbackHandler) {
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.hotelService = hotelService;
        this.stateMachineService = stateMachineService;
        this.cardService = cardService;
        this.feedbackHandler = feedbackHandler;
    }

    @Async
    @SneakyThrows
    public void handleHotelCallback(CallbackQuery callbackQuery, DvgKiprBot bot) {
//        Look comments in activity handler class
        String action = callbackQuery.getData().split("/")[0];
        switch (action) {
            case "hotels" -> defaultHandler(callbackQuery, bot);
            case "hotels_stars" -> starsHandler(callbackQuery, bot);
            case "hotels_select" -> selectHandler(callbackQuery, bot);
            case "hotels_change" -> changeHandler(callbackQuery, bot);
            case "hotels_media" -> mediaHandler(callbackQuery, bot);
        }
    }


    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption("Выберите звёздность отеля")
                .replyMarkup(keyboardService.getHotelsStarsKeyboard())
                .build());
    }


    @Async
    @SneakyThrows
    public void starsHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        Stars stars = Stars.valueOf(callbackQuery.getData().split("/")[1]);

        StateMachine usersState = stateMachineService.setStarsByUserId(callbackQuery.getFrom().getId(), stars);


        List<Hotel> currentHotels = hotelService.findByResortAndStars(usersState.resort, stars);

        if (currentHotels.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Отелей не найдено, попробуйте позже")
                    .build());
            return;
        }

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getHotelMedia(currentHotels.get(0)))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(currentHotels.get(0).toString())
                .parseMode(ParseMode.MARKDOWN)
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
    protected void mediaHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        String[] dataArray = callbackQuery.getData().split("/");
        int index = Integer.parseInt(dataArray[1]);
        Long hotelId = Long.parseLong(dataArray[2]);

        StateMachine stateMachine = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Hotel> currentHotels = hotelService.findByResort(stateMachine.resort);
        List<List<InputMedia>> allMedias;
        try {
            allMedias = mediaService.getHotelMedias(currentHotels.get(index));
        } catch (Exception e) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .text("Дополнительных фото нет")
                    .showAlert(true)
                    .build());
            return;
        }

        if (allMedias.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .text("Дополнительных фото нет")
                    .showAlert(true)
                    .build());
            return;
        }

        for (List<InputMedia> medias : allMedias) {
            if (medias.size() > 1) {
                bot.executeAsync(SendMediaGroup.builder()
                        .chatId(callbackQuery.getFrom().getId())
                        .medias(medias)
                        .build());
            } else {
                InputFile file = new InputFile(
                        medias.get(0).getNewMediaStream(),
                        medias.get(0).getMediaName());
                bot.executeAsync(SendPhoto.builder()
                        .chatId(callbackQuery.getFrom().getId())
                        .photo(file)
                        .build());
            }
        }

        bot.executeAsync(DeleteMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .build());

        bot.executeAsync(SendPhoto.builder()
                .chatId(callbackQuery.getFrom().getId())
                .photo(mediaService.getHotelFile(currentHotels.get(index)))
                .caption(currentHotels.get(index).toString())
                .replyMarkup(keyboardService.getHotelsKeyboard(index, hotelId, currentHotels.size()))
                .build());

        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }


    @Async
    @SneakyThrows
    protected void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        int index = Integer.parseInt(callbackQuery.getData().split("/")[1]);
        StateMachine usersState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());

        List<Hotel> currentHotels = hotelService.findByResortAndStars(
                usersState.resort,
                usersState.stars);


        if (currentHotels.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Отелей не найдено, попробуйте позже")
                    .build());
            return;
        }

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getHotelMedia(currentHotels.get(index)))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(cardService.getHotelCard(currentHotels.get(index)))
                .parseMode(ParseMode.MARKDOWN)
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

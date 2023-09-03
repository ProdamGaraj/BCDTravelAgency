package bcd.solution.dvgKiprBot.core.handlers.extensionsHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.*;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class FavoritesHandler {
    private final Logger logger;
    private final StateMachineService stateMachineService;
    private final CardService cardService;
    private final KeyboardService keyboardService;
    private final MediaService mediaService;
    private final HotelService hotelService;


    public FavoritesHandler(
            Logger logger,
            StateMachineService stateMachineService,
            CardService activityService,
            KeyboardService keyboardService,
            MediaService mediaService,
            HotelService hotelService)
    {
        this.logger = logger;
        this.stateMachineService = stateMachineService;
        this.cardService = activityService;
        this.keyboardService = keyboardService;
        this.mediaService = mediaService;
        this.hotelService = hotelService;
    }

    @Async
    public void handleAddToFavorites(CallbackQuery callbackQuery, DvgKiprBot bot){

        String[] dataArray = callbackQuery.getData().split("/");
        int index = Integer.parseInt(dataArray[1]);
        Long hotelId = Long.parseLong(dataArray[2]);

        StateMachine stateMachine = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        Hotel currentHotel = hotelService.getById(hotelId).get();
        String description = cardService.getHotelCard(currentHotel, true);

        List<List<InputMedia>> allMedias;
        try {
            allMedias = mediaService.getHotelMedias(currentHotel);
        } catch (Exception e) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .text("Дополнительных фото нет")
                    .showAlert(true)
                    .build());
            return;
        }

        if (allMedias.isEmpty()) {
            try {
                bot.executeAsync(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Дополнительных фото нет")
                        .showAlert(true)
                        .build());
            } catch (TelegramApiException e) {
                log
            }
            return;
        }

        bot.executeAsync(
                SendMessage.builder()
                        .chatId(callbackQuery.getFrom().getId())
                        .text("_Отель_ "+currentHotel.get(index).name+" "+currentHotel.get(index).stars)
                        .parseMode(ParseMode.MARKDOWN)
                        .build()
        );
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
                .photo(mediaService.getHotelFile(currentHotel.get(index)))
                .caption(cardService.getHotelCard(currentHotel.get(index), false))
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(keyboardService.getHotelsKeyboard(index, hotelId, currentHotel.size()))
                .build());

        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }
}
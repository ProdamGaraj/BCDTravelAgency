package bcd.solution.dvgKiprBot.core.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.ResortService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
public class ResortHandler {
    private final ResortService resortService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;
    private final StateMachineService stateMachineService;
    private final HotelHandler hotelHandler;

    public ResortHandler(ResortService resortService,
                         MediaService mediaService,
                         KeyboardService keyboardService,
                         StateMachineService stateMachineService,
                         HotelHandler hotelHandler) {
        this.resortService = resortService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
        this.stateMachineService = stateMachineService;
        this.hotelHandler = hotelHandler;
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
            case "resorts_media" -> mediaHandler(callbackQuery, bot);
        }
    }

    @Async
    @SneakyThrows
    protected void mediaHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        String[] dataArray = callbackQuery.getData().split("/");
        int index = Integer.parseInt(dataArray[1]);
        Long hotelId = Long.parseLong(dataArray[2]);

        StateMachine stateMachine = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Resort> currentResorts = resortService.getByIndexAndActivities(index, stateMachine.activities);
        List<List<InputMedia>> allMedias;
        try {
            allMedias = mediaService.getResortMedias(currentResorts.get(index));
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
                .photo(mediaService.getResortFile(currentResorts.get(index)))
                .caption(currentResorts.get(index).toString())
                .replyMarkup(keyboardService.getResortsKeyboard(index, hotelId, currentResorts.size()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId())
                .build());
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        StateMachine userState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Resort> currentResorts = resortService.getByIndexAndActivities(0,
                userState.activities.stream().toList());
//        TODO: add getting media of resort
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getResortMedia(currentResorts.get(0)))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(resortService.toString(currentResorts.get(0)))
                .replyMarkup(keyboardService.getResortsKeyboard(0,
                        currentResorts.get(0).getId(),
                        currentResorts.size()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @Async
    @SneakyThrows
    protected void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Long resortId = Long.parseLong(callbackQuery.getData().split("/")[1]);

        Optional<Resort> selectedResort = resortService.getById(resortId);
        if (selectedResort.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                            .callbackQueryId(callbackQuery.getId())
                            .showAlert(true).text("Курорт не найден, попробуйте позже")
                    .build());
            return;
        }

        stateMachineService.setResortByUserId(selectedResort.get(), callbackQuery.getFrom().getId());

        hotelHandler.starsHandler(callbackQuery, bot);
    }

    @Async
    @SneakyThrows
    protected void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        int index = Integer.parseInt(callbackQuery.getData().split("/")[1]);

        StateMachine userState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Resort> currentResorts = resortService.getByIndexAndActivities(index,
                userState.activities.stream().toList());
        if (currentResorts.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Курортов не найдено, попробуйте позже")
                    .build());
            return;
        }
//        TODO: add getting media of resort
        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.getResortMedia(currentResorts.get(index)))
                .build());
        bot.executeAsync(EditMessageCaption.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .caption(resortService.toString(currentResorts.get(index)))
                .replyMarkup(keyboardService.getResortsKeyboard(index,
                        currentResorts.get(index).getId(),
                        currentResorts.size()))
                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

}

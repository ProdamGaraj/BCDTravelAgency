package bcd.solution.dvgKiprBot.core.utils.handlers.selectHandlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.Resort;
import bcd.solution.dvgKiprBot.core.models.StateMachine;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.ResortService;
import bcd.solution.dvgKiprBot.core.services.StateMachineService;
import bcd.solution.dvgKiprBot.core.utils.handlers.CallbackQueryHandler;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

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
        }
    }

    @Async
    @SneakyThrows
    public void defaultHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        StateMachine userState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Resort> currentResorts = resortService.getByIndexAndActivities(0, userState.activities);
//        TODO: add getting media of resort
//        bot.executeAsync(EditMessageMedia.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .media(mediaService.updateMediaForResort(new Resort()))
//                .build());
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
    private void selectHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
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

        hotelHandler.defaultHandler(callbackQuery, bot);
    }

    @Async
    @SneakyThrows
    private void changeHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {
        Integer index = Integer.parseInt(callbackQuery.getData().split("/")[1]);

        StateMachine userState = stateMachineService.getByUserId(callbackQuery.getFrom().getId());
        List<Resort> currentResorts = resortService.getByIndexAndActivities(index, userState.activities);
        if (currentResorts.isEmpty()) {
            bot.executeAsync(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.getId())
                    .showAlert(true).text("Курортов не найдено, попробуйте позже")
                    .build());
            return;
        }
//        TODO: add getting media of resort
//        bot.executeAsync(EditMessageMedia.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .media(mediaService.updateMediaForResort(new Resort()))
//                .build());
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

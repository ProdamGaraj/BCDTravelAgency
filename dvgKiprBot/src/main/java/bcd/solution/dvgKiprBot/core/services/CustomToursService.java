package bcd.solution.dvgKiprBot.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.repository.CustomTourRepo;
import org.springframework.stereotype.Service;

@Service
public class CustomToursService {
    private final CustomTourRepo customTourRepo;
    @Autowired
    public CustomToursService(CustomTourRepo customTourRepo) {
        this.customTourRepo = customTourRepo;
    }


    @SneakyThrows
    public void personalToursChooseHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        bot.executeAsync(EditMessageMedia.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .media(mediaService.updateMediaForCustomTour(new CustomTour()))
                .build());
//        bot.executeAsync(EditMessageCaption.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .caption("Здесь будет список авторских туров:")
//                .replyMarkup(keyboardService.getCustomToursKeyboard(0))
//                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void personalTour_leftHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index -= 1;

//        if (index < 0 || index >= customTourRepo.customTourList().size()) {
//            index = 0;
//        }//TODO: think about  mod(currentIndex:size)


//        bot.executeAsync(EditMessageReplyMarkup.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .replyMarkup(keyboardService.getCustomToursKeyboard(index))
//                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    public void personalTour_rightHandler(CallbackQuery callbackQuery, DvgKiprBot bot) {

        Long ID = callbackQuery.getMessage().getChatId();

        selectedActivity.putIfAbsent(ID, 0);
        Integer index = selectedActivity.get(ID);
        index += 1;

//        if (index < 0 || index >= customTourRepo.customTourList().size()) {
//            index = 0;
//        }

//        bot.executeAsync(EditMessageReplyMarkup.builder()
//                .chatId(callbackQuery.getMessage().getChatId())
//                .messageId(callbackQuery.getMessage().getMessageId())
//                .replyMarkup(keyboardService.getCustomToursKeyboard(index))
//                .build());
        bot.executeAsync(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

package сore.utils.handlers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import сore.services.KeyboardService;

public class CallbackQueryHandler {
    private final AbsSender bot;
    static final KeyboardService keyboardService = new KeyboardService();

    public CallbackQueryHandler(AbsSender bot) {
        this.bot = bot;
    }

    @SneakyThrows
    public void handleQuery(CallbackQuery callbackQuery) {
        String callback_data = callbackQuery.getData();
        switch (callback_data) {
            case "restart":
                restartHandler(callbackQuery);
                break;
            case "activities":
                activitiesChooseHandler(callbackQuery);
                break;
            case "resorts":
                resortsChooseHandler(callbackQuery);
                break;
            case "personal_tours":
                personalToursChooseHandler(callbackQuery);
                break;
            default:
                bot.execute(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .text("Что-то пошло не так")
                        .showAlert(Boolean.TRUE)
                        .build());
                break;
        }
    }

    @SneakyThrows
    private void restartHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());

        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void activitiesChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text("Cписок типов активностей:")
                .build());

        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void resortsChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text("Cписок курортов:")
                .replyMarkup(keyboardService.getResortsKeyboard())
                .build());

        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }

    @SneakyThrows
    private void personalToursChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text("Cписок авторских туров:")
                .replyMarkup(keyboardService.getPersonalToursKeyboard())
                .build());

        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

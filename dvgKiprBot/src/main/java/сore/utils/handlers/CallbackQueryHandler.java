package сore.utils.handlers;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class CallbackQueryHandler {
    private final AbsSender bot;

    public CallbackQueryHandler(AbsSender bot) {
        this.bot = bot;
    }

    public void handleQuery(CallbackQuery callbackQuery) {
        String callback_data = callbackQuery.getData();
        switch (callback_data) {
            case "activities":
                activitiesChooseHandler(callbackQuery);
                break;
            case "resorts":

                break;
            case "personal_tours":

                break;
            default:

                break;
        }
    }

    @SneakyThrows
    private void activitiesChooseHandler(CallbackQuery callbackQuery) {
        bot.execute(EditMessageText.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .text("Cписок активностей:")
                .build());

        bot.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.getId()).build());
    }
}

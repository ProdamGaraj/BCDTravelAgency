package сore.utils.handlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class CallbackQueryHandler {

    private final AbsSender bot;

    public CallbackQueryHandler(AbsSender bot) {
        this.bot = bot;
    }

    public void handleQuery(CallbackQuery callbackQuery) {

    }
}

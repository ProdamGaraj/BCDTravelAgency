package сore;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import сore.models.StateMachine;
import сore.services.ActivityService;
import сore.services.CustomToursService;
import сore.services.ResortService;
import сore.utils.handlers.CallbackQueryHandler;
import сore.utils.handlers.MessageHandler;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Optional;

@Log4j
@Singleton
@SpringBootApplication
public class DvgKiprBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    private final CustomToursService customToursService;
    private final ResortService resortService;
    private final ActivityService activityService;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public DvgKiprBot() {
        this.messageHandler = new MessageHandler(this);
        this.callbackQueryHandler = new CallbackQueryHandler(this);
        this.customToursService = new CustomToursService();
        this.resortService = new ResortService();
        this.activityService = new ActivityService();
    }


    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage() != null) {
            Message message = update.getMessage();

            log.debug(message.getText());
            messageHandler.handleMessage(message);
            /*utilClass.logMessage(message);*/ //TODO: Надо пофиксить, утил класс - нулл, не понимаю как заинжектить
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery() != null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            callbackQueryHandler.handleQuery(callbackQuery);
        }
    }

    @Override
    public String getBotUsername() {
        return "dvgKiprbot";
    }

    @Override
    public String getBotToken() {
        return "6460979142:AAHV4_8sKXuKmWwCZUBCBt7Rln_ZyYKxh9Y";
    }


    @SneakyThrows
    public static void main(String[] args) {
        DvgKiprBot bot = new DvgKiprBot();
        bot.botName="dvgKiprbot";
        bot.botToken="6460979142:AAHV4_8sKXuKmWwCZUBCBt7Rln_ZyYKxh9Y";
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}

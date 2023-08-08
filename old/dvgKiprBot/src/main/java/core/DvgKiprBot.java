package core;

import core.services.ActivityService;
import core.services.CustomToursService;
import core.services.HotelService;
import core.services.ResortService;
import core.utils.handlers.CallbackQUeryHandler;
import core.utils.handlers.CallbackQueryHandler;
import core.utils.handlers.MessageHandler;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.inject.Singleton;

@Log4j
@Singleton
@SpringBootApplication
public class DvgKiprBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    public final ActivityService activityService;
    public final CustomToursService customToursService;
    public final HotelService hotelService;
    public final ResortService resortService;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public DvgKiprBot() {
        this.messageHandler = new MessageHandler(this);
        this.callbackQueryHandler = new CallbackQueryHandler(this);
        this.activityService = new ActivityService(this);
        this.customToursService = new CustomToursService(this);
        this.hotelService = new HotelService(this);
        this.resortService = new ResortService(this);
    }


    @Override
    public void onUpdateReceived(Update update) {

        //TODO check some issues with locks

        if (update.hasMessage() && update.getMessage() != null) {
            Message message = update.getMessage();
            log.debug(message.getText());

            // Create a new thread to handle the message asynchronously
            Thread messageHandlerThread = new Thread(() -> {
                messageHandler.handleMessage(message);
            });
            messageHandlerThread.start();
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery() != null) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            // Create a new thread to handle the callback query asynchronously
            Thread callbackQueryHandlerThread = new Thread(() -> {
                callbackQueryHandler.handleQuery(callbackQuery);
            });
            callbackQueryHandlerThread.start();
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
//        SpringApplication.run(DvgKiprBot.class, args);
    }
}

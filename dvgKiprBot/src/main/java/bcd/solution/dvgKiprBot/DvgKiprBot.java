package bcd.solution.dvgKiprBot;

import bcd.solution.dvgKiprBot.core.utils.handlers.CallbackQueryHandler;
import bcd.solution.dvgKiprBot.core.utils.handlers.MessageHandler;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Log4j
@SpringBootApplication
public class DvgKiprBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public DvgKiprBot() {
        this.messageHandler = new MessageHandler(this);
        this.callbackQueryHandler = new CallbackQueryHandler(this);
    }


    @Override
    public void onUpdateReceived(Update update) {

        //TODO check some issues with locks

        if (update.hasMessage() && update.getMessage() != null) {
            Message message = update.getMessage();
            //log.debug(message.getText());

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
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @SneakyThrows
    public static void main(String[] args){
        SpringApplication.run(DvgKiprBot.class, args);
        DvgKiprBot bot = new DvgKiprBot();
        bot.botName="dvgKiprbot";
        bot.botToken="6495757627:AAHfu3cyhYU9KAky8DN96EDToHPsw28AIE4";
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}

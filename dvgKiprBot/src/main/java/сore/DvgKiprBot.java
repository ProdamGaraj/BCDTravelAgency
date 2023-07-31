package сore;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import сore.models.StateMachine;
import сore.services.ActivityService;
import сore.services.CustomToursService;
import сore.services.ResortService;

import java.util.ArrayList;
import java.util.Optional;

@Log4j
public class DvgKiprBot extends TelegramLongPollingBot {

    private final CustomToursService customToursService;
    private final ResortService resortService;
    private final ActivityService activityService;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    public DvgKiprBot() {
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
            /*utilClass.logMessage(message);*/ //TODO: Надо пофиксить, утил класс - нулл, не понимаю как заинжектить
            handleMessage(message);
        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        //handling commands
        StateMachine stateMachine = new StateMachine();

        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/autorization":
                        execute(SendMessage.builder()
                                .text("please enter your password")
                                .chatId(message.getChatId())
                                .build());
                        //TODO: add atorization, saving progress
                        break;
                    case "/customtours":
                        execute(SendMessage.builder()
                                .text("This our best tours for u")
                                .chatId(message.getChatId())
                                .build());
                        ArrayList<Message> response = new ArrayList<>();
                        for (Integer i = 0; i < 5; i++) {
                            //MOCK data
                            Message m = new Message(){};
                            m.setText(i.toString());
                            m.setChat(message.getChat());
                            response.add(m);
                        }
                         response.forEach(item -> {
                            try {
                                execute(SendMessage.builder()
                                        .text(item.getText())
                                        .chatId(item.getChatId())
                                        .build());
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        //TODO: output list of customtours, saving progress

                        break;
                    case "/resorts":
                        execute(SendMessage.builder()
                                .text("This our kurorts for u")
                                .chatId(message.getChatId())
                                .build());
                        resortService.get_all_resorts();
                        //TODO: output list of kourorts, saving progress
                        break;
                    case "/activities":
                        execute(SendMessage.builder()
                                .text("This our activities for u")
                                .chatId(message.getChatId())
                                .build());
                        //TODO: output list of activities, saving progress
                        break;
                    default:
                        execute(SendMessage.builder()
                                .text("/activities \n" +
                                        "/resorts \n" +
                                        "/customtours \n" +
                                        "/autorization \n")
                                .chatId(message.getChatId())
                                .build());
                        break;
                }
            }
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

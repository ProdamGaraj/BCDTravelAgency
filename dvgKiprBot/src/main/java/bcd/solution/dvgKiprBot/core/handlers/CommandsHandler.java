package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.api.TelegramDataDTO;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;


@Component
public class CommandsHandler {
    private final UserService userService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;
    public final String inviteString = """
            Кипр - это островное государство в Средиземном море,расположенное на перекрестке Европы, Азии и Африки. Он является отличным туристическим направлением благодаря своим красивым пляжам, теплому климату и богатой истории. Кипр имеет богатое культурное наследие, которое отражается в его архитектуре, музеях и археологических раскопках. Столицей Кипра является Никосия, где можно посетить множество достопримечательностей, таких как Кипрский музей, Собор Святого Иоанна и Кипрский национальный парк. Кипр также славится своими винами и кухней, в которой сочетаются греческие, турецкие и английские влияния. В целом, Кипр - это прекрасное место для отдыха и изучения культуры, истории и кухни Средиземноморья.

            Доступные комманды:
            /start""";

    @Value("${backend.baseurl}")
    public String backendUrl;

    public CommandsHandler(UserService userService,
                           MediaService mediaService,
                           KeyboardService keyboardService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;

    }

    @Async
    @SneakyThrows
    public void choosingMessageSender(Long chatId, DvgKiprBot bot, boolean hasPhone, boolean isAuthorized) {
        bot.executeAsync(SendPhoto.builder()
                .chatId(chatId)
                .photo(mediaService.getStartMessageMedia())
                .caption(inviteString)
                .replyMarkup(keyboardService.getTourChoosingKeyboard(hasPhone, isAuthorized))
                .build());
    }

    public boolean sendTelegramData(String username, Long userId) {
        RestTemplate template = new RestTemplate();

        String url = backendUrl + "/profile/telegram/connect/set_id";
        TelegramDataDTO data = new TelegramDataDTO(username, userId.toString());
        ResponseEntity<String> response = template.postForEntity(url, data, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    @Async
    @SneakyThrows
    public void startHandler(DvgKiprBot bot, Long userId, Long chatId, String message) {
        if (message.split(" ").length == 2) {
            if (sendTelegramData(message.split(" ")[1], userId)) {
                bot.executeAsync(SendMessage.builder()
                        .chatId(userId)
                        .text("Телеграм успешно прикреплен")
                        .build());
            }
        }

        if (!userService.hasPhoneById(userId)) {
            bot.executeAsync(SendPhoto.builder()
                    .chatId(chatId)
                    .photo(mediaService.getStartMessageMedia())
//                .caption("Для доступа к полному функционалу бота необходимо указать номер телефона."
//                        + " Но Вы все равно можете выбрать один из авторских туров"
//                )
                    .caption("Для повышения качесва обслуживания нам неоходим Ваш номер телефона")
                    .replyMarkup(keyboardService.getStarterKeyboard())
                    .build());
            return;
        }
        choosingMessageSender(
                chatId,
                bot, true,
                userService.isAuthorized(userId));
    }
}

package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.UserService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandsHandler {
    private final UserService userService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;

    public CommandsHandler(UserService userService,
                           MediaService mediaService,
                           KeyboardService keyboardService) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
    }

    @Async
    @SneakyThrows
    public void startHandler(Message message, DvgKiprBot bot) {
        userService.addUserIfNotExists(
                message.getFrom().getId(),
                message.getFrom().getUserName());
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getStartMessageMedia())
                .caption("Кипр - это островное государство в Средиземном море," +
                        "расположенное на перекрестке Европы, Азии и Африки. " +
                        "Он является отличным туристическим направлением благодаря своим красивым пляжам, теплому климату и богатой истории." +
                        "Кипр имеет богатое культурное наследие, которое отражается в его архитектуре, музеях и археологических раскопках." +
                        "Столицей Кипра является Никосия, где можно посетить множество достопримечательностей, таких как Кипрский музей, " +
                        "Собор Святого Иоанна и Кипрский национальный парк. Кипр также славится своими винами и кухней, в которой сочетаются греческие," +
                        " турецкие и английские влияния. В целом, Кипр - это прекрасное место для отдыха и изучения культуры, истории и кухни Средиземноморья.\n")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @Async
    @SneakyThrows
    public void tourChoosingHandler(Message message, DvgKiprBot bot) {

    }

    @Async
    @SneakyThrows
    public void mediaHandler(Message message, DvgKiprBot bot) {

    }
}
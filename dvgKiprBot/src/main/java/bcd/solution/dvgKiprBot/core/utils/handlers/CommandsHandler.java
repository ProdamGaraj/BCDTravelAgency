package bcd.solution.dvgKiprBot.core.utils.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.*;
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
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getMediaForCustomTour(new CustomTour()))
                .caption("Выберите, от чего хотите отталкиваться при выборе тура")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @Async
    @SneakyThrows
    public void mediaHandler(Message message, DvgKiprBot bot) {

//        List<Activity> all_activities = activityRepo.findAll();
//        resortRepo.save(
//                Resort.builder()
//                        .name("PROTARAS")
//                        .activities(all_activities)
//                        .media("pathToFile")
//                .build());
//        resortRepo.save(
//                Resort.builder()
//                        .name("AYIA NAPA")
//                        .activities(all_activities)
//                        .media("pathToFile")
//                .build());
//        resortRepo.save(
//                Resort.builder()
//                        .name("LARNACA")
//                        .activities(all_activities)
//                        .media("pathToFile")
//                .build());
//        resortRepo.save(
//                Resort.builder()
//                        .name("LIMASSOL")
//                        .activities(all_activities)
//                        .media("pathToFile")
//                .build());
//        customTourRepo.save(CustomTour.builder()
//                .name("Вокруг Кипра за 7 дней")
//                .description("Вокруг Кипра за 7 дней")
//                .build());
//        customTourRepo.save(CustomTour.builder()
//                .name("Пляж и развлечения")
//                .description("Только отдых и водные развлечения на протяжении 7 дней!")
//                .build());
//        customTourRepo.save(CustomTour.builder()
//                .name("Турецкая и Греческая культуры за один тур")
//                .description("Вы посетите обе половины Кипра и узнаете, " +
//                        "как столь разные народы уживаются на одном острове!")
//                .build());


    }
}

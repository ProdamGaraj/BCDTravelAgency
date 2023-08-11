package bcd.solution.dvgKiprBot.core.handlers;

import bcd.solution.dvgKiprBot.DvgKiprBot;
import bcd.solution.dvgKiprBot.core.models.*;
import bcd.solution.dvgKiprBot.core.repository.ActivityRepo;
import bcd.solution.dvgKiprBot.core.repository.HotelFeatureRepo;
import bcd.solution.dvgKiprBot.core.repository.HotelRepo;
import bcd.solution.dvgKiprBot.core.repository.ResortRepo;
import bcd.solution.dvgKiprBot.core.services.KeyboardService;
import bcd.solution.dvgKiprBot.core.services.MediaService;
import bcd.solution.dvgKiprBot.core.services.ResortService;
import bcd.solution.dvgKiprBot.core.services.UserService;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CommandsHandler {
    private final UserService userService;
    private final MediaService mediaService;
    private final KeyboardService keyboardService;
    private final HotelFeatureRepo hotelFeatureRepo;
    private final ResortService resortService;
    private final HotelRepo hotelRepo;
    private final ResortRepo resortRepo;
    private final ActivityRepo activityRepo;

    public CommandsHandler(UserService userService,
                           MediaService mediaService,
                           KeyboardService keyboardService,
                           HotelFeatureRepo hotelFeatureRepo,
                           ResortService resortService,
                           HotelRepo hotelRepo,
                           ResortRepo resortRepo,
                           ActivityRepo activityRepo) {
        this.userService = userService;
        this.mediaService = mediaService;
        this.keyboardService = keyboardService;
        this.hotelFeatureRepo = hotelFeatureRepo;
        this.resortService = resortService;
        this.hotelRepo = hotelRepo;
        this.resortRepo = resortRepo;
        this.activityRepo = activityRepo;

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
                        "Он является отличным туристическим направлением благодаря " +
                        "своим красивым пляжам, теплому климату и богатой истории." +
                        "Кипр имеет богатое культурное наследие, которое отражается " +
                        "в его архитектуре, музеях и археологических раскопках." +
                        "Столицей Кипра является Никосия, где можно посетить множество " +
                        "достопримечательностей, таких как Кипрский музей, " +
                        "Собор Святого Иоанна и Кипрский национальный парк. " +
                        "Кипр также славится своими винами и кухней, в которой сочетаются греческие," +
                        " турецкие и английские влияния. В целом, Кипр - это " +
                        "прекрасное место для отдыха и изучения культуры, " +
                        "истории и кухни Средиземноморья.\n\n" +
                        "Доступные комманды:\n" +
                        "/start\n" +
                        "/customtours - подбор тура\n" +
                        "/authorization - вход для партнеров")
                .replyMarkup(keyboardService.getTourChoosingKeyboard())
                .build());
    }

    @Async
    @SneakyThrows
    public void tourChoosingHandler(Message message, DvgKiprBot bot) {
        bot.executeAsync(SendPhoto.builder()
                .chatId(message.getChatId())
                .photo(mediaService.getTourChoosingMedia())
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
//        Set<HotelFeature> hotel_features = new HashSet<>();
//        hotel_features.add(new HotelFeature("бассейн", "открытый ", true));
//        hotel_features.add(new HotelFeature("бассейн", "крытый ", true));
//        hotel_features.add(new HotelFeature("джакузи", "джакузи", true));
//        hotel_features.add(new HotelFeature("SPA-центр", "SPA-центр", false));
//        hotel_features.add(new HotelFeature("массаж", "массаж", false));
//        hotel_features.add(new HotelFeature("тренажерный зал", "тренажерный зал", true));
//        hotel_features.add(new HotelFeature("теннисный корт", "теннисный кор", true));
//        hotel_features.add(new HotelFeature("бильярд", "бильярд", false));
//        hotel_features.add(new HotelFeature("дайвинг", "дайвинг", false));
//        hotel_features.add(new HotelFeature("центр", "водных видов спорта", false));
//        hotel_features.add(new HotelFeature("анимация", "вечерняя", true));
//        hotel_features.add(new HotelFeature("музыка", "живая", true));
//        hotel_features.add(new HotelFeature("бассейн", "детский", true));
//        hotel_features.add(new HotelFeature("комната", "игровая", true));
//        hotel_features.add(new HotelFeature("площадка", "детская", true));
//        hotel_features.add(new HotelFeature("клуб", "детский (от 2-х до 12 лет)", true));
//        hotel_features.add(new HotelFeature("услуги няни", "по запросу", false));
//        hotel_features.add(new HotelFeature("стойка регистрации", "круглосуточная ", true));
//        hotel_features.add(new HotelFeature("камера хранения багажа", "камера хранения багажа ", true));
//        hotel_features.add(new HotelFeature("бюро", "экскурсионное", true));
//        hotel_features.add(new HotelFeature("услуги по глажке одежды", "услуги по глажке одежды ", false));
//        hotel_features.add(new HotelFeature("room service", "с 7:30 до 23:00", false));
//        hotel_features.add(new HotelFeature("Wi-Fi", "на территории", true));
//        hotel_features.add(new HotelFeature("конференц-зал", "конференц-зал", true));
//        hotel_features.add(new HotelFeature("ресторан", "ресторан", true));
//        hotel_features.add(new HotelFeature("бар", "у бассейна", true));
//        hotel_features.add(new HotelFeature("Mesoyios Fish", "таверна", false));
//        hotel_features.add(new HotelFeature("бассейн", "открытый с пресной водой", true));
//        hotel_features.add(new HotelFeature("зонтики", "у бассейна", true));
//        hotel_features.add(new HotelFeature("шезлонги", "шезлонги", true));
//        hotel_features.add(new HotelFeature("полотенца", "полотенца", false));
//        hotel_features.add(new HotelFeature("ресторан", "количество: 7 ", false));
//        hotel_features.add(new HotelFeature("зал", "конференц ", true));
//        hotel_features.add(new HotelFeature("прачечная", "(по будням)", true));
//        hotel_features.add(new HotelFeature("услуги доктора", "по запросу", true));
//        hotel_features.add(new HotelFeature("аренда детских колясок", "по запросу", false));
//        hotel_features.add(new HotelFeature("СПА-центр", "08:00-19:00, джакузи - бесплатно", false));
//        hotel_features.add(new HotelFeature("салон красоты", "10:00-19:00, парикмахерская по записи", false));
//        hotel_features.add(new HotelFeature("теннисный корт", "количество: 2", true));
//        hotel_features.add(new HotelFeature("площадка", "детская", true));
//        hotel_features.add(new HotelFeature("бассейн", "детский", true));
//        hotel_features.add(new HotelFeature("мини-клуб", "для детей 4-12 лет", true));
//        hotel_features.add(new HotelFeature("мини-диско", "мини-диско", true));
//        hotel_features.add(new HotelFeature("уроки", "кулинарии", true));
//        hotel_features.add(new HotelFeature("площадка", "детская", true));
//        hotel_features.add(new HotelFeature("клуб", "детский (от 2-х до 12 лет)", true));
//        hotel_features.add(new HotelFeature("услуги няни", "по запросу", false));
//        hotel_features.add(new HotelFeature("дневная анимация ", "аэробика, растяжка, йога, пилатес, уроки танцев, водный спортзал, " +
//                "водное поло, водный волейбол, стрельба из лука, дартс, бочча, настольный теннис, баскетбол, дегустация еды и напитков," +
//                " мастер-класс по приготовлению суши", true));
//        hotel_features.add(new HotelFeature("вечерняя шоу-программа", "вечера живой музыки, волшебные шоу," +
//                " фольклорные и международные танцевальные шоу, ночи викторины, бинго, семейные игры, семейные шоу, танцы, пианист", true));
//        hotel_features.add(new HotelFeature("бильярд", "бильярд", false));
//        hotel_features.add(new HotelFeature("дайвинг", "дайвинг", false));
//        hotel_features.add(new HotelFeature("игры", "настольные", true));
//        hotel_features.add(new HotelFeature("водные виды спорта", "предлагают сторонние организации", false));
//
//        hotelFeatureRepo.saveAllAndFlush(hotel_features);
//
//        List<Food> foods = new ArrayList<>();
//        foods.add(Food.BB);
//        foods.add(Food.HB);
//        foods.add(Food.FB);
//
//        List<Hotel> hotels = new ArrayList<>();
//        hotels.add(Hotel.builder()
//                .name("GRECIAN BAY")
//                .description("Отель расположен на одном из самых красивых песчаных пляжей Кипра, в 10 минутах ходьбы от центра Айя-Напы." +
//                        " Рестораны, бары, пабы, ночные клубы и магазины в шаговой доступности от отеля." +
//                        " До Парка скульптур под открытым небом 1 км, до Луна-парка 1,5 км, до международного аэропорта Ларнака 58 км")
//                .features(getByHotelName("GRECIAN BAY"))
//                .resort(resortService.getByIndex(0))
//                .stars(Stars.fivestar)
//                .food(foods)
//                .media("pathToFile").build());
//        hotels.add(Hotel.builder()
//                .name("Park Beach Hotel")
//                .description("Капитальная реновация отеля проведена зимой 2018-2019 г. Отель состоит из главного 2-этажного здания и семи 2-х этажных корпусов. " +
//                        "Преимущество отеля в его идеальном расположении — первая береговая линии, рядом с центром города Лимассол," +
//                        " в шаговой доступности от магазинов, ресторанов и баров. Отель подходит для семейных пар и отдыха с детьми.\n" +
//                        "Расположен в 60 км от аэропорта г. Ларнака, в 4 км от центра города.\n")
//                .features(getByHotelName("Park Beach Hotel"))
//                .resort(resortService.getByIndex(0))
//                .stars(Stars.threestar)
//                .food(foods)
//                .media("pathToFile").build());
//        hotels.add(Hotel.builder()
//                .name("Cavo Maris Beach")
//                .description("Расположен в 2 км от Пляжа залива Фигового дерева и в 2,4 км от Пляжа Коннос-Бэй," +
//                        " также недалеко от Пляжа Санрайз и порадует своих гостей панорамным видом, а также наличием турецкой бани, джакузи и спа-центра." +
//                        " Прогулка в центр Протараса займет 20 минут. Дойти до песчаного пляжа можно без особых усилий." +
//                        " Расположен недалеко от нескольких известных достопримечательностей, среди которых Cavo Maris Beach.")
//                .features(getByHotelName("GRECIAN BAY"))
//                .resort(resortService.getByIndex(0))
//                .stars(Stars.fourstar)
//                .food(foods)
//                .media("pathToFile").build());
//
//        hotelRepo.saveAll(hotels);



        bot.executeAsync(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Test")
//                .text(resortService.getByActivities(activityRepo.findAll()).toString())
                .build());


    }

    private List<HotelFeature> getByHotelName(String name) {
        Set<HotelFeature> hotel_features = new HashSet<>();
        switch (name) {
            case "GRECIAN BAY" -> {
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бассейн", "открытый "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бассейн", "крытый "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("джакузи", "джакузи"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("SPA-центр", "SPA-центр"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("массаж", "массаж"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("тренажерный зал", "тренажерный зал"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("теннисный корт", "теннисный кор"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бильярд", "бильярд"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("дайвинг", "дайвинг"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("центр", "водных видов спорта"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("анимация", "вечерняя"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("музыка", "живая"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бассейн", "детский"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("комната", "игровая"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("площадка", "детская"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("клуб", "детский (от 2-х до 12 лет)"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("услуги няни", "по запросу"));
            }
            case "Park Beach Hotel" -> {
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("стойка регистрации", "круглосуточная "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("камера хранения багажа", "камера хранения багажа "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бюро", "экскурсионное"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("услуги по глажке одежды", "услуги по глажке одежды "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("room service", "с 7:30 до 23:00"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("Wi-Fi", "на территории"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("конференц-зал", "конференц-зал"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("ресторан", "ресторан"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бар", "у бассейна"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("Mesoyios Fish", "таверна"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бассейн", "открытый с пресной водой"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("зонтики", "у бассейна"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("шезлонги", "шезлонги"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("полотенца", "полотенца"));
            }
            case "Cavo Maris Beach" -> {
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("ресторан", "количество: 7 "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("зал", "конференц "));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("прачечная", "(по будням)"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("услуги доктора", "по запросу"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("аренда детских колясок", "по запросу"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("СПА-центр", "08:00-19:00, джакузи - бесплатно"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("салон красоты", "10:00-19:00, парикмахерская по записи"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("теннисный корт", "количество: 2"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("площадка", "детская"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бассейн", "детский"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("мини-клуб", "для детей 4-12 лет"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("мини-диско", "мини-диско"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("уроки", "кулинарии"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("площадка", "детская"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("клуб", "детский (от 2-х до 12 лет)"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("услуги няни", "по запросу"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("дневная анимация ", "аэробика, растяжка, йога, пилатес, уроки танцев, водный спортзал, " +
                        "водное поло, водный волейбол, стрельба из лука, дартс, бочча, настольный теннис, баскетбол, дегустация еды и напитков," +
                        " мастер-класс по приготовлению суши"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("вечерняя шоу-программа", "вечера живой музыки, волшебные шоу," +
                        " фольклорные и международные танцевальные шоу, ночи викторины, бинго, семейные игры, семейные шоу, танцы, пианист"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("бильярд", "бильярд"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("дайвинг", "дайвинг"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("игры", "настольные"));
                hotel_features.add(hotelFeatureRepo.findFirstByNameAndDescription("водные виды спорта", "предлагают сторонние организации"));
            }
            default -> {
            }
        }
        return new ArrayList<>(hotel_features);
    }
}

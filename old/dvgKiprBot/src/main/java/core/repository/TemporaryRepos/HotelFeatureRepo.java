package core.repository.TemporaryRepos;

import core.models.HotelFeature;

import java.util.ArrayList;
import java.util.List;

public class HotelFeatureRepo {
    public List<HotelFeature> hotelFeatures(String hotelName) {

        List<HotelFeature> hotel_features = new ArrayList<>();

        switch (hotelName) {
            case "GRECIAN BAY":
                hotel_features.add(new HotelFeature(1L, "бассейн", "открытый ", true));
                hotel_features.add(new HotelFeature(2L, "бассейн", "крытый ", true));
                hotel_features.add(new HotelFeature(3L, "джакузи", "джакузи", true));
                hotel_features.add(new HotelFeature(4L, "SPA-центр", "SPA-центр", false));
                hotel_features.add(new HotelFeature(5L, "массаж", "массаж", false));
                hotel_features.add(new HotelFeature(6L, "тренажерный зал", "тренажерный зал", true));
                hotel_features.add(new HotelFeature(7L, "теннисный корт", "теннисный кор", true));
                hotel_features.add(new HotelFeature(8L, "бильярд", "бильярд", false));
                hotel_features.add(new HotelFeature(9L, "дайвинг", "дайвинг", false));
                hotel_features.add(new HotelFeature(10L, "центр", "водных видов спорта", false));
                hotel_features.add(new HotelFeature(11L, "анимация", "вечерняя", true));
                hotel_features.add(new HotelFeature(12L, "музыка", "живая", true));
                hotel_features.add(new HotelFeature(13L, "бассейн", "детский", true));
                hotel_features.add(new HotelFeature(14L, "комната", "игровая", true));
                hotel_features.add(new HotelFeature(15L, "площадка", "детская", true));
                hotel_features.add(new HotelFeature(16L, "клуб", "детский (от 2-х до 12 лет)", true));
                hotel_features.add(new HotelFeature(17L, "услуги няни", "по запросу", false));
                break;
            case "Park Beach Hotel":
                hotel_features.add(new HotelFeature(1L, "стойка регистрации", "круглосуточная ", true));
                hotel_features.add(new HotelFeature(2L, "камера хранения багажа", "камера хранения багажа ", true));
                hotel_features.add(new HotelFeature(3L, "бюро", "экскурсионное", true));
                hotel_features.add(new HotelFeature(4L, "услуги по глажке одежды", "услуги по глажке одежды ", false));
                hotel_features.add(new HotelFeature(5L, "room service", "с 7:30 до 23:00", false));
                hotel_features.add(new HotelFeature(6L, "Wi-Fi", "на территории", true));
                hotel_features.add(new HotelFeature(7L, "конференц-зал", "конференц-зал", true));
                hotel_features.add(new HotelFeature(8L, "ресторан", "ресторан", true));
                hotel_features.add(new HotelFeature(9L, "бар", "у бассейна", true));
                hotel_features.add(new HotelFeature(10L, "Mesoyios Fish", "таверна", false));
                hotel_features.add(new HotelFeature(11L, "бассейн", "открытый с пресной водой", true));
                hotel_features.add(new HotelFeature(12L, "зонтики", "у бассейна", true));
                hotel_features.add(new HotelFeature(13L, "шезлонги", "шезлонги", true));
                hotel_features.add(new HotelFeature(14L, "полотенца", "полотенца", false));
                break;
            case "Cavo Maris Beach":
                hotel_features.add(new HotelFeature(1L, "ресторан", "количество: 7 ", false));
                hotel_features.add(new HotelFeature(2L, "зал", "конференц ", true));
                hotel_features.add(new HotelFeature(3L, "прачечная", "(по будням)", true));
                hotel_features.add(new HotelFeature(4L, "услуги доктора", "по запросу", true));
                hotel_features.add(new HotelFeature(5L, "аренда детских колясок", "по запросу", false));
                hotel_features.add(new HotelFeature(6L, "СПА-центр", "08:00-19:00, джакузи - бесплатно", false));
                hotel_features.add(new HotelFeature(7L, "салон красоты", "10:00-19:00, парикмахерская по записи", false));
                hotel_features.add(new HotelFeature(8L, "теннисный корт", "количество: 2", true));
                hotel_features.add(new HotelFeature(9L, "площадка", "детская", true));
                hotel_features.add(new HotelFeature(10L, "бассейн", "детский", true));
                hotel_features.add(new HotelFeature(11L, "мини-клуб", "для детей 4-12 лет", true));
                hotel_features.add(new HotelFeature(12L, "мини-диско", "мини-диско", true));
                hotel_features.add(new HotelFeature(13L, "уроки", "кулинарии", true));
                hotel_features.add(new HotelFeature(14L, "площадка", "детская", true));
                hotel_features.add(new HotelFeature(15L, "клуб", "детский (от 2-х до 12 лет)", true));
                hotel_features.add(new HotelFeature(16L, "услуги няни", "по запросу", false));
                hotel_features.add(new HotelFeature(17L, "дневная анимация ", "аэробика, растяжка, йога, пилатес, уроки танцев, водный спортзал, " +
                        "водное поло, водный волейбол, стрельба из лука, дартс, бочча, настольный теннис, баскетбол, дегустация еды и напитков," +
                        " мастер-класс по приготовлению суши", true));
                hotel_features.add(new HotelFeature(18L, "вечерняя шоу-программа", "вечера живой музыки, волшебные шоу," +
                        " фольклорные и международные танцевальные шоу, ночи викторины, бинго, семейные игры, семейные шоу, танцы, пианист", true));
                hotel_features.add(new HotelFeature(19L, "бильярд", "бильярд", false));
                hotel_features.add(new HotelFeature(20L, "дайвинг", "дайвинг", false));
                hotel_features.add(new HotelFeature(21L, "игры", "настольные", true));
                hotel_features.add(new HotelFeature(22L, "водные виды спорта", "предлагают сторонние организации", false));
                break;
            default:
                break;
        }

        return hotel_features;
    }
}

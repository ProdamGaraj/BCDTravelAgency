package core.repository.TemporaryRepos;

import core.models.Food;
import core.models.Hotel;
import core.models.Stars;

import java.util.ArrayList;
import java.util.List;

public class HotelRepo {

    private final HotelFeatureRepo hotelFeatureRepo = new HotelFeatureRepo();

    private final ResortRepo resortRepo = new ResortRepo();

    public List<Hotel> hotelList() {

        List<Food> foods = new ArrayList<>();
        foods.add(Food.BB);
        foods.add(Food.HB);
        foods.add(Food.FB);

        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel(1L,
                "GRECIAN BAY",
                "Отель расположен на одном из самых красивых песчаных пляжей Кипра, в 10 минутах ходьбы от центра Айя-Напы." +
                " Рестораны, бары, пабы, ночные клубы и магазины в шаговой доступности от отеля." +
                " До Парка скульптур под открытым небом 1 км, до Луна-парка 1,5 км, до международного аэропорта Ларнака 58 км",
                hotelFeatureRepo.hotelFeatures("GRECIAN BAY"),
                resortRepo.resortList().get(0),
                Stars.fivestar,
                foods,
                "pathToFile"
                ));
        hotels.add(new Hotel(2L,
                "Park Beach Hotel",
                "Капитальная реновация отеля проведена зимой 2018-2019 г. Отель состоит из главного 2-этажного здания и семи 2-х этажных корпусов. " +
                        "Преимущество отеля в его идеальном расположении — первая береговая линии, рядом с центром города Лимассол," +
                        " в шаговой доступности от магазинов, ресторанов и баров. Отель подходит для семейных пар и отдыха с детьми.\n" +
                        "Расположен в 60 км от аэропорта г. Ларнака, в 4 км от центра города.\n",
                hotelFeatureRepo.hotelFeatures("Park Beach Hotel"),
                resortRepo.resortList().get(0), Stars.threestar,
                foods,
                "pathToFile"

        ));
        hotels.add(new Hotel(3L,
                "Cavo Maris Beach",
                "Расположен в 2 км от Пляжа залива Фигового дерева и в 2,4 км от Пляжа Коннос-Бэй," +
                        " также недалеко от Пляжа Санрайз и порадует своих гостей панорамным видом, а также наличием турецкой бани, джакузи и спа-центра." +
                        " Прогулка в центр Протараса займет 20 минут. Дойти до песчаного пляжа можно без особых усилий." +
                        " Расположен недалеко от нескольких известных достопримечательностей, среди которых Cavo Maris Beach.",
                hotelFeatureRepo.hotelFeatures("GRECIAN BAY"),
                resortRepo.resortList().get(0),
                Stars.fourstar,
                foods,
                "pathToFile"
        ));

        return hotels;
    }
}

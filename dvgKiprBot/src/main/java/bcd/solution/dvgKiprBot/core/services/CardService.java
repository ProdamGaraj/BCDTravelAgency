package bcd.solution.dvgKiprBot.core.services;

import bcd.solution.dvgKiprBot.core.models.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    @SneakyThrows
    public String getHotelCard(Hotel hotel) {
//        StringBuilder features = new StringBuilder();
//        for (HotelFeature feature : hotel.features) {
//            features.append("- ").append(feature.name).append("\n");
//        }
//        StringBuilder foods = new StringBuilder();
//        for (Food food : hotel.food) {
//            foods.append("- ").append(food).append("\n");
//        }
        return hotel.name + " " + hotel.stars.toString() + "\n\n" +
                "Относиться к курорту " + hotel.resort.name + "\n\n" +
                hotel.description
//                + "\n\n"
//                + "Особенноси:\n" +
//                features + "\n\n" +
//                "Доступные типы питания:\n" +
//                foods
                ;
    }

    @SneakyThrows
    public String getResortCard(Resort resort) {
        StringBuilder activity_list = new StringBuilder();
        for (Activity activity : resort.activities) {
            activity_list.append("- ").append(activity.name).append("\n");
        }

        return resort.name + "\n\n"
                + resort.description + "\n\n"
                + resort.geo + "\n\n"
                + "Доступные активности:\n"
                + activity_list;
    }

    @SneakyThrows
    public String getCustomTourCard(CustomTour customTour) {
        return customTour.name + "\n\n" +
                customTour.description;
    }

    @SneakyThrows
    public String getManagerCard(StateMachine stateMachine) {
        StringBuilder card = new StringBuilder(
                "Пользователь подобрал тур\n\n" +
                        "Тег: @" + stateMachine.user.getLogin() + "\n" +
                        "Номер телефона: " + stateMachine.user.getPhone() + "\n\n");

        fillTourInfo(card, stateMachine);

        card.append("Вскоре он с Вами свяжется для завершения оформления тура.");
        return card.toString();
    }

    @SneakyThrows
    public String getUserCard(StateMachine stateMachine,
                              String managerUsername,
                              List<String> contactPhones) {
        StringBuilder card = new StringBuilder("Спасибо, что выбрали нас!\n\nВаш выбор:\n");

        fillTourInfo(card, stateMachine);

        card.append("Обратитесь к менеджеру (@")
                .append(managerUsername)
                .append(") для расчёта выбранного тура или свяжитесь с нами по номерам:");
        if (!contactPhones.isEmpty()) {
            for (String phone : contactPhones) {
                card.append("\n- ").append(phone);
            }
        }
        return card.toString();
    }


    private void fillTourInfo(StringBuilder card, StateMachine stateMachine) {
        if (stateMachine.customTour != null) {
            card.append("Авторский тур: ").append(stateMachine.customTour.name).append("\n\n");
        } else {
            if (stateMachine.resort != null) {
                card.append("Курорт: ").append(stateMachine.resort.name).append("\n");
            }
            card.append("Отель: ").append(stateMachine.hotel.name).append("\n\n");
        }
    }
}

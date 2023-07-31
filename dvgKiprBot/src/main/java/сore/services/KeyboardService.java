package сore.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Service
public class KeyboardService {

    private int selectedIndex = 0;

    public InlineKeyboardMarkup getTourChoosingKeyboard() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Активности")
                                .callbackData("activities")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Курорты")
                                .callbackData("resorts")
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Авторские туры")
                                .callbackData("personal_tours")
                        .build()))
                .build();
    }

    public InlineKeyboardMarkup getActivitiesKeyboard() {

        List<String> activities = new ArrayList<>();
        activities.add("Пиво");
        activities.add("Водка");
        activities.add("Раки");
        activities.add("Текст");
        activities.add("Ещё один текст");
        activities.add("Описание");

        String currentActivity = activities.get(selectedIndex);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("activity_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentActivity)
                                .callbackData("activity_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("activity_right")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getResortsKeyboard() {
        List<String> resorts = new ArrayList<>();
        resorts.add("Я");
        resorts.add("Ты");
        resorts.add("Он");
        resorts.add("Она");
        resorts.add("Оно");
        resorts.add("Они");

        String currentResort = resorts.get(selectedIndex);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("resort_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentResort)
                                .callbackData("resort_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("resort_right")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getPersonalToursKeyboard() {

        List<String> personalTours = new ArrayList<>();
        personalTours.add("I");
        personalTours.add("am");
        personalTours.add("liking");
        personalTours.add("banana");
        personalTours.add("now");
        personalTours.add("!");

        String currentPersonalTours = personalTours.get(selectedIndex);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("personalTour_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentPersonalTours)
                                .callbackData("personalTour_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("personalTour_right")
                                .build()
                ))
                .build();
    }

    public InlineKeyboardMarkup getHotelChoosingKeyboard() {

        List<String> hotels = new ArrayList<>();
        hotels.add("ADELAIS HOTEL");
        hotels.add("MANDALI");
        hotels.add("TOXOTIS");
        hotels.add("ATLANTICA AQUA BLUE");
        hotels.add("BOHEMIAN GARDENS");
        hotels.add("CAVO MARIS BEACH");
        hotels.add("CRYSTAL SPRINGS");

        String currentHotel = hotels.get(selectedIndex);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder()
                                .text("<-")
                                .callbackData("hotel_left")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(currentHotel)
                                .callbackData("hotel_select")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("->")
                                .callbackData("hotel_right")
                                .build()
                ))
                .build();
    }
}
